import java.io.BufferedReader;
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
            System.out.println("");

            // CLients = fitting rooms + waiting room seats
            int clients = fittingRooms + (fittingRooms*2);


            // Request Fitting Room
            String requestFittingRoomMsg = "REQUESTING_ROOM";
            clientToServer.println(requestFittingRoomMsg);

            String serverResponse = serverOutput.readLine();
            if(serverResponse.equals("SUCCESS")) {
                // Simulate trying on clothes for 5 seconds
                Thread.sleep(5000);

                String doneMsg = "EXITING_ROOM";
                clientToServer.println(doneMsg);

                serverResponse = serverOutput.readLine();
                //Handle server response for exiting room
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
