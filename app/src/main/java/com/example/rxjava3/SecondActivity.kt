package com.example.rxjava3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava3.databinding.ActivitySecondBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val someObservable = Observable.just(1, 2, 25, 4, 5, 14)

        someObservable
            .subscribeOn(Schedulers.newThread())
            .delay(1000L, TimeUnit.MILLISECONDS)
            .repeat(3)
            .doOnError {
                Log.d("RxJava3Error", "doOnError ${it.localizedMessage}")
            }
            .filter {
                it >= 3
            }
            .doOnNext {
                Log.d("RxJava3", "doOnNext ${Thread.currentThread().name}")
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d("RxJava3AnotherThread", "doOnNext ${Thread.currentThread().name}")
            }
            .subscribe(
                {
                    Log.d("RxJava3", "onNext $it")
                },
                {},
                {}
            )

        Flowable
            .just(1, 1, 1, 4, 5, 6, 7, 8, 9, 10)
            .onBackpressureBuffer(4)
            .subscribe({
                Log.d(
                    "RxJava3", "onNext $it"
                )
            }, {}, {}
            )

        Single.just(1)
            .subscribe({

            }, {

            })
    }
}