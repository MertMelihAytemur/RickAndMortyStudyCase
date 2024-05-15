package com.vmlmedia.rickandmortystudycase.feature.home.data.di

import com.vmlmedia.rickandmortystudycase.feature.home.data.HomeRepositoryImpl
import com.vmlmedia.rickandmortystudycase.feature.home.data.remote.HomeApi
import com.vmlmedia.rickandmortystudycase.feature.home.domain.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class HomeModule {

    @Provides
    fun provideHomeApiService(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    fun provideHomeRepository(homeApi: HomeApi): HomeRepository {
        return HomeRepositoryImpl(homeApi)
    }
}