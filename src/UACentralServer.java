import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class UACentralServer {
    public static ArrayList<Socket> clientList = new ArrayList<>();
    public static ArrayList<Socket> serverList = new ArrayList<>();

    public static void main(String [] args) {
        ServerSocket servSock = null;
        try {
            servSock = new ServerSocket(32005);
            System.out.println("Server started, waiting for connections...");

            while (true) {
                Socket client = servSock.accept();
                System.out.println("New connection: " + client.getInetAddress().getHostAddress());

                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final String clientIP;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.clientIP = clientSocket.getInetAddress().getHostAddress();
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String line;

                while(true) {
                    Thread.sleep(500);
                    if(in.ready()) {
                        line = in.readLine();
                        System.out.println("From Connection (" + clientIP + "): " + line);

                        if(line.contains("INITIALIZE")) {
                            String str = line.replaceAll("[^0-9]", "");
                            if(str.length() > 0 && Integer.parseInt(str) > 0 && !serverList.isEmpty()) {
                                initialize(Integer.parseInt(str));
                            }
                            out.println("INIT_SUCCESS");
                        }

                        else if(line.contains("SERVER")) {
                            serverList.add(clientSocket);
                            System.out.println("Server added. Total servers: " + serverList.size());
                        }

                        else if(line.contains("CLIENT")) {
                            clientList.add(clientSocket);
                            System.out.println("Client added. Total clients: " + clientList.size());
                        }

                        else if(line.contains("FR_REQ")) {
                            //roomSecured indicates both waiting room and fitting room
                            boolean roomSecured = false;

                            //Need to change to a better identifier
                            String clientId = clientIP;

                            for (Socket servSock : serverList) {
                                try {
                                    PrintWriter servOut = new PrintWriter(servSock.getOutputStream(), true);
                                    BufferedReader servIn = new BufferedReader(new InputStreamReader(servSock.getInputStream()));

                                    servOut.println("FR_REQ " + clientId);
                                    String response = servIn.readLine();

                                    if (response != null) {

                                        //Change fitting room server to message central server "ALLOC {Room_id}"
                                        if (response.contains("ALLOC")) {
                                            String roomId = response.split(" ")[0];

                                            System.out.println(roomId + " is allocated.");

                                            roomSecured = true;
                                            out.println(roomId);
                                            break;
                                        }
                                        else if (response.contains("WAIT")) {
                                            roomSecured = true;
                                            out.println("Waiting for room...");

                                            //Maybe server should wait here until a fitting room is available?
                                            break;
                                        }
                                        else if (response.equals("NO")) {
                                            continue;
                                        }
                                    }
                                } catch (IOException e) {
                                    System.out.println("Error communicating with a Fitting Room Server: " + e.getMessage());
                                }
                            }

                            if (!roomSecured) {
                                System.out.println("There’s no rooms available");
                                out.println("NO");
                            }
                        }

                        //From Client
                        else if (line.contains("FR_EXIT")) {
                        }

                        //From Fitting Room Server... if received, should have client id
                        else if (line.contains("WR_EXIT")) {
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Connection dropped: " + clientIP);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                serverList.remove(clientSocket);
                clientList.remove(clientSocket);
            }
        }

        public void initialize(int nums) {
            int roomsPerServ = nums / serverList.size();
            for(Socket servSock : serverList) {
                try {
                    PrintWriter servOut = new PrintWriter(servSock.getOutputStream(), true);
                    servOut.println("INITIALIZE " + roomsPerServ);
                } catch(Exception e) {
                    System.out.println("Failed to initialize a server: " + e.getMessage());
                }
            }
        }
    }
}