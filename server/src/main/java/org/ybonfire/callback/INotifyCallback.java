package org.ybonfire.callback;

/**
 * 通知回调
 *
 * @author yuanbo
 * @date 2023-09-15 17:07
 */
public interface INotifyCallback {
    void onSuccess();

    void onException(final Throwable ex);
}
