/* https://www.youtube.com/watch?v=ME3LH2bib3g&ab_channel=PhilippLackner
How to Build a Clean Architecture GraphQL App With Kotlin - Android Studio Tutorial
Date 5/11/2024 accessed
All code here is adapted from the video
The code for okayHttp injections I learned how to do through this video as well*/

package com.example.delitelligencefrontend.di


import com.apollographql.apollo.ApolloClient
import com.example.delitelligencefrontend.data.ApolloEmployeeClient
import com.example.delitelligencefrontend.data.ApolloProductClient
import com.example.delitelligencefrontend.domain.EmployeeClient
import com.example.delitelligencefrontend.domain.GetEmployeesUseCase
import com.example.delitelligencefrontend.domain.GetProductsUseCase
import com.example.delitelligencefrontend.domain.ProductClient
import com.example.delitelligencefrontend.domain.WeightApiService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.12:5000")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMyApiService(retrofit: Retrofit): WeightApiService {
        return retrofit.create(WeightApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("http://192.168.1.12:8080/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeClient(apolloClient: ApolloClient): EmployeeClient {
        return ApolloEmployeeClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetEmployeesUseCase(employeeClient: EmployeeClient): GetEmployeesUseCase {
        return GetEmployeesUseCase(employeeClient)
    }

    @Provides
    @Singleton
    fun provideProductClient(apolloClient: ApolloClient): ProductClient {
        return ApolloProductClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productClient: ProductClient): GetProductsUseCase {
        return GetProductsUseCase(productClient)
    }



}