import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class UACentralServer {
    public static ArrayList<Socket> clientList = new ArrayList<Socket>();
    public static ArrayList<Socket> serverList = new ArrayList<Socket>();
    
    public static void main(String [] args) {

        ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(32005);
			System.out.println("Server started, waiting for client");

            while (true) {
                Socket client = servSock.accept();
                System.out.println("New client connected"
                                   + client.getInetAddress()
                                         .getHostAddress());

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
                    Thread.sleep(1000);
                    if(in.ready()) {
                        line = in.readLine();
                        System.out.println("From Client: ");
                        System.out.println(line);

                        if(line.contains("INITIALIZE")) {
                            String str = line.replaceAll("[^0-9]", "");
                            if(Integer.parseInt(str) > 0 && !serverList.isEmpty()) {
                                initialize(Integer.parseInt(str), out);
                            }

                            out.println("INIT_SUCCESS");
                        }

                        if(line.contains("SERVER")) {
                            serverList.add(clientSocket);
                        }

                        if(line.contains("CLIENT")) {
                            clientList.add(clientSocket);
                        }

                        if(line.contains("FR_REQ")) {



                        }



                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void initialize(int nums, PrintWriter out) {
            int roomsPerServ = nums/serverList.size();
            for(Socket servSock : serverList) {
                try {
                    out.println("INITIALIZE " + roomsPerServ);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    
}
