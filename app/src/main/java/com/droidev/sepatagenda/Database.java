package com.droidev.sepatagenda;

import android.app.Activity;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    ArrayList<String> visualizador = new ArrayList<>();

    public ArrayList<String> carregar(Activity activity, Connection connection) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    Statement stmt;
                    String sql = "SELECT * FROM AGENDA ORDER BY ID DESC";

                    stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);

                    visualizador = new ArrayList<>();

                    while (rs.next()) {

                        String id = rs.getString("ID");
                        String atendente = rs.getString("ATENDENTE");
                        String solicitante = rs.getString("SOLICITANTE");
                        String assunto = rs.getString("ASSUNTO");
                        String data = rs.getString("DATA");
                        String hora = rs.getString("HORA");
                        String status = rs.getString("STATUS");
                        String mensagem = rs.getString("DETALHES");

                        visualizador.add("ID: " + id
                                + "\nAtendente: " + atendente
                                + "\nSolicitante: " + solicitante
                                + "\nAssunto: " + assunto
                                + "\nData: " + data
                                + "\nHora: " + hora
                                + "\nStatus: " + status
                                + "\nDetalhes: " + mensagem);
                    }

                } catch (Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return visualizador;
    }

    public void marcarResolvido(Activity activity, Connection connection, String id, String status) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String sql = "UPDATE AGENDA SET STATUS = '" + status + "' WHERE ID = '" + id + "'";

                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(sql);

                } catch (Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
