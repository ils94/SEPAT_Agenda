package com.droidev.sepatagenda;

import android.app.Activity;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class dbQueries {

    ArrayList<String> visualizador = new ArrayList<>();

    public ArrayList<String> carregar(Activity activity, Connection connection) {
        Thread thread = new Thread(() -> {

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
                    String concluido = rs.getString("CONCLUIDO");
                    String reaberto = rs.getString("REABERTO");
                    String mensagem = rs.getString("DETALHES");

                    visualizador.add("ID: " + id
                            + "#@#Atendente: " + atendente
                            + "#@#Solicitante: " + solicitante
                            + "#@#Assunto: " + assunto
                            + "#@#Data: " + data
                            + "#@#Hora: " + hora
                            + "#@#Status: " + status
                            + "#@#Concluído por: " + concluido
                            + "#@#Reaberto por: " + reaberto
                            + "#@#Detalhes: " + mensagem);
                }

            } catch (Exception e) {
                activity.runOnUiThread(() -> Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            activity.runOnUiThread(() -> Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show());
        }

        return visualizador;
    }

    public ArrayList<String> pesquisar(Activity activity, Connection connection, String string) {
        Thread thread = new Thread(() -> {

            try {

                Statement stmt;
                String sql = "SELECT * FROM AGENDA WHERE " +
                        "ATENDENTE ILIKE '%" + string + "%' " +
                        "OR SOLICITANTE ILIKE '%" + string + "%' " +
                        "OR ASSUNTO ILIKE '%" + string + "%' " +
                        "OR DATA ILIKE '%" + string + "%' " +
                        "OR HORA ILIKE '%" + string + "%' " +
                        "OR STATUS ILIKE '%" + string + "%' " +
                        "OR CONCLUIDO ILIKE '%" + string + "%' " +
                        "OR DETALHES ILIKE '%" + string + "%' " +
                        "ORDER BY ID DESC";

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
                    String concluido = rs.getString("CONCLUIDO");
                    String reaberto = rs.getString("REABERTO");
                    String mensagem = rs.getString("DETALHES");

                    visualizador.add("ID: " + id
                            + "#@#Atendente: " + atendente
                            + "#@#Solicitante: " + solicitante
                            + "#@#Assunto: " + assunto
                            + "#@#Data: " + data
                            + "#@#Hora: " + hora
                            + "#@#Status: " + status
                            + "#@#Concluído por: " + concluido
                            + "#@#Reaberto por: " + reaberto
                            + "#@#Detalhes: " + mensagem);
                }

            } catch (Exception e) {
                activity.runOnUiThread(() -> Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            activity.runOnUiThread(() -> Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show());
        }

        return visualizador;
    }

    public void marcarResolvido(Activity activity, Connection connection, String id, String status, String atendente) {

        Thread thread = new Thread(() -> {

            try {

                String sql = "UPDATE AGENDA SET STATUS = '" + status + "', CONCLUIDO = '" + atendente + "' WHERE ID = '" + id + "'";

                Statement stmt = connection.createStatement();
                stmt.executeUpdate(sql);

            } catch (Exception e) {
                activity.runOnUiThread(() -> Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            activity.runOnUiThread(() -> Toast.makeText(activity.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show());
        }
    }
}
