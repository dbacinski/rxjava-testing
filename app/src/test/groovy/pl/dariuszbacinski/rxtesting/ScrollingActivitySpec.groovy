package pl.dariuszbacinski.rxtesting

import android.Manifest
import com.tbruyelle.rxpermissions.RxPermissions
import pl.polidea.robospock.RoboSpecification
import rx.Observable
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject
import rx.subjects.Subject

class RequestPermissionSpec extends RoboSpecification {
    RxPermissions rxPermissionsMock = Mock(RxPermissions)

    def "error when permission where not granted - observable"() {
        given:
            Observable<Boolean> booleanObservable = Observable.just(Boolean.valueOf(false))
            RequestPermission objectUnderTest = new RequestPermission(rxPermissionsMock)
            rxPermissionsMock.request(Manifest.permission.ACCESS_COARSE_LOCATION) >> booleanObservable
            TestSubscriber<String> subscriber = new TestSubscriber<>()
        when:
            objectUnderTest.requestLocationPermission().subscribe(subscriber)
        then:
            //subscriber.awaitTerminalEvent() //hangs
            subscriber.assertError(SecurityException) //failed
    }

    def "error when permission where not granted - subject"() {
        given:
            Subject<Boolean> booleanSubject = PublishSubject.create()
            RequestPermission objectUnderTest = new RequestPermission(rxPermissionsMock)
            rxPermissionsMock.request(Manifest.permission.ACCESS_COARSE_LOCATION) >> booleanSubject.asObservable()
            TestSubscriber<String> subscriber = new TestSubscriber<>()
        when:
            objectUnderTest.requestLocationPermission().subscribe(subscriber)
            booleanSubject.onNext(false)
            booleanSubject.onCompleted()
        then:
            //subscriber.awaitTerminalEvent() //hangs
            subscriber.assertError(SecurityException)
    }

    def "error when permission where not granted - subject sleep"() {
        given:
            Subject<Boolean> booleanSubject = PublishSubject.create()
            RequestPermission objectUnderTest = new RequestPermission(rxPermissionsMock)
            rxPermissionsMock.request(Manifest.permission.ACCESS_COARSE_LOCATION) >> booleanSubject.asObservable()
            TestSubscriber<String> subscriber = new TestSubscriber<>()
        when:
            objectUnderTest.requestLocationPermission().subscribe(subscriber)
            sleep 10 //why this sleep helps?
            booleanSubject.onNext(false)
        then:
            subscriber.awaitTerminalEvent()
            subscriber.assertError(SecurityException) //ok
    }
}
