package com.neuroverse.auth.core.di

import com.neuroverse.auth.data.repository.FirebaseAuthRepository
import com.neuroverse.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the AuthRepository interface to its implementation.
 * Swapping backends (e.g. FakeAuthRepository for local demos) only needs this one binding changed.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): AuthRepository
}
