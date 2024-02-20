package multi;

import java.io.*;
import java.net.*;

public class multi_thread_server {

    private static final int PORT = 2;
    private static final int MAX_CLIENTS = 10;
    private static int clientCount = 0;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serveur TCP en écoute sur le port " + PORT);

        while (true) {
            if (clientCount < MAX_CLIENTS) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion reçue de " + clientSocket.getRemoteSocketAddress());

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
                clientCount++;
                System.out.print(clientCount);
            } else {
                System.out.println("Nombre maximum de clients atteint. Attente pour de nouvelles connexions...");
                Thread.sleep(100000); //attendre pour verifier de nouveau
            }
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            ) {
                String chaine = reader.readLine();
                System.out.println("Reçu de " + clientSocket.getRemoteSocketAddress() + ": " + chaine);

                // Simulation d'un traitement long (sleep)
                Thread.sleep(1000);

                String chaineInversee = inverserChaine(chaine);
                writer.println(chaineInversee);
                System.out.println("Envoyé à " + clientSocket.getRemoteSocketAddress() + ": " + chaineInversee);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    synchronized (multi_thread_server.class) {
                        clientCount--;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String inverserChaine(String chaine) {
        StringBuilder sb = new StringBuilder(chaine);
        return sb.reverse().toString();
    }
}

