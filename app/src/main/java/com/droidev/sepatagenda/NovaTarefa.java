package com.droidev.sepatagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NovaTarefa extends AppCompatActivity {

    EditText solicitante, dataEditText, horaEditText, assunto, mensagem;
    RadioButton radioButton;
    RadioGroup radioGroup;
    DBQueries dbQueries;
    TinyDB tinyDB;
    String data, hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        setTitle("Nova Tarefa");

        dbQueries = new DBQueries();

        tinyDB = new TinyDB(NovaTarefa.this);

        solicitante = findViewById(R.id.solicitante);
        dataEditText = findViewById(R.id.data);
        horaEditText = findViewById(R.id.hora);
        assunto = findViewById(R.id.assunto);
        mensagem = findViewById(R.id.mensagem);

        radioGroup = findViewById(R.id.radioGroup);

        dataHora();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.adicionar:

                if (!(solicitante.getText().toString().equals("")
                        || dataEditText.getText().toString().equals("")
                        || horaEditText.toString().equals("")
                        || mensagem.getText().toString().equals(""))) {

                    dbQueries.inserir(NovaTarefa.this,
                            MainActivity.connection,
                            tinyDB.getString("atendente"),
                            solicitante.getText().toString(),
                            assunto.getText().toString(),
                            dataEditText.getText().toString(),
                            horaEditText.getText().toString(),
                            grupo(),
                            "AINDA EM ABERTO",
                            "NINGUEM",
                            mensagem.getText().toString());

                    Toast.makeText(NovaTarefa.this, "Tarefa adicionada.", Toast.LENGTH_SHORT).show();
                    
                    assunto.setText("");
                    dataEditText.setText("");
                    horaEditText.setText("");
                    mensagem.setText("");

                } else {

                    Toast.makeText(NovaTarefa.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.dataHora:

                dataHora();

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

    public void dataHora() {

        data = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        data = data.replace("/01/", "/jan/")
                .replace("/02/", "/fev/")
                .replace("/03/", "/mar/")
                .replace("/04/", "/abr/")
                .replace("/05/", "/mai/")
                .replace("/06/", "/jun/")
                .replace("/07/", "/jul/")
                .replace("/08/", "/ago/")
                .replace("/09/", "/set/")
                .replace("/10/", "/out/")
                .replace("/11/", "/nov/")
                .replace("/12/", "/dez/")
                .toUpperCase();

        hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        dataEditText.setText(data);
        horaEditText.setText(hora);
    }
}