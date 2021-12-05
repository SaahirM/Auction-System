import java.util.HashMap;

public class RegularLot extends Lot{
    private int minIncrement;

    public RegularLot(Auction auction, int lotNumber) {
        super(auction, lotNumber);
        this.minIncrement = auction.getMinIncrement();
    }

    public int getMinIncrement() {
        return minIncrement;
    }
}
