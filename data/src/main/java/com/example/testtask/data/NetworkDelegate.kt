package com.example.testtask.data

import com.example.testtask.data.root.IBaseRetrofitService
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class NetworkDelegate<T>(
    private val iService: Class<T>
) : ReadOnlyProperty<Any, T> {

    @Inject
    lateinit var retrofit: Retrofit

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return retrofit.create(iService)
    }

}

inline fun <reified T : IBaseRetrofitService> networkDelegate() = NetworkDelegate(T::class.java)