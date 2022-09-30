package com.droidev.sepatagenda;

import android.app.Activity;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBQueries {

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

                PreparedStatement pst;

                String sql = "SELECT * FROM AGENDA WHERE " +
                        "ATENDENTE ILIKE ? " +
                        "OR SOLICITANTE ILIKE ? " +
                        "OR ASSUNTO ILIKE ? " +
                        "OR DATA ILIKE ? " +
                        "OR HORA ILIKE ? " +
                        "OR STATUS ILIKE ? " +
                        "OR CONCLUIDO ILIKE ? " +
                        "OR REABERTO ILIKE ? " +
                        "OR DETALHES ILIKE ? " +
                        "ORDER BY ID DESC";

                pst = connection.prepareStatement(sql);
                pst.setString(1, "%" + string + "%");
                pst.setString(2, "%" + string + "%");
                pst.setString(3, "%" + string + "%");
                pst.setString(4, "%" + string + "%");
                pst.setString(5, "%" + string + "%");
                pst.setString(6, "%" + string + "%");
                pst.setString(7, "%" + string + "%");
                pst.setString(8, "%" + string + "%");
                pst.setString(9, "%" + string + "%");

                ResultSet rs = pst.executeQuery();

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

                PreparedStatement pst;

                String sql = "UPDATE AGENDA SET STATUS = ?, CONCLUIDO = ? WHERE ID = ?";

                pst = connection.prepareStatement(sql);

                pst.setString(1, status);
                pst.setString(2, atendente);
                pst.setInt(3, Integer.parseInt(id));

                pst.executeUpdate();

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

    public void inserir(Activity activity, Connection connection, String atendente, String solicitante, String assunto, String data, String hora, String status, String concluido, String reaberto, String detalhes) {
        Thread thread = new Thread(() -> {

            try {

                PreparedStatement pst;

                String sql = "INSERT INTO AGENDA (ATENDENTE, SOLICITANTE, ASSUNTO, DATA, HORA, STATUS, CONCLUIDO, REABERTO, DETALHES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pst = connection.prepareStatement(sql);

                pst.setString(1, atendente.toUpperCase());
                pst.setString(2, solicitante.toUpperCase());
                pst.setString(3, assunto.toUpperCase());
                pst.setString(4, data.toUpperCase());
                pst.setString(5, hora.toUpperCase());
                pst.setString(6, status.toUpperCase());
                pst.setString(7, concluido.toUpperCase());
                pst.setString(8, reaberto.toUpperCase());
                pst.setString(9, detalhes.toUpperCase());

                pst.executeUpdate();

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
