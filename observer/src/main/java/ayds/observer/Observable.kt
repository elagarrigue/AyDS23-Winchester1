package ayds.observer

interface Observable<T> {
    fun subscribe(observer: Observer<T>)
    fun unSubscribe(observer: Observer<T>)
}

fun interface Observer<T> {
    fun update(value: T)
}

interface Publisher<T> {
    fun notify(value: T)
}