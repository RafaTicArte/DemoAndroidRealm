package com.ticarte.rafa.demoandroidrealm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
        Realm.init(applicationContext);

        // Aplicar configuración específica a la base de datos
        // Por ejemplo, cambiar el nombre de la base de datos y la ruta de almacenamiento
        // para poder acceder a la base de datos en un móvil sin rootear
        /*
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .name("demoandroid.realm")
                .directory(getExternalFilesDirs(null)[1])
                .build();
        Realm.setDefaultConfiguration(config);
        */

        // Comprobar la ruta de almacenamiento de la base de datos
        // La ruta por defecto es /data/data/[app]/files
        /*
        Realm realm = Realm.getDefaultInstance();
        Log.d("DemoAndroidRealm", "Path: " + realm.getPath());
        realm.close();
        */
    }

    public static Context getContext() {
        return applicationContext;
    }
}