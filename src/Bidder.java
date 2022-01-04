import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * @author Saahir Monowar
 * Desc: An entity with the ability to place bids
 */
public class Bidder {
    // Context about this bidder
    private final int bidderNumber;
    private final String bidderName;
    private final String bidderRegion;
    // lots where this bidder has bid
    private final ArrayList<Lot> lots = new ArrayList<>();

    public Bidder( String bidderName, int bidderId, String region ) {
        if (bidderName == null) {
            throw new NullPointerException("Null bidder name passed");
        } else if (bidderId <= 0 || bidderName.length() == 0) {
            throw new InputMismatchException(
                    "Bad param(s) passed" +
                    "\nBidder ID (" + bidderId + ") must be positive" +
                    "\nBidder Name (" + bidderName + ") cannot be empty"
            );
        }

        this.bidderNumber = bidderId;
        this.bidderName = bidderName;
        this.bidderRegion = region;
    }

    public int getBidderId( ) {
        return bidderNumber;
    }

    public String getBidderRegion() {
        return this.bidderRegion;
    }

    public String feesOwed() {
        String owed;
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

    /**
     * Makes this bidder place a bid on a lot
     * @param lot lot to place bid on
     * @param amount amount to bid
     * @return true if bid is winning
     */
    public boolean placeBidOn(Lot lot, int amount) {
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
