package com.droidcon.simplejokes.di

import com.droidcon.simplejokes.core.data.Vault
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val vaultModule = module {
    single<Vault> {
        Vault(androidApplication())
    }
}