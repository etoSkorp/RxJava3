package com.example.rxjava3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava3.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAnotherActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        binding.btnPublishSubject.setOnClickListener {
            binding.tvObserverInfo.text = ""
            doWorkPublishSubject()
        }

        binding.btnReplaySubject.setOnClickListener {
            binding.tvObserverInfo.text = ""
            doWorkReplaySubject()
        }

        binding.btnBehaviorSubject.setOnClickListener {
            binding.tvObserverInfo.text = ""
            doWorkBehaviorSubject()
        }

        binding.btnAsyncSubject.setOnClickListener {
            binding.tvObserverInfo.text = ""
            doWorkAsyncSubject()
        }
    }

    // Излучает(emit) все последующие элементы наблюдаемого источника в момент подписки
    private fun doWorkPublishSubject() {
        val source = PublishSubject.create<Int>()

        // Получит 1, 2, 3, 4, 5 и onComplete
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        // Получит 4, 5 и onComplete для следующего наблюдателя
        source.subscribe(getSecondObserver())
        source.onNext(4)
        source.onNext(5)
        source.onComplete()
    }


    private fun doWorkReplaySubject() {
        val source = ReplaySubject.create<Int>()

        // Он получит 1, 2, 3, 4
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        source.onNext(4)
        source.onComplete()
        // Он также получит 1, 2, 3, 4 так как он использует Replay Subject
        source.subscribe(getSecondObserver())
    }

    // Он излучает (emit) совсем недавно созданый элемент и все последующие элементы наблюдаемого источника,
    // когда наблюдатель (observer) присоединяется к нему.
    private fun doWorkBehaviorSubject() {
        val source = BehaviorSubject.create<Int>()

        // Получит 1, 2, 3, 4 and onComplete
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        // Получит 3 (последний элемент), 4, 5 (последующие элементы) и onComplete
        source.subscribe(getSecondObserver())
        source.onNext(4)
        source.onNext(5)
        source.onComplete()
    }

    //  Он выдает только последнее значение наблюдаемого источника (и только последнее).
    private fun doWorkAsyncSubject() {
        val source = AsyncSubject.create<Int>()

        // Получит только 4 и onComplete
        source.subscribe(getFirstObserver())
        source.onNext(1)
        source.onNext(2)
        source.onNext(3)
        // Тоже получит только 4 и onComplete
        source.subscribe(getSecondObserver())
        source.onNext(4)
        source.onComplete()
    }

    private fun getFirstObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.d("LOG", "First onSubscribe ${d.isDisposed}");
            }

            override fun onNext(t: Int) {
                binding.tvObserverInfo.append("First onNext $t\n");
                Log.d("LOG", "First onNext $t");
            }

            override fun onError(e: Throwable) {
                binding.tvObserverInfo.append("First onError $e\n");
                Log.d("LOG", "First onError $e");
            }

            override fun onComplete() {
                binding.tvObserverInfo.append("First onComplete\n");
                Log.d("LOG", "First onComplete");
            }
        }
    }

    private fun getSecondObserver(): Observer<Int> {
        return object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.d("LOG", "Second onSubscribe ${d.isDisposed}");
            }

            override fun onNext(t: Int) {
                binding.tvObserverInfo.append("Second onNext $t\n");
                Log.d("LOG", "Second onNext $t");
            }

            override fun onError(e: Throwable) {
                binding.tvObserverInfo.append("Second onError $e\n");
                Log.d("LOG", "Second onError $e");
            }

            override fun onComplete() {
                binding.tvObserverInfo.append("Second onComplete\n");
                Log.d("LOG", "Second onComplete");
            }

        }
    }
}