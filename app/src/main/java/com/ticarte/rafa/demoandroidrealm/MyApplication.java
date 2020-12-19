package com.ticarte.rafa.demoandroidrealm;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/*
 * Recuerda referenciar esta clase en AndroidManifest.xml
 */
public class MyApplication extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        // Inicializar la base de datos sólo una vez por aplicación
        Realm.init(this);
    }

    public static Context getContext() {
        return applicationContext;
    }
}