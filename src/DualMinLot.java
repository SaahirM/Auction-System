import java.util.HashMap;

public class DualMinLot extends Lot {
    private int incrementLimit;
    private int minInc1;
    private int minInc2;

    public DualMinLot(Auction auction,
                      int lotNumber, int incLim, int min1, int min2) {
        super(auction, lotNumber);
        this.incrementLimit = incLim;
        this.minInc1 = min1;
        this.minInc2 = min2;
    }

    public int getIncrementLimit() {
        return incrementLimit;
    }

    public int getMinInc1() {
        return minInc1;
    }

    public int getMinInc2() {
        return minInc2;
    }
}