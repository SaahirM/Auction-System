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
    private HashMap<Integer, Bidder> allBidders = null; // The set of all bidders

    // Helper arrays for the class
    private Map<Boolean, String> winString = null;

    public Lot( Auction auction, HashMap<Integer, Bidder> allBidders, int lotNumber ) {
        if ((lotNumber > 0) && (auction != null)) {
            //Cache the information for the lot.
            this.lotNumber = lotNumber;
            this.theAuction = auction;
            this.allBidders = allBidders;

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
        int outcome = LotNotAccepting;

        // Make sure the bidder is valid
        boolean validBidder = allBidders.get(bidderId) != null;

        // Proceed with the bid

        if (theAuction.auctionIsOpen() && validBidder) {
            outcome = BidNotAcceptable;
            if (bid > 0) {
                outcome = BidAcceptableNotWinning;

                // If the current winner is re-bidding then it's just to increase the current reserve bid

                if (bidderId == winningBidder) {
                    if (bid > topBid) {
                        topBid = bid;
                        outcome = BidWinning;
                    }
                } else {

                    // An acceptable bid must exceed the current bid by the minimum increment or more.

                    // TO DO: Add code to manage the minimum increment rather than just a bigger bid

                    if (bid > topBid) {
                        // It could be the winning bid.  Let's see if it beats any reserve.

                        outcome = BidWinning;
                        winningBidder = bidderId;

                        topBid = bid;
                    }
                }
            }
        }

        return outcome;
    }

    public boolean isClosed() {
        return theAuction.auctionIsClosed();
    }
}
