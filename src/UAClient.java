import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UAClient {

    private Socket client;

    public UAClient(Socket client) {

        this.client = client;

    }

    public static void main(String[] args) {

        String host = "localhost";
        int port = 32005;

        System.out.println("starting client");

        try {
            Socket socket = new Socket(host, port);

            System.out.println("Built Socket");

            BufferedReader serverOutput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clientToServer = new PrintWriter(socket.getOutputStream(), true);

            // Server Initialization
            int fittingRooms = Integer.parseInt(args[0]);
            System.out.println("Sending Initialization to Servers...");
            clientToServer.println("INITIALIZE " + fittingRooms);

            String initializationResponse = serverOutput.readLine();
            if (initializationResponse.equals("INIT_SUCCESS")) {
                System.out.println("Initialized.");
            }
            else if (initializationResponse.equals("INIT_FAILED")) {
                System.out.println("Failed to initialize");
                Thread.sleep(5000);
                System.exit(1);
            }

            // CLients = fitting rooms + waiting room seats
            int clients = fittingRooms + (fittingRooms*2);

            Thread[] threads = new Thread[clients];
            for (int i = 0; i < clients; i++) {
                Socket newSocket = new Socket(host, port);
                threads[i] = new Thread(new UAClientHandler(newSocket));
            }

            for (int i = 0; i < threads.length; i++) {
                double randomValue = 0.5 + (Math.random() * 0.5);
                Thread.sleep((int)(randomValue * 1000));

                threads[i].start();
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}

class UAClientHandler implements Runnable{

    Socket Socket;
    BufferedReader ServerOutput;
    PrintWriter ClientToServer;

    public UAClientHandler(Socket socket) throws IOException {
        Socket = socket;

        BufferedReader serverOutput = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        PrintWriter clientToServer = new PrintWriter(Socket.getOutputStream(), true);

        ServerOutput = serverOutput;
        ClientToServer = clientToServer;
    }

    @Override
    public void run() {
        String clientInitialization = "CLIENT";
        ClientToServer.println(clientInitialization);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String requestFittingRoomMsg = "FR_REQ";
        ClientToServer.println(requestFittingRoomMsg);

        try {
            String serverResponse = ServerOutput.readLine();
            if(serverResponse.equals("WAIT")) {
                //Wait a few seconds and check if server has a new message
                Thread.sleep(5000);
                if(ServerOutput.ready()) {
                    UseWaitingRoom();
                }

            } else if (serverResponse.equals("NO")) {
                System.out.println("No rooms available...");
            } else {
                UseWaitingRoom();
            }
        } catch (IOException | InterruptedException ex) {

        }

    }

    private void UseWaitingRoom() throws InterruptedException, IOException {
        String serverResponse;
        // Simulate trying on clothes for 5 seconds
        double randomValue = 0.5 + (Math.random() * 0.5);
        Thread.sleep((int)(randomValue * 1000));

        String doneMsg = "FR_EXIT";
        ClientToServer.println(doneMsg);

        serverResponse = ServerOutput.readLine();
        //Handle server response for exiting room
        System.out.println("Exited fitting room.");
    }
}
