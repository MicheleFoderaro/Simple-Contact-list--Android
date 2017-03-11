package com.example.michele.rubrica.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.michele.rubrica.R;
import com.example.michele.rubrica.adapters.ContattoAdapter;
import com.example.michele.rubrica.database.DatabaseHandler;
import com.example.michele.rubrica.models.Contatto;

/**
 * Created by Michele on 10/03/2017.
 */

public class MainActivity extends AppCompatActivity  {

    private static final int SPECIAL = 1;
    private static final int NOTSPECIAL = 0;
    private final String KEY_MODE = "mode";
    RecyclerView contattoRV;
    Contatto contattoEditing = new Contatto();
    ContattoAdapter adapter;
    DatabaseHandler dbHandler;

    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    int mode = 0;
    RecyclerView.LayoutManager layoutManager = linearLayoutManager;


    public static final String NOME_CONTATTO = "nome";
    public static final String CELLULARE_CONTATTO = "cellulare";
    public static final String SPECIALE_CONTATTO = "speciale";

    public static final String ACTION_MODE = "ACTION_MODE";
    public static final int REQUEST_EDIT = 1002;
    public static final int EDIT_MODE = 1;
    public static final int NEW_CONTATTO_REQUEST = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Element for recyclerView
        adapter = new ContattoAdapter(this);
        contattoRV = (RecyclerView) findViewById(R.id.contatto_rv);
        contattoRV.setAdapter(adapter);
        contattoRV.setLayoutManager(layoutManager);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        mode = sharedPref.getInt(KEY_MODE, 0);
        if (mode==0) {
            contattoRV.setLayoutManager(linearLayoutManager);
        } else {
            contattoRV.setLayoutManager(gridLayoutManager);
        }
        findViewById(R.id.activity_main_add_contatto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ContattoDetailsActivity.class);
                startActivityForResult(intent, NEW_CONTATTO_REQUEST);
            }
        });
        //DB SQLite
        registerForContextMenu(contattoRV);
        dbHandler = new DatabaseHandler(this);
        adapter.setDataSet(dbHandler.getAllContatti());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!= Activity.RESULT_CANCELED) {

            String nome, cellulare, speciale;
            nome = data.getStringExtra(NOME_CONTATTO);
            cellulare = data.getStringExtra(CELLULARE_CONTATTO);
            speciale = data.getStringExtra(SPECIALE_CONTATTO); //integer
            //Controllo chi ha avviato l'intent
            if (requestCode == NEW_CONTATTO_REQUEST && resultCode == Activity.RESULT_OK) {
                //Controllo l'integrita' dei dati
                    Contatto contatto = new Contatto(nome, cellulare);
                    dbHandler.addNote(contatto);
                    adapter.addDataSet(contatto);
                    contattoRV.scrollToPosition(0);
            } else if (requestCode == REQUEST_EDIT && resultCode == Activity.RESULT_OK) {
                    contattoEditing.setNome(nome);
                    contattoEditing.setNumero(cellulare);
                    dbHandler.updateNote(contattoEditing);
                    adapter.editDataSet(contattoEditing, adapter.getPosition());
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Gestisco la scelta dell'utente
        int id = item.getItemId();
        switch (id){
            case R.id.action_delete:
                dbHandler.deleteNote(adapter.getDataSet(adapter.getPosition()));
                adapter.removeDataSet(adapter.getPosition());
                break;
            case R.id.action_edit:
                contattoEditing = adapter.getDataSet(adapter.getPosition());
                Intent i = new Intent(this, ContattoDetailsActivity.class);
                i.putExtra(ACTION_MODE, EDIT_MODE);
                i.putExtra(NOME_CONTATTO, contattoEditing.getNome());
                i.putExtra(CELLULARE_CONTATTO, contattoEditing.getNumero());
                startActivityForResult(i, REQUEST_EDIT);
                break;
            case R.id.action_markspecial:
                contattoEditing = adapter.getDataSet(adapter.getPosition());
                if(contattoEditing.getSpeciale()==NOTSPECIAL) {
                    contattoEditing.setSpeciale(SPECIAL);
                } else {
                    contattoEditing.setSpeciale(NOTSPECIAL);
                }
                dbHandler.updateNote(contattoEditing);
                adapter.editDataSet(contattoEditing, adapter.getPosition());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id) {
            case R.id.action_switch:
                if (mode==0) {
                    contattoRV.setLayoutManager(gridLayoutManager);
                    mode=1;
                } else {
                    contattoRV.setLayoutManager(linearLayoutManager);
                    mode=0;
                }
                break;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_MODE, mode);
        editor.commit();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}