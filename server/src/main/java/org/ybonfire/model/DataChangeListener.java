package org.ybonfire.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.ybonfire.callback.INotifyCallback;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LongPollingListener
 *
 * @author yuanbo
 * @date 2023-09-15 14:38
 */
@Data
public class DataChangeListener {
    private static final long FIXED_RESPONSE_TIME_MILLIS = 5 * 1000L;// 5s
    private final String id;
    private final long timeoutTime;
    private final AsyncContext context;
    private final INotifyCallback callback;

    public DataChangeListener(final String id, final Long timeoutMillis, final AsyncContext context,
        final INotifyCallback callback) {
        this.id = id;
        this.timeoutTime = timeoutMillis == null ? System.currentTimeMillis() + FIXED_RESPONSE_TIME_MILLIS
            : System.currentTimeMillis() + timeoutMillis;
        this.context = context;
        this.callback = callback;
    }

    public boolean isTimeout() {
        return timeoutTime - System.currentTimeMillis() < 0L;
    }

    public void onDataChange(final ConfigDataChangeEvent event) {
        try {
            sendResponse(event);
            callback.onSuccess();
        } catch (Throwable ex) {
            callback.onException(ex);
        }
    }

    public void sendResponse(final ConfigDataChangeEvent event) throws IOException {
        final HttpServletResponse response = (HttpServletResponse)context.getResponse();
        if (event == null) {
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
        } else {
            response.setStatus(HttpStatus.OK.value());
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(BaseResult.ok(event)));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        context.complete();

    }
}
