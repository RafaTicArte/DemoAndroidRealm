package com.ticarte.rafa.demoandroidrealm;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ListView listViewPersons;
    EditText editTextName;
    ArrayList<String> listNames;
    ArrayList<Person> listPersons;
    ArrayAdapter<String> adapterPersons;

    // La base de datos está inicializada en la clase MyApplication

    // Por no complicar el ejemplo, el adaptador creado será del tipo String,
    // así que al recuperar de la base de datos la lista de objetos
    // se hará una transformación a una lista de String
    // que será la que se cargue en el adaptador.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewPersons = (ListView)findViewById(R.id.listViewPersons);
        editTextName = (EditText) findViewById(R.id.editTextName);
        listPersons = new ArrayList<>();
        listNames = new ArrayList<>();

        // Inicialización del adaptador
        adapterPersons = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listNames);
        listViewPersons.setAdapter(adapterPersons);

        // Carga de datos en el adaptador
        loadDataAdapter();

        // Configuración del clic en los elementos del adaptador
        listViewPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ListView", listNames.get(position));

                Realm realm = Realm.getDefaultInstance();

                Person personRealm = realm.where(Person.class)
                        .equalTo("id", listNames.get(position).split(":")[0])
                        .findFirst();

                Person person = realm.copyFromRealm(personRealm);

                realm.close();

                editTextName.setText(person.getId() + ":" + person.getName());
            }
        });

        // Configuración del clic largo en los elementos del adaptador
        listViewPersons.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Eliminación un elemento
                Realm realm = Realm.getDefaultInstance();

                realm.beginTransaction();

                Person personRealm = realm.where(Person.class)
                        .equalTo("id", listNames.get(position).split(":")[0])
                        .findFirst();

                personRealm.deleteFromRealm();

                realm.commitTransaction();

                realm.close();

                editTextName.setText("");

                loadDataAdapter();

                return true;
            }
        });

        // Configuración del clic del botón guardar
        FloatingActionButton saveButton = findViewById(R.id.fab);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().toString().contains(":")) {
                    // Actualización de un elemento
                    Realm realm = Realm.getDefaultInstance();

                    Person personRealm = realm.where(Person.class)
                            .equalTo("id", editTextName.getText().toString().split(":")[0])
                            .findFirst();

                    Person person = realm.copyFromRealm(personRealm);
                    person.setName(editTextName.getText().toString().split(":")[1]);

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(person);
                    realm.commitTransaction();

                    realm.close();
                } else {
                    // Inserción de un elemento
                    Person person = new Person();
                    person.setId(UUID.randomUUID().toString());
                    person.setName(editTextName.getText().toString());

                    Realm realm = Realm.getDefaultInstance();

                    realm.beginTransaction();
                    realm.copyToRealm(person);
                    realm.commitTransaction();

                    realm.close();

                    editTextName.setText("");
                }

                loadDataAdapter();
            }
        });
    }

    private void loadDataAdapter() {
        // Recuperación de un elemento
        listPersons.clear();
        listNames.clear();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Person> result = realm.where(Person.class)
                .findAll();

        Log.d("Realm find items: ", ""+result.size());

        listPersons.addAll(realm.copyFromRealm(result));

        realm.close();

        for (Person person : listPersons) {
            listNames.add(person.toString());
        }

        adapterPersons.notifyDataSetChanged();
    }
}