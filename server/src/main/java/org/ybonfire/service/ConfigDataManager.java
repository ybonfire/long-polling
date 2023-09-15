package org.ybonfire.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ybonfire.callback.INotifyCallback;
import org.ybonfire.model.ConfigData;
import org.ybonfire.model.ConfigDataChangeEvent;
import org.ybonfire.model.DataChangeListener;
import org.ybonfire.model.DataChangeTypeEnum;
import org.ybonfire.model.request.AddListenerRequest;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * ConfigDataManager
 *
 * @author yuanbo
 * @date 2023-09-15 14:40
 */
@Slf4j
@Service
public class ConfigDataManager {
    private final Map<String, ConfigData> configDataTable = new ConcurrentHashMap<>();
    private final Map<String, Map<String, DataChangeListener>> listenersTable = new ConcurrentHashMap<>();
    private final ExecutorService notifier = ForkJoinPool.commonPool();
    private final Thread timeoutDetector;

    public ConfigDataManager() {
        this.timeoutDetector = new Thread(new TimeoutDetector());
        this.timeoutDetector.setDaemon(true);
        timeoutDetector.start();
    }

    /**
     * @description: 添加配置
     * @param:
     * @return:
     * @date: 2023/09/15 14:42:14
     */
    public synchronized void add(final ConfigData data) {
        final String dataId = data.getId();
        final ConfigData prev = configDataTable.put(dataId, data);
        final Map<String, DataChangeListener> listeners = listenersTable.get(dataId);
        if (listeners != null && listeners.size() > 0) {
            for (final DataChangeListener listener : listeners.values()) {
                notifier.execute(() -> notifyListener(listener, data,
                    prev == null ? DataChangeTypeEnum.ADD : DataChangeTypeEnum.UPDATE));
            }
        }
    }

    /**
     * @description: 删除配置
     * @param:
     * @return:
     * @date: 2023/09/15 14:42:17
     */
    public synchronized void remove(final String dataId) {
        final ConfigData prev = configDataTable.remove(dataId);
        final Map<String, DataChangeListener> listeners = listenersTable.get(dataId);
        if (listeners != null && listeners.size() > 0) {
            for (final DataChangeListener listener : listeners.values()) {
                if (prev != null) {
                    notifier.execute(() -> notifyListener(listener, prev, DataChangeTypeEnum.DELETE));
                }
            }
        }
    }

    /**
     * @description:
     * @param:
     * @return:
     * @date: 2023/09/15 16:43:34
     */
    public synchronized void addListener(final AddListenerRequest request, final HttpServletRequest req,
        final HttpServletResponse resp) {
        final String dataId = request.getDataId();

        final AsyncContext context = req.startAsync(req, resp);
        context.setTimeout(-1L);
        final DataChangeListener listener =
            new DataChangeListener(request.getListenerId(), request.getTimeoutMillis(), context, new INotifyCallback() {
                @Override
                public void onSuccess() {
                    final Map<String, DataChangeListener> listeners = listenersTable.get(dataId);
                    if (listeners != null) {
                        listeners.remove(request.getListenerId());
                    }
                }

                @Override
                public void onException(Throwable ex) {
                    System.out.println("notify failed");
                }
            });

        final Map<String, DataChangeListener> listeners = new ConcurrentHashMap<>();
        final Map<String, DataChangeListener> prev = listenersTable.putIfAbsent(dataId, listeners);
        if (prev != null) {
            prev.put(listener.getId(), listener);
        } else {
            listeners.put(listener.getId(), listener);
        }
    }

    /**
     * @description: 通知监听者
     * @param:
     * @return:
     * @date: 2023/09/15 17:02:05
     */
    private void notifyListener(final DataChangeListener listener, final ConfigData configData,
        final DataChangeTypeEnum changeType) {
        final ConfigDataChangeEvent event = new ConfigDataChangeEvent(configData, changeType);
        listener.onDataChange(event);
    }

    /**
     * @description: 超时检测器
     * @author: yuanbo
     * @date: 2023/9/15
     */
    private class TimeoutDetector implements Runnable {

        @Override
        public void run() {
            while (true) {
                final List<DataChangeListener> listeners = listenersTable.values().stream().map(Map::values)
                    .flatMap(Collection::stream).collect(Collectors.toList());
                for (final DataChangeListener listener : listeners) {
                    if (listener.isTimeout()) {
                        listener.onDataChange(null);
                    }
                }
            }
        }
    }
}
