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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // La base de datos está inicializada en la clase MyApplication

        listViewPersons = (ListView)findViewById(R.id.listViewPersons);
        editTextName = (EditText) findViewById(R.id.editTextName);

        listNames = new ArrayList<>();

        adapterPersons = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listNames);
        listViewPersons.setAdapter(adapterPersons);

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

        listViewPersons.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ListView", listNames.get(position));

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

        loadDataAdapter();

        FloatingActionButton saveButton = findViewById(R.id.fab);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().toString().contains(":")) {
                    // Update item
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
                    // Insert item
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
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Person> result = realm.where(Person.class)
                .findAll();

        Log.d("Realm items: ", ""+result.size());

        listPersons = new ArrayList<>();
        listPersons.addAll(realm.copyFromRealm(result));

        realm.close();

        listNames.clear();

        for (Person person : listPersons) {
            listNames.add(person.getId() + ":" + person.getName());
        }

        adapterPersons.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}