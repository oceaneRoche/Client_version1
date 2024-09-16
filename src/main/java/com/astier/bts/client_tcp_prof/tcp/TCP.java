/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astier.bts.client_tcp_prof.tcp;


import com.astier.bts.client_tcp_prof.HelloController;
import com.astier.bts.client_tcp_prof.clavier.In;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


import static javafx.scene.paint.Color.RED;

/**
 * @author Michael
 */
public class TCP extends Thread {
    int port;
    InetAddress serveur;
    Socket socket;
    boolean marche = false;
    boolean connection = false;
    PrintStream out;
    BufferedReader in;

    HelloController fxmlCont;

    public TCP() {
    }

    public TCP(InetAddress serveur, int port, HelloController fxmlCont) {
        this.port = port;
        this.serveur = serveur;
        this.fxmlCont = fxmlCont;
        System.out.println("@ serveur: " + serveur + " port: " + port);
    }

    public void connection() throws IOException {
        socket = new Socket(serveur, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
        marche = true;
        connection = true;
        System.out.println("    MESSAGE SERVEUR >  \n      " + in.readLine() + "\n");
        String laRequette = fxmlCont.TextFieldRequette.getText();
        requette(laRequette);
        new Thread(this::run).start();

    }

    public void deconnection() throws InterruptedException {
        if (socket != null && socket.isClosed()){
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            marche = false;
            connection = false;
            System.out.println("Deconnexion");
        }
    }

    public void requette(String laRequette) throws IOException {
        out.println(laRequette);  // envoi reseau
        System.out.println("la requette " + laRequette);
    }

    public void run() {
        while (marche) {
            try {
                if (in.ready()){
                    String message = in.readLine();
                    if (message != null) {
                        System.out.println("message serveur : " + message);
                        updateMessage(message);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /*
    Pour déclencher une opération graphique en dehors du thread graphique  utiliser
    javafx.application.Platform.runLater(java.lang.Runnable)
    Cette méthode permet d'éxécuter le code du runnable par le thread graphique de JavaFX.
    */
    protected void updateMessage(String message) {
        Platform.runLater(() -> fxmlCont.TextAreaReponses.appendText("    MESSAGE SERVEUR >  \n      " + message + "\n"));
    }
}