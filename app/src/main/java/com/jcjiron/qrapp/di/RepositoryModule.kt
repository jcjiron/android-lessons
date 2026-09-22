package com.jcjiron.qrapp.di

import com.jcjiron.qrapp.data.repository.QrRepositoryImpl
import com.jcjiron.qrapp.domain.repository.QrRepository
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
    abstract fun bindQrRepository(impl: QrRepositoryImpl): QrRepository
}
