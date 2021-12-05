import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;

public class Bidder {
    // Context about this bidder
    private int bidderNumber = 0;
    private String bidderName = null;
    private String bidderRegion;

    // Context that surrounds this bidder
    private ArrayList<Lot> lots = new ArrayList<>();

    // State of readiness of the class
    private boolean bidderReady = false;

    public Bidder( String bidderName, int bidderId, String region ) {
        if ((bidderId > 0) && (bidderName != null) && (bidderName.length() > 0)) {
            this.bidderNumber = bidderId;
            this.bidderName = bidderName;
            this.bidderRegion = region;
            bidderReady = true;
        }
    }

    public int getBidderId( ) {
        return bidderNumber;
    }

    public String feesOwed() {
        String owed = "";
        int won = 0;
        int cost = 0;

        for( Lot theLot : lots ){
            if (theLot.isClosed() && (theLot.winningBidder() == bidderNumber)) {
                // Add this lot to what's reported.
                won++;
                cost += theLot.currentBid();
            }
        }

        owed = bidderName + "\t" + won + "\t" + cost + "\n";

        return owed;
    }

    public int placeBidOn(Lot lot, int amount) {
        if (lot == null) {
            throw new NullPointerException("Passed Lot is null");
        } else if (amount <= 0) {
            throw new InputMismatchException("Trying to bid non-positive amount: "
                    + amount);
        } else {
            lots.add(lot);
            return lot.placeBid(amount, this.bidderNumber);
        }
    }

    public boolean bidderIsReady() {
        return bidderReady;
    }
}
