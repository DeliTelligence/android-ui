package com.example.delitelligencefrontend.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.example.delitelligencefrontend.data.*
import com.example.delitelligencefrontend.domain.*
import com.example.delitelligencefrontend.domain.interfaces.*
import com.example.delitelligencefrontend.model.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideEmployeesUseCase(employeeClient: EmployeeClient): EmployeesUseCase {
        return EmployeesUseCase(employeeClient)
    }

    @Provides
    @Singleton
    fun provideProductClient(apolloClient: ApolloClient): ProductClient {
        return ApolloProductClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productClient: ProductClient): ProductsUseCase {
        return ProductsUseCase(productClient)
    }

    @Provides
    @Singleton
    fun provideSaleClient(apolloClient: ApolloClient): SaleClient {
        return ApolloSaleClient(apolloClient)
    }

    @Provides
    @Singleton
    fun providePostDeliSaleUseCase(saleClient: SaleClient): DeliSaleUseCase {
        return DeliSaleUseCase(saleClient)
    }

    @Provides
    @Singleton
    fun provideInventoryClient(apolloClient: ApolloClient): InventoryClient {
        return ApolloInventoryClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideInventoryUseCase(inventoryClient: InventoryClient): InventoryUseCase {
        return InventoryUseCase(inventoryClient)
    }

    @Provides
    @Singleton
    fun provideSupplierClient(apolloClient: ApolloClient): SupplierClient {
        return ApolloSupplierClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetSuppliersUseCase(supplierClient: SupplierClient): SupplierUseCase {
        return SupplierUseCase(supplierClient)
    }

    @Provides
    @Singleton
    fun provideSession(@ApplicationContext context: Context): Session {
        return Session(context)
    }
}