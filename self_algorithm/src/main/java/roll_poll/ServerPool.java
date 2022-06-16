package roll_poll;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class ServerPool {
    Random someRandGen = new Random();
    TreeMap<Integer, ServerDetails> pool;
    int totalWeight;

    public void init(ArrayList<ServerDetails> serverList) {
        this.pool = new TreeMap<Integer, ServerDetails>();
        // create the "weighted selection" list
        totalWeight = 0;
        for(ServerDetails serverDetails : serverList) {
            //  associate each server with the sum of the weights so far
            totalWeight += serverDetails.getWeight();
            this.pool.put(totalWeight, serverDetails);
        }
    }

    public ServerDetails getNext() {
        int rnd = someRandGen.nextInt(this.totalWeight);
        return pool.ceilingEntry(rnd).getValue();
    }
}