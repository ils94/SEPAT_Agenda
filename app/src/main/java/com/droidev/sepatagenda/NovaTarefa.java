package com.droidev.sepatagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    Miscs miscs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        setTitle("Nova Tarefa");

        miscs = new Miscs();

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

                if (solicitante.getText().toString().equals("")
                        || dataEditText.getText().toString().equals("")
                        || horaEditText.toString().equals("")
                        || mensagem.getText().toString().equals("")) {

                    Toast.makeText(NovaTarefa.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();

                } else {
                    if (MainActivity.connection == null) {

                        Toast.makeText(NovaTarefa.this, "Não há nenhuma conexão com o banco, tente novamente.", Toast.LENGTH_SHORT).show();

                    } else {

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
                        mensagem.setText("");
                    }
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

        radioButton = findViewById(selectedId);

        return radioButton.getText().toString();

    }

    public void dataHora() {

        dataEditText.setText(miscs.dataHoje());

        horaEditText.setText(miscs.horaAgora());
    }
}