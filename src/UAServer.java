import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.*;

public class UAServer extends Thread {
    public Socket socket;
    public BufferedReader out;
    public PrintWriter in;
    //public Fitting_Room fr;

    public UAServer(int port, String hostname) throws Exception {
        this.socket = new Socket(hostname, port);
        this.out = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.in = new PrintWriter(socket.getOutputStream(), true);
        in.println("SERVER");
    }

    public void handle(String message) {
        String[] tokens = message.split(" ");
        int arg = -1;
        if (tokens.length == 2) arg = Integer.parseInt(tokens[1]);
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
            default:
                in.println("");
        }
    }

    public void initialize(int arg) {
        // fr = new Fitting_Room(arg);
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
        UAServer room = new UAServer(32005, "localhost");
        for(;;) {
            Thread.sleep(500);
            boolean isReady = room.out.ready();
            while (isReady) {
                room.handle(room.out.readLine());
                isReady = room.out.ready();
            }
        }
    }
}