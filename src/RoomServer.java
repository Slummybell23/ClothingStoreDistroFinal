import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class RoomServer {
    public Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    public Fitting_Room fr;

    public RoomServer(int port, String hostname) throws Exception {
        this.socket = new Socket(hostname, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        out.println("SERVER");
    }

    public void handle(String message) {
        String[] tokens = message.split(" ");
        int arg = -1;
        try {
            if (tokens.length == 2) arg = Integer.parseInt(tokens[1]);
        } catch (Exception e) { e.printStackTrace(); }
        switch(tokens[0]) {
            case "INITIALIZE":
                initialize(arg);
                break;
            case "FR_REQ":
                fr_req(arg);
                break;
            case "FR_EXIT":
                fr_exit(arg);
                break;
            case "WR_EXIT":
                wr_exit(arg);
                break;
            case "GET_FR":
                get_fr();
                break;
            case "GET_WR":
                get_wr();
                break;
            case "GET_NEXT_AVAIL":
                get_next_avail();
                break;
        }
    }

    public void initialize(int arg) {
        if (arg <= 0 || fr != null) {
            out.println("INIT_FAILED");
            return;
        }
        fr = new Fitting_Room(arg);
        out.println("INIT_SUCCESS");
    }

    public void fr_req(int arg) {

    }

    public void fr_exit(int arg) {

    }

    public void wr_exit(int arg) {

    }

    public void get_fr() {

    }

    public void get_wr() {

    }

    public void get_next_avail() {

    }

    public static void main(String[] args) throws Exception {
        while (true) {
            try {
                RoomServer room = new RoomServer(32005, "localhost");
                String message;
                while ((message = room.in.readLine()) != null) {
                    if (message.trim().isEmpty()) continue;
                    try {
                        room.handle(message);
                    } catch (Exception e) { e.printStackTrace(); }
                }
                System.out.println("DISCONNECTED");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
        }
    }
}