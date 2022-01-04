import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * @author Saahir Monowar
 * Desc: An auction object to store lots and context surrounding itself
 */
public class Auction {

    // Constants used by this class
    private static final int NewAuction = 1;
    private static final int OpenAuction = 2;
    private static final int ClosedAuction = 3;

    // Context about this auction
    private final String auctionName;
    private final int minIncrement;
    private int state = NewAuction;
    private final String region;

    // Context surrounding this auction
    private final HashMap<Integer, Lot> lotSet;    // This auction's lots

    // Helper variables for the class
    private final Map<Integer, String> naming;

    public Auction( HashMap<Integer, Lot> auctionLots, String auctionName, int minBidIncrement, String region ) throws Lot.AuctionAlreadySetException {
        if (auctionLots == null || auctionName == null) {
            throw new NullPointerException("Null param(s) passed");
        } else if (minBidIncrement <= 0 || auctionName.length() == 0) {
            throw new InputMismatchException(
                    "Bad param(s) passed" +
                    "\nMin increment (" + minBidIncrement + ") must be positive" +
                    "\nAuction name (" + auctionName + ") cannot be empty"
            );
        }

        this.auctionName = auctionName;
        this.minIncrement = minBidIncrement;
        this.lotSet = auctionLots;
        this.region = region;

        naming = new HashMap<>();
        naming.put( NewAuction, "new" );
        naming.put( OpenAuction, "open" );
        naming.put( ClosedAuction, "closed" );

        // Link the lots to this auction
        for (Lot lot : lotSet.values()) {
            lot.setAuction(this);
        }
    }

    /**
     * Adds the passed lot to this auction's set of lots. Also links lot to this auction
     * @param lot lot to be added
     * @param lotNum lot's lot number
     * @throws Lot.AuctionAlreadySetException if another auction is using the passed lot
     */
    public void replaceLot(Lot lot, int lotNum) throws Lot.AuctionAlreadySetException {
        if (lot == null) {
            throw new NullPointerException("Lot is null");
        } else if (lotSet.get(lotNum) == null) {
            throw new IndexOutOfBoundsException("lot doesn't belong to this auction");
        }

        lotSet.put(lotNum, lot);
        lot.setAuction(this);
    }

    public boolean openAuction( ) {
        if (state == NewAuction) {
            state = OpenAuction;
            return true;
        }
        return false;
    }

    public boolean closeAuction( ) {
        if (state == OpenAuction) {
            state = ClosedAuction;
            return true;
        }
        return false;
    }

    public String winningBids( ) {
        StringBuilder bids;

        bids = new StringBuilder();
        for(Lot lot : lotSet.values()) {
            bids.append(lot.winningBidString());
        }

        return bids.toString();
    }

    public int getMinIncrement( ) {
        return minIncrement;
    }

    public String getStatus() {
        String status;
        int bids = 0;

        // Find out all the bids

        for(Lot lot : lotSet.values()) {
            bids += lot.currentBid();
        }

        // Make the return string.

        status = auctionName + "\t" + naming.get(state) + "\t" + bids + "\n";

        return status;
    }

    public int auctionBidTotal() {
        int bids = 0;

        // Find out all the bids

        for(Lot lot : lotSet.values()) {
            bids += lot.currentBid();
        }

        return bids;
    }

    public boolean auctionIsOpen() {
        return state == OpenAuction;
    }

    public boolean auctionIsClosed() {
        return state == ClosedAuction;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public String getRegion() {
        return this.region;
    }
}
