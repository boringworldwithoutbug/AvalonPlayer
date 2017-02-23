package com.avalon.avalonplayer.net;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuqiuqiang on 2017/1/23.
 */

public class BaseObservable<T> implements ObservableOnSubscribe<T> {

    Type mType;
    Observable<String> observable;

    public BaseObservable(Type mType, Observable<String> observable) {
        this.mType = mType;
        this.observable = observable;
    }

    @Override
    public void subscribe(final ObservableEmitter<T> e) throws Exception {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        T t = new GsonBuilder().serializeNulls().create()
                                .fromJson(s, mType);

                        e.onNext(t);
                        e.onComplete();
                    }
                });
    }
}
