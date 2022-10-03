package com.droidev.sepatagenda;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private ArrayList<String> banco;

    RecyclerView RecyclerView;
    RecyclerView.Adapter Adapter;

    public static Connection connection;
    private Boolean confirmar = false;

    private Miscs miscs;

    private TinyDB tinyDB;

    public void makeConnection() {

        new Thread(() -> {
            try {

                String dbHost = tinyDB.getString("dbHost");
                String dbPort = tinyDB.getString("dbPort");
                String dbName = tinyDB.getString("dbName");
                String dbUser = tinyDB.getString("dbUser");
                String dbPass = tinyDB.getString("dbPass");

                String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

                if (!(dbName.isEmpty() || tinyDB.getString("atendente").equals(""))) {

                    connection = DriverManager.getConnection(url, dbUser, dbPass);
                }

            } catch (SQLException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    public void carregar() {

        try {

            if (connection.isClosed() || connection == null) {

                makeConnection();

            } else {

                DBQueries db = new DBQueries();

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

    public void procurar() {

        EditText editText = new EditText(this);
        editText.setHint("Exemplo: urgente");
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setMaxLines(1);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(editText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Procurar")
                .setMessage("Digite uma caracteristica da tarefa a ser procurada.")
                .setPositiveButton("Procurar", null)
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Limpar", null)
                .setView(lay)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        positiveButton.setOnClickListener(v -> {

            try {

                if (connection.isClosed() || connection == null) {

                    makeConnection();

                } else {

                    DBQueries db = new DBQueries();

                    banco = new ArrayList<>();

                    RecyclerView = findViewById(R.id.visualizador_recycle);
                    RecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    Adapter = new RecyclerViewAdapter(MainActivity.this, banco, MainActivity.this);
                    RecyclerView.setAdapter(Adapter);

                    banco.addAll(db.pesquisar(MainActivity.this, connection, editText.getText().toString()));

                    dialog.dismiss();
                }
            } catch (Exception e) {

                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        neutralButton.setOnClickListener(v -> editText.setText(""));
    }

    public void mudarStatus(String id, String status, String atendente) {

        try {

            if (connection.isClosed() || connection == null) {

                makeConnection();

            } else {

                DBQueries db = new DBQueries();

                db.marcarResolvido(MainActivity.this, connection, id, status, atendente + " - " + miscs.dataHoje() + " - " + miscs.horaAgora());
            }
        } catch (Exception e) {

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
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

        positiveButton.setOnClickListener(v -> {

            if (atendente.getText().toString().isEmpty() || dbName.getText().toString().isEmpty() || dbUser.getText().toString().isEmpty() || dbPass.getText().toString().isEmpty() || dbHost.getText().toString().isEmpty() || dbPort.getText().toString().isEmpty()) {

                Toast.makeText(MainActivity.this, "É neccessário preencher todos os campos", Toast.LENGTH_SHORT).show();

            } else if (atendente.getText().toString().length() > 10) {

                Toast.makeText(MainActivity.this, "Nome do Atendente não pode ser maior que 10 caracteres.", Toast.LENGTH_SHORT).show();
            } else {

                tinyDB.remove("atendente");
                tinyDB.remove("dbName");
                tinyDB.remove("dbUser");
                tinyDB.remove("dbPass");
                tinyDB.remove("dbHost");
                tinyDB.remove("dbPort");

                tinyDB.putString("atendente", atendente.getText().toString().toUpperCase());
                tinyDB.putString("dbName", dbName.getText().toString());
                tinyDB.putString("dbUser", dbUser.getText().toString());
                tinyDB.putString("dbPass", dbPass.getText().toString());
                tinyDB.putString("dbHost", dbHost.getText().toString());
                tinyDB.putString("dbPort", dbPort.getText().toString());

                dialog.dismiss();

                Toast.makeText(MainActivity.this, "Salvo", Toast.LENGTH_SHORT).show();

                new Thread(() -> {

                    try {

                        if (!(connection == null)) {

                            connection.close();
                        }

                        makeConnection();
                    } catch (SQLException e) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });

        neutralButton.setOnClickListener(v -> {

            dbName.setText("");
            dbUser.setText("");
            dbPass.setText("");
            dbHost.setText("");
        });
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

            case R.id.compartilharLink:

                if (tinyDB.getString("dbName").isEmpty()) {

                    Toast.makeText(this, "Ainda não há nenhuma credencial salva.", Toast.LENGTH_SHORT).show();
                } else {

                    String link = "https://sepatagenda.db/"
                            + tinyDB.getString("dbName")
                            + "/" + tinyDB.getString("dbUser")
                            + "/" + tinyDB.getString("dbPass")
                            + "/" + tinyDB.getString("dbHost")
                            + "/" + tinyDB.getString("dbPort");

                    Intent shareLinkIntent = new Intent(Intent.ACTION_SEND);
                    shareLinkIntent.setType("text/plain");
                    shareLinkIntent.putExtra(Intent.EXTRA_TEXT, link);
                    startActivity(Intent.createChooser(shareLinkIntent, "Compartilhar link com..."));
                }

                break;

            case R.id.novaTarefa:

                Intent myIntent = new Intent(MainActivity.this, NovaTarefa.class);
                startActivity(myIntent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        RecyclerView = findViewById(R.id.visualizador_recycle);

        setTitle("SEPAT Agenda");

        miscs = new Miscs();

        tinyDB = new TinyDB(MainActivity.this);

        makeConnection();

        Uri uri = getIntent().getData();

        if (uri != null) {

            String path = uri.toString();

            deepLink(path.replace("https://sepatagenda.db/", ""));
        }
    }

    @Override
    public void onBackPressed() {
        if (confirmar) {
            finish();
        } else {
            Toast.makeText(this, "Pressione voltar de novo para sair",
                    Toast.LENGTH_SHORT).show();
            confirmar = true;
            new Handler().postDelayed(() -> confirmar = false, 3 * 1000);
        }
    }

    @Override
    public void onLongItemClick(int position) {

        String[] detalhesArray = banco.get(position).split("#@#");

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

        positiveButton.setOnClickListener(v -> {

            Intent myIntent = new Intent(MainActivity.this, DetalhesActivity.class);
            myIntent.putExtra("detalhes", detalhesArray[2] +
                    "\n\n" + detalhesArray[3] +
                    "\n\nDetalhes: \n\n" + detalhesArray[9].replace("Detalhes: ", ""));
            startActivity(myIntent);
        });

        neutralButton.setOnClickListener(v -> {

            if (!detalhesArray[6].equals("Status: RESOLVIDO")) {

                if (confirmar) {

                    mudarStatus(detalhesArray[0].replace("ID: ", ""), "RESOLVIDO", tinyDB.getString("atendente").toUpperCase());

                    carregar();

                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Pressione RESOLVIDO de novo para confirmar", Toast.LENGTH_SHORT).show();

                    confirmar = true;

                    new Handler().postDelayed(() -> confirmar = false, 3 * 1000);
                }
            } else {

                Toast.makeText(MainActivity.this, "O Status já está marcado como ''Resolvido''", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limparChaves() {

        tinyDB.remove("dbName");
        tinyDB.remove("dbUser");
        tinyDB.remove("dbPass");
        tinyDB.remove("dbHost");
        tinyDB.remove("dbPort");
    }

    public void deepLink(String link) {

        String[] linkArray = link.split("/");

        limparChaves();

        tinyDB.putString("dbName", linkArray[0]);
        tinyDB.putString("dbUser", linkArray[1]);
        tinyDB.putString("dbPass", linkArray[2]);
        tinyDB.putString("dbHost", linkArray[3]);
        tinyDB.putString("dbPort", linkArray[4]);

        login();
    }
}