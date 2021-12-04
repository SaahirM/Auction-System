import java.util.HashMap;

public class ReserveLot extends Lot{
    private int reserveValue;

    public ReserveLot(Auction auction, HashMap<Integer, Bidder> allBidders,
                      int lotNumber, int reserve) {
        super(auction, allBidders, lotNumber);
        this.reserveValue = reserve;
    }

    public int getReserveValue() {
        return reserveValue;
    }
}
