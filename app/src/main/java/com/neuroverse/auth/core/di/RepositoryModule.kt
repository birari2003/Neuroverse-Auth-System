package com.neuroverse.auth.core.di

import com.neuroverse.auth.data.repository.FakeAuthRepository
import com.neuroverse.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds AuthRepository interface to its implementation.
 * To switch to Firebase or another backend in the future, only this binding needs to change.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        fakeAuthRepository: FakeAuthRepository
    ): AuthRepository
}
