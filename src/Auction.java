import java.util.HashMap;
import java.util.Map;

public class Auction {

    // Constants used by this class
    private static final int NewAuction = 1;
    private static final int OpenAuction = 2;
    private static final int ClosedAuction = 3;

    // Context about this auction
    private String auctionName = null;
    private int minIncrement = 0;
    private int state = NewAuction;
    private String region;

    // Context surrounding this auction
    private HashMap<Integer, Lot> lotSet = null;    // All the lots available

    // Helper variables for the class
    private Map<Integer, String> naming = null;
    private boolean auctionReady = false;

    public Auction( HashMap<Integer, Lot> auctionLots, String auctionName, int minBidIncrement, String region ) throws Lot.AuctionAlreadySetException {
        if ((auctionName != null) && (auctionName.length() > 0) && (minBidIncrement > 0)) {
            this.auctionName = auctionName;
            this.minIncrement = minBidIncrement;
            this.lotSet = auctionLots;
            this.region = region;

            naming = new HashMap<Integer, String>();
            naming.put( NewAuction, "new" );
            naming.put( OpenAuction, "open" );
            naming.put( ClosedAuction, "closed" );

            // Link the lots to this auction
            for (Lot lot : lotSet.values()) {
                lot.setAuction(this);
            }

            auctionReady = true;
        }
    }

    public void replaceLot(Lot lot, int lotNum) throws Lot.AuctionAlreadySetException {
        lotSet.put(lotNum, lot);
        lot.setAuction(this);
    }

    public boolean openAuction( ) {
        boolean opened = false;

        if (state == NewAuction) {
            state = OpenAuction;
            opened = true;
        }

        return opened;
    }

    public boolean closeAuction( ) {
        boolean closed = false;

        if (state == OpenAuction) {
            state = ClosedAuction;
            closed = true;
        }

        return closed;
    }

    public String winningBids( ) {
        StringBuilder bids = null;

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
        String status = "";
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

    public boolean auctionIsReady() {
        return auctionReady;
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
