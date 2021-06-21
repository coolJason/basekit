package com.moregood.kit.net;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * @类名 ErrorConsumer
 * @描述
 * @作者 xifengye
 * @创建时间 2020/8/16 17:06
 * @邮箱 ye_xi_feng@163.com
 */
public abstract class ErrorConsumer implements Consumer<Throwable> {
    @Override
    public void accept(Throwable throwable) throws Throwable {
        onError(throwable);
    }

    public abstract void onError(Throwable throwable);
}
