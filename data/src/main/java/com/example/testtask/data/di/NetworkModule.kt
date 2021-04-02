package com.example.testtask.data.di

import android.content.Context
import com.example.testtask.root.BuildConfig
import com.example.testtask.root.di.ApplicationContext
import com.example.testtask.root.di.AuthInterceptor
import com.example.testtask.root.utils.SharedPreferencesHelper
//import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @AuthInterceptor
    fun providesAuthInterceptor(sharedPreferences: SharedPreferencesHelper):Interceptor{
        return Interceptor {
            val initialRequest = it.request()
            val request = initialRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + sharedPreferences.getAuthToken())
                .header("deviceToken", sharedPreferences.getFirebaseToken() ?: "")
                .header("deviceId", sharedPreferences.getDeviceId() ?: "")
                .header("osType", "Android")
                .removeHeader("Pragma")
                .build()
            val response = it.proceed(request)
            response.cacheResponse()
            response
        }
    }

    @Provides
    fun providesOkHttpClient(@AuthInterceptor authInterceptor:Interceptor, @ApplicationContext context: Context): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
//                .addInterceptor(ChuckInterceptor(context))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
    }


    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

}
