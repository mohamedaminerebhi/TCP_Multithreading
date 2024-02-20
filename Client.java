package multi;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private String host;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connecté au serveur " + host + ":" + port);
    }

    public void sendString(String chaine) throws Exception {
        writer.println(chaine);
        System.out.println("Envoyé au serveur: " + chaine);
    }

    public String receiveString() throws Exception {
        String chaine = reader.readLine();
        System.out.println("Reçu du serveur: " + chaine);
        return chaine;
    }

    public void close() throws Exception {
        socket.close();
        System.out.println("Déconnecté du serveur " + host + ":" + port);
        
    }

    public static void main(String[] args) throws Exception {
        // Remplacer "localhost" et 6000 par l'adresse et le port du serveur
        Client client = new Client("localhost", 2);
        client.connect();
        String chaine;
        System.out.println("donner une chaine a inverser");
        Scanner s=new Scanner(System.in);
        chaine=s.nextLine();
        client.sendString(chaine);
        String chaineInversee = client.receiveString();
        client.close();

        System.out.println("Chaîne inversée : " + chaineInversee);
    }
}