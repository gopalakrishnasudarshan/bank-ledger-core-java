package com.bankledger.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BankClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException {

        String[] readRequests = {
                "READ,0,0,10", // shard 0 from offset 0
                "READ,1,0,10", // shard 1 from offset 0
                "READ,2,0,10"  // shard 2 from offset 0
        };

        for (int i = 0; i < readRequests.length; i++) {
            final int clientId = i + 1;
            final String request = readRequests[i];
            new Thread(() -> sendRead(clientId, request)).start();
        }



//        String [] requests = {
//                "ACC01,DEPOSIT,50",
//                "ACC02,DEPOSIT,20",
//                "ACC01,WITHDRAW,10",
//                "ACC03,DEPOSIT,30",
//                "ACC04,DEPOSIT,40",
//                "ACC05,DEPOSIT,60"};
//        for(int i = 0; i < requests.length; i++){
//            final int clientID = i + 1;
//            final String request = requests[i];
//
//            new Thread(() -> sendRequest(clientID,request)).start();
//        }

    }

    private static void sendRead(int clientId, String request) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(request);

            System.out.println("Client " + clientId + " sent: " + request);

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Client " + clientId + " got : " + line);
                if ("END".equals(line)) break;
            }

        } catch (IOException e) {
            System.err.println("Client " + clientId + " error: " + e.getMessage());
        }
    }

}


//    public static void sendRequest(int clientId, String request) {
//        try(Socket socket = new Socket(HOST,PORT);
//        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
//            out.println(request);
//
//            String response = in.readLine();
//            System.out.println("Client : "+ clientId + " sent : "+ request);
//            System.out.println("Client : "+ clientId +" got : " + response);
//
//        }catch (IOException e){
//            System.out.println("Client " + clientId + " error: " + e.getMessage());
//        }
//
//    }


