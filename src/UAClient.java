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
            Scanner scanner = new Scanner(System.in);

            Socket socket = new Socket(host, port);

            System.out.println("Built Socket");

            BufferedReader serverOutput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clientToServer = new PrintWriter(socket.getOutputStream(), true);

            // Server Initialization
            int fittingRooms = Integer.parseInt(args[0]);
            System.out.println("Sending Initialization to Servers...");
            clientToServer.println("INITIALIZE " + fittingRooms);

            String initializationResponse = serverOutput.readLine();
            System.out.println("Initialized.");

            // CLients = fitting rooms + waiting room seats
            int clients = fittingRooms + (fittingRooms*2);

            Thread[] threads = new Thread[clients];
            for (int i = 0; i < clients; i++) {
                threads[i] = new Thread(new UAClientHandler(serverOutput, clientToServer));

                threads[i].start();
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}

class UAClientHandler implements Runnable{

    BufferedReader ServerOutput;
    PrintWriter ClientToServer;

    public UAClientHandler(BufferedReader serverOutput, PrintWriter clientToServer) {
        ServerOutput = serverOutput;
        ClientToServer = clientToServer;
    }

    @Override
    public void run() {
        String requestFittingRoomMsg = "REQUESTING_ROOM";
        ClientToServer.println(requestFittingRoomMsg);

        try {
            String serverResponse = ServerOutput.readLine();
            if(serverResponse.equals("SUCCESS")) {
                // Simulate trying on clothes for 5 seconds
                Thread.sleep(5000);

                String doneMsg = "EXITING_ROOM";
                ClientToServer.println(doneMsg);

                serverResponse = ServerOutput.readLine();
                //Handle server response for exiting room
            } else if(serverResponse.equals("WAITING")) {

            } else if (serverResponse.equals("NO ROOM")) {

            }
        } catch (IOException | InterruptedException ex) {

        }

    }
}
