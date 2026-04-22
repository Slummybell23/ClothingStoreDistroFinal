import java.io.*;
import java.net.*;
import java.time.LocalTime;

public class UACentralServer {
    private Socket <String> clientList = new ArrayList();
    private Socket <String> serverList = new ArrayList();
    
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
        private final Socket clientIP = clientSocket.getInetAddress().getHostAddress();

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new PrintWriter(s.getOutputStream(), true);

                String line;

                while((line = in.readLIne()) != null) {
                    System.out.printf("From Client: ");
                    out.println(line);

                    if(line.contains("INITIALIZE{")) {
                        String str = line.replaceAll("[^0-9]", "");
                        if(Integer.parseInt(str) > 0) {
                            initialize(Integer.parseInt(str));
                        }
                    }

                    if(line.contains("INIT_SUCCESS")) {
                        out.println("INIT_SUCCESS");
                    }

                    if(line.contains("INIT_FAILED")) {
                        out.println("INIT_FAILED");
                    }

                    if(line.contains("SERVER")) {
                        serverList.add(clientSocket);
                    }

                    if(line.contains("CLIENT")) {
                        clientList.add(clientSocket);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void initialize(int nums) {
            int roomsPerServ = nums/serverList.size();
            for(Socket servSock : serverList) {
                try {
                    out.println("INITIALIZE{" + roomsPerServ + "}");
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    
}
