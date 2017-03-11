package com.example.michele.rubrica.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michele.rubrica.R;

/**
 * Created by Michele on 10/03/2017.
 */

public class ContattoDetailsActivity extends AppCompatActivity {

    EditText nomeEt, cellulareEt;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatto_details);
        //Setto la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //Campi che utlizzo in questa activity
        nomeEt = (EditText) findViewById(R.id.activity_contatto_details_nome);
        cellulareEt = (EditText) findViewById(R.id.activity_contatto_details_cellulare);
        //Controllo se l'intent che ha avviato quella modifica ha come chiave EDIT_MODE
        intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra(MainActivity.ACTION_MODE, 0) == MainActivity.EDIT_MODE) {
                //Setto i dati che vorrò visualizzare
                nomeEt.setText(intent.getStringExtra(MainActivity.NOME_CONTATTO));
                cellulareEt.setText(intent.getStringExtra(MainActivity.CELLULARE_CONTATTO));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    //Menu e switch di entrata in base all'item
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm) {
            if (nomeEt.getText().toString().isEmpty() || cellulareEt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Inserisci tutti i campi", Toast.LENGTH_SHORT).show();
            } else {
                confirmNote();
                return true;
            }
        }
        if(id == android.R.id.home){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contatto_details_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Metodo che restituisce i dati
    private void confirmNote() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.NOME_CONTATTO, nomeEt.getText().toString());
        returnIntent.putExtra(MainActivity.CELLULARE_CONTATTO, cellulareEt.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
