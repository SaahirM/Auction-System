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

    public String getBidderRegion() {
        return this.bidderRegion;
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
            return lot.placeBid(amount, this);
        }
    }

    public boolean bidderIsReady() {
        return bidderReady;
    }

    /**
     * Returns list of auctions with the same region as this bidder
     * @param allAuctions arrayList of every auction
     * @return availableOptions arrayList of auctions bidder can bid at
     */
    public ArrayList<Auction> openAuctions(ArrayList<Auction> allAuctions) {
        if (allAuctions == null) {
            throw new NullPointerException("allAuctions ArrayList is null");
        }

        ArrayList<Auction> availableOptions = new ArrayList<>();
        if (this.bidderRegion == null) {
            availableOptions.addAll(allAuctions);
            return availableOptions;
        }

        for (Auction auction : allAuctions) {
            if (auction.getRegion() == null ||
                    auction.getRegion().equals(this.bidderRegion)
            ) {
                availableOptions.add(auction);
            }
        }
        return availableOptions;
    }
}
