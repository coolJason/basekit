package com.moregood.kit.utils;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 *管理Disposable
 */
public class CompsiteDisposableHelper {
    //创建 SingleObject 的一个对象
    private static CompsiteDisposableHelper instance = new CompsiteDisposableHelper();
    private CompositeDisposable compositeDisposable=new CompositeDisposable();

    //获取唯一可用的对象
    public static CompsiteDisposableHelper getInstance(){
        return instance;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
