package com.droidev.sepatagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class novaTarefa extends AppCompatActivity {

    EditText solicitante, data, hora, assunto, mensagem;
    Button dataEHora;
    RadioButton radioButton;
    RadioGroup radioGroup;
    DBQueries dbQueries;
    TinyDB tinyDB;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        dbQueries = new DBQueries();

        tinyDB = new TinyDB(novaTarefa.this);

        solicitante = findViewById(R.id.solicitante);
        data = findViewById(R.id.data);
        hora = findViewById(R.id.hora);
        assunto = findViewById(R.id.assunto);
        mensagem = findViewById(R.id.mensagem);

        dataEHora = findViewById(R.id.dataEHora);

        radioGroup = findViewById(R.id.radioGroup);

    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.adicionar:

                dbQueries.inserir(novaTarefa.this, MainActivity.connection, tinyDB.getString("atendente"), solicitante.getText().toString(), data.getText().toString(), hora.getText().toString(), grupo(), "AINDA EM ABERTO",
                        "NINGUEM", mensagem.getText().toString());

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nova_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private String grupo() {

        int selectedId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedId);

        return radioButton.getText().toString();

    }
}