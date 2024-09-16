package com.astier.bts.client_tcp_prof;

import com.astier.bts.client_tcp_prof.tcp.TCP;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import static javafx.scene.paint.Color.*;

public class HelloController implements Initializable {
    public Button button;
    public Button connecter;
    public Button deconnecter;
    public TextField TextFieldIP;
    public TextField TextFieldPort;
    public TextField TextFieldRequette;
    public Circle voyant;
    public TextArea TextAreaReponses;
    static public TCP tcp;
    static boolean enRun = false;
    String adresse, port;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button.setOnAction(e -> {
            try {
                envoyer();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        connecter.setOnAction(e -> {
            try {
                connecter();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        deconnecter.setOnAction(e -> {
            try {
                deconnecter();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        voyant.setFill(RED);
    }


    private void envoyer() throws InterruptedException {
        String requette = TextFieldRequette.getText();
        if (enRun && !requette.isEmpty()) {
            try {
                tcp.requette(requette);
                TextAreaReponses.appendText("Requete envoyer : " + requette + "\n");
            } catch (IOException e) {
                TextAreaReponses.appendText("Erreur d'envoi de requete" + e.getMessage());
            }
        } else {
            TextAreaReponses.appendText("Connexion non etablie ou requete vide");
        }
    }

    private void deconnecter() throws InterruptedException {
        if (enRun) {
            tcp.deconnection();
            enRun = false;
        }
    }

    private void connecter() throws IOException {
        adresse = TextFieldIP.getText();
        port = TextFieldPort.getText();
        tcp = new TCP(InetAddress.getByName(adresse), Integer.parseInt(port), this);
        voyant.setFill(GREEN);
        tcp.connection();
        enRun = true;
    }
}