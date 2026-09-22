package com.jcjiron.qrapp.di

import com.jcjiron.qrapp.data.repository.QrImageRepositoryImpl
import com.jcjiron.qrapp.domain.repository.QrImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQrImageRepository(impl: QrImageRepositoryImpl): QrImageRepository
}
