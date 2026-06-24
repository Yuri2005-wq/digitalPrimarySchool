package com.digitalprimaryschool.digitalprimaryschool.service;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectiviteService {

    public static boolean estConnecte() {
        try (Socket socket = new Socket()) {
            // On essaie de joindre un serveur fiable (ici Google DNS)
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}