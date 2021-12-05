import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Lot {
    // Outcomes of placing a bid.  Making constants available outside the class so others understand the return codes
    public final static int LotNotAccepting = 0;
    public final static int BidNotAcceptable = 1;
    public final static int BidAcceptableNotWinning = 2;
    public final static int BidWinning = 3;

    // Context about this lot
    private int lotNumber = 0;
    private int winningBidder = 0;
    private int topBid = 0;

    // Context about the environment in which the lot belongs
    private Auction theAuction = null;      // The auction to which the lot belongs

    // Helper arrays for the class
    private Map<Boolean, String> winString = null;

    public Lot( Auction auction, int lotNumber ) {
        if ((lotNumber > 0) && (auction != null)) {
            //Cache the information for the lot.
            this.lotNumber = lotNumber;
            this.theAuction = auction;

            // Load some generic naming that we'll use when someone wins or loses the lot bid.
            winString = new HashMap<Boolean, String>();
            winString.put( true, "winning" );
            winString.put( false, "losing" );
        }
    }

    public int currentBid() {
        return topBid;
    }

    public int winningBidder() {
        return winningBidder;
    }

    public String winningBidString() {
        return lotNumber + "\t" + topBid + "\t" + winningBidder + "\n";
    }

    public int placeBid( int bid, int bidderId ) {
        assert (bid > 0) : "Bid is negative";
        int outcome = LotNotAccepting;

        if (theAuction.auctionIsOpen()) {
            outcome = BidAcceptableNotWinning;

            // If the current winner is re-bidding then it's just to increase the current reserve bid
            if (bidderId == winningBidder) {
                if (bid > topBid) {
                    topBid = bid;
                    outcome = BidWinning;
                }
            } else {

                // An acceptable bid must exceed the current bid by the minimum increment or more.
                if (bid >= topBid + theAuction.getMinIncrement()) {

                    outcome = BidWinning;
                    winningBidder = bidderId;

                    topBid = bid;
                }
            }
        }

        return outcome;
    }

    public boolean isClosed() {
        return theAuction.auctionIsClosed();
    }
}
