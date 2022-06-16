package roll_poll;

import java.util.ArrayList;
public class RollPollTest {
    public static void main(String[] args) {
        ArrayList<ServerDetails> serverList = new ArrayList<ServerDetails>() {{
            add(new ServerDetails(20, "10.240.4.8"));
            add(new ServerDetails(30, "10.240.4.10"));
            add(new ServerDetails(25, "10.240.4.9"));
            add(new ServerDetails(25, "10.240.4.6"));
        }};
        ServerPool serverPool = new ServerPool();
        serverPool.init(serverList);
        System.out.println(serverPool.getNext().getAddress());
    }
}
