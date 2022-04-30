package com.droidev.sepatagenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private ArrayList<String> banco;

    RecyclerView RecyclerView;
    RecyclerView.Adapter Adapter;

    private Connection connection;

    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        RecyclerView = findViewById(R.id.visualizador_recycle);

        setTitle("SEPAT Agenda");

        tinyDB = new TinyDB(MainActivity.this);

        makeConnection();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:

                procurar();

                break;

            case R.id.refresh:

                carregar();

                break;

            case R.id.login:

                login();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void makeConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String dbHost = tinyDB.getString("dbHost");
                    String dbPort = tinyDB.getString("dbPort");
                    String dbName = tinyDB.getString("dbName");
                    String dbUser = tinyDB.getString("dbUser");
                    String dbPass = tinyDB.getString("dbPass");

                    String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

                    if (!dbName.isEmpty()) {

                        connection = DriverManager.getConnection(url, dbUser, dbPass);
                    }

                } catch (SQLException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onLongItemClick(int position) {

        String[] detalhesArray = banco.get(position).split("\n");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(detalhesArray[0])
                .setMessage(detalhesArray[6] + "\n\nEscolha as opções abaixo:")
                .setPositiveButton("Detalhes", null)
                .setNegativeButton("Fechar", null)
                .setNeutralButton("Resolvido", null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, Detalhes.class);
                myIntent.putExtra("detalhes", detalhesArray[7].replace("Detalhes: ", ""));
                startActivity(myIntent);

                dialog.dismiss();
            }
        });

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!detalhesArray[6].equals("Status: RESOLVIDO")) {

                    mudarStatus(detalhesArray[0].replace("ID: ", ""), "RESOLVIDO");

                    carregar();

                    dialog.dismiss();
                } else {

                    Toast.makeText(MainActivity.this, "O Status já está marcado como ''Resolvido''", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void carregar() {

        try {

            if (connection.isClosed() || connection == null) {

                makeConnection();

            } else {

                Database db = new Database();

                banco = new ArrayList<>();

                RecyclerView = findViewById(R.id.visualizador_recycle);
                RecyclerView.setLayoutManager(new LinearLayoutManager(this));
                Adapter = new RecyclerViewAdapter(this, banco, this);
                RecyclerView.setAdapter(Adapter);

                banco.addAll(db.carregar(MainActivity.this, connection));
            }
        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void procurar(){

    }

    public void mudarStatus(String id, String status) {

        try {

            if (connection.isClosed() || connection == null) {

                makeConnection();

            } else {

                Database db = new Database();

                db.marcarResolvido(MainActivity.this, connection, id, status);
            }
        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void login() {

        EditText atendente = new EditText(this);
        atendente.setHint("Atendente");
        atendente.setInputType(InputType.TYPE_CLASS_TEXT);
        atendente.setMaxLines(1);

        EditText dbName = new EditText(this);
        dbName.setHint("dbName");
        dbName.setInputType(InputType.TYPE_CLASS_TEXT);
        dbName.setMaxLines(1);

        EditText dbUser = new EditText(this);
        dbUser.setHint("dbUser");
        dbUser.setInputType(InputType.TYPE_CLASS_TEXT);
        dbUser.setMaxLines(1);

        EditText dbPass = new EditText(this);
        dbPass.setHint("dbPass");
        dbPass.setInputType(InputType.TYPE_CLASS_TEXT);
        dbPass.setMaxLines(1);

        EditText dbHost = new EditText(this);
        dbHost.setHint("dbHost");
        dbHost.setInputType(InputType.TYPE_CLASS_TEXT);
        dbHost.setMaxLines(1);

        EditText dbPort = new EditText(this);
        dbPort.setHint("dbPort");
        dbPort.setInputType(InputType.TYPE_CLASS_NUMBER);
        dbPort.setMaxLines(1);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(atendente);
        lay.addView(dbName);
        lay.addView(dbUser);
        lay.addView(dbPass);
        lay.addView(dbHost);
        lay.addView(dbPort);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Login")
                .setMessage("Insira as credencias do Banco de Dados.")
                .setPositiveButton("Salvar", null)
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Limpar Tudo", null)
                .setView(lay)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        atendente.setText(tinyDB.getString("atendente"));
        dbName.setText(tinyDB.getString("dbName"));
        dbUser.setText(tinyDB.getString("dbUser"));
        dbPass.setText(tinyDB.getString("dbPass"));
        dbHost.setText(tinyDB.getString("dbHost"));
        dbPort.setText(tinyDB.getString("dbPort"));

        if (dbPort.getText().toString().isEmpty()) {

            dbPort.setText("5432");
        }

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (atendente.getText().toString().isEmpty() || dbName.getText().toString().isEmpty() || dbUser.getText().toString().isEmpty() || dbPass.getText().toString().isEmpty() || dbHost.getText().toString().isEmpty() || dbPort.getText().toString().isEmpty()) {

                    Toast.makeText(MainActivity.this, "É neccessário prencher todos os campos", Toast.LENGTH_SHORT).show();
                } else {

                    tinyDB.putString("atendente", atendente.getText().toString());
                    tinyDB.putString("dbName", dbName.getText().toString());
                    tinyDB.putString("dbUser", dbUser.getText().toString());
                    tinyDB.putString("dbPass", dbPass.getText().toString());
                    tinyDB.putString("dbHost", dbHost.getText().toString());
                    tinyDB.putString("dbPort", dbPort.getText().toString());

                    dialog.dismiss();
                }
            }
        });

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbName.setText("");
                dbUser.setText("");
                dbPass.setText("");
                dbHost.setText("");
            }
        });
    }
}