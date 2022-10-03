package com.droidev.sepatagenda;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Miscs {

    public String dataHoje() {

        String data;

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

        return data;
    }

    public String horaAgora() {

        String hora;

        hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        return hora;
    }
}
