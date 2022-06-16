package roll_poll;

public class ServerDetails {
    int weight;
    String address;
    public ServerDetails(int weight, String address) {
        this.weight = weight;
        this.address = address;
    }
    public int getWeight(){return this.weight;}
    public String getAddress(){return this.address;}
}
