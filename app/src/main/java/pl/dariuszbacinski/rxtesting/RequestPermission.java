package pl.dariuszbacinski.rxtesting;

import android.Manifest;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RequestPermission {


RxPermissions rxPermissions;

    public RequestPermission(RxPermissions rxPermissions) {
        this.rxPermissions = rxPermissions;
    }

    Observable<String> requestLocationPermission(){
        return rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean granted) {
                if(granted) {
                    return Observable.just("Good job!");
                } else {
                    throw new SecurityException();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
