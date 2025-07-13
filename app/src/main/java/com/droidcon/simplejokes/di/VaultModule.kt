package com.droidcon.simplejokes.di

import android.content.Context
import com.droidcon.simplejokes.core.data.Vault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VaultModule {

    @Provides
    @Singleton
    fun provideVault(@ApplicationContext context: Context): Vault {
        return Vault(context)
    }


}