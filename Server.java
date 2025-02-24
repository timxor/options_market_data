// file: Server.java
// compile: javac Server.java
// run: java Server
// Description: This is a simple server program that listens for incoming connections on a specified port.
//

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        String ipAddress = "127.0.0.1";
        int portNumber = 5000;
        int maxConnections = 50;

        System.out.println("\n\n Now starting Server.java on: " + ipAddress + ":" + portNumber + " ...\n");

        try {
            InetAddress localhost = InetAddress.getByName(ipAddress);
            ServerSocket serverSocket = new ServerSocket(portNumber, maxConnections, localhost);
            System.out.println("\nServer started at " + ipAddress + " on port " + portNumber + " \n");
            System.out.println("\nWaiting for client connections...");


//            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("\nLocalhost Name: " + localhost.getHostName()+"\n");
            System.out.println("\nLocalhost IP :" + localhost.getHostAddress()+"\n");

            System.out.println("\nType 'exit' to stop server. \n");

            // Start a separate thread to listen for shutdown command
            Thread shutdownThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String command = scanner.nextLine();
                    if (command.equalsIgnoreCase("exit")) {
                        try {
                            serverSocket.close();
                            System.out.println("\nExiting Server.java...\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                }
            });

            shutdownThread.start(); // Start the shutdown listener

            // Accept client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nClient connected: " + clientSocket.getInetAddress() + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}