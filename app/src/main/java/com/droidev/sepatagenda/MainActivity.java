package com.droidev.sepatagenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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

    private final String host = "";
    private final String database = "";
    private final String user = "";
    private final String pass = "";
    private final int port = 0;

    private String url = "jdbc:postgresql://%s:%d/%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        RecyclerView = findViewById(R.id.visualizador_recycle);

        setTitle("SEPAT Agenda");

        makeConnection();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.refresh:

                carregar();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public MainActivity() {
        this.url = String.format(this.url, this.host, this.port, this.database);
    }

    public void makeConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    connection = DriverManager.getConnection(url, user, pass);

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

        String detalhes = detalhesArray[0] +
                "\n" + detalhesArray[1] +
                "\n" + detalhesArray[2] +
                "\n" + detalhesArray[3] +
                "\n" + detalhesArray[4] +
                "\n" + detalhesArray[5] +
                "\n" + detalhesArray[6] +
                "\n\nDetalhes:\n\n" + detalhesArray[7].replace("Detalhes: ", "");

        Intent myIntent = new Intent(MainActivity.this, Detalhes.class);
        myIntent.putExtra("detalhes", detalhes);
        startActivity(myIntent);

        //ystem.out.println(banco.get(position));
    }

    public void carregar() {

        try {

            if (connection.isClosed()) {

                makeConnection();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        carregar();
                    }
                }, 3000);
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
}