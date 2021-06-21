package com.moregood.kit.net;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * @类名 ErrorHandler
 * @描述
 * @作者 xifengye
 * @创建时间 2020/8/16 17:11
 * @邮箱 ye_xi_feng@163.com
 */
public class ErrorHandler implements Consumer<Throwable> {
    ZSubscriber subscriber ;

    public ErrorHandler(ZSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void accept(Throwable throwable) throws Throwable {
        if(subscriber!=null){
            subscriber.onError(throwable);
        }
    }
}
