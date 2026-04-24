import java.time.*;
import java.util.*;

public class Fitting_Stall {
    private long freeAt = 0;

    public boolean isOccupied() {
        return System.nanoTime() < freeAt;
    }

    public void occupyForSeconds(double seconds) {
        long nanos = (long) (seconds * 1_000_000_000L);
        this.freeAt = System.nanoTime() + nanos;
    }

    public double waitTime(){
        return this.freeAt - System.nanoTime();
    }
}