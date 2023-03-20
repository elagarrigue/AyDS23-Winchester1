package ayds.observer

import java.util.*

class Subject<T> : Observable<T>, Publisher<T> {
    
    private val observers: MutableList<Observer<T>> = ArrayList()
    
    private var value: T? = null
    
    override fun subscribe(observer: Observer<T>) {
        observers.add(observer)
    }

    override fun unSubscribe(observer: Observer<T>) {
        observers.remove(observer)
    }

    override fun notify(value: T) {
        this.value = value
        observers.forEach { 
            it.update(value)
        }
    }

    fun lastValue(): T? {
        return value
    }
}