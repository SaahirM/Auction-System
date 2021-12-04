import java.util.HashMap;

public class RegularLot extends Lot{
    private int minIncrement;

    public RegularLot(Auction auction, HashMap<Integer, Bidder> allBidders, int lotNumber) {
        super(auction, allBidders, lotNumber);
        this.minIncrement = auction.getMinIncrement();
    }

    public int getMinIncrement() {
        return minIncrement;
    }
}
