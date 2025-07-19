package com.droidcon.simplejokes

import android.app.Application
import com.droidcon.simplejokes.di.databaseModule
import com.droidcon.simplejokes.di.jokesRepositoryModule
import com.droidcon.simplejokes.di.localizationModule
import com.droidcon.simplejokes.di.networkModule
import com.droidcon.simplejokes.di.preferencesModule
import com.droidcon.simplejokes.di.snackbarModule
import com.droidcon.simplejokes.di.vaultModule
import com.droidcon.simplejokes.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                jokesRepositoryModule,
                localizationModule,
                snackbarModule,
                vaultModule,
                preferencesModule,
                networkModule,
                viewModelsModule
            )
        }
    }

}