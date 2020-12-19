package com.ticarte.rafa.demoandroidrealm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Person extends RealmObject {

    // Debe existir una clave primaria para poder actualizar objetos
    @PrimaryKey
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return getId() + ":" + getName();
    }
}
