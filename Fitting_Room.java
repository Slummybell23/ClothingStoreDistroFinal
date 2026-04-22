import java.util.*;
public class Fitting_Room{
    private ArrayList<Fitting_Stall> Fr;
    private int waitSeats; 

    public Fitting_Room(int n){
        this.Fr = new ArrayList<Fitting_Stall>(n);
        this.waitSeats = 2*n; 
    }

    public int getNumFr(){
        return this.Fr.size();
    }

    public int getNumWait(){
        return this.waitSeats;
    }
}