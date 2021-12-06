import java.util.InputMismatchException;

public class Lot {
    // Outcomes of placing a bid.  Making constants available outside the class so others understand the return codes
    public final static int LotNotAccepting = 0;
    public final static int BidNotAcceptable = 1;
    public final static int BidAcceptableNotWinning = 2;
    public final static int BidWinning = 3;

    // Type of lot (so it is not confused with its subclasses)
    public final static int lotType = 0;

    // Context about this lot
    protected int lotNumber;
    protected int winningBidder = 0;
    protected int topBid = 0;
    protected int minBidIncrement = 0;

    // Context about the environment in which the lot belongs
    private Auction theAuction = null;      // The auction to which the lot belongs

    public static class AuctionAlreadySetException extends Exception {
        public AuctionAlreadySetException(String message) {
            super(message);
        }
    }

    public Lot( int lotNumber ) {
        if (lotNumber <= 0) {
            throw new InputMismatchException("lot number must be positive");
        }
        this.lotNumber = lotNumber;
    }

    /**
     * Links this lot to the passed auction (if not already linked to another auction)
     * @param auction Auction to link to
     */
    public void setAuction(Auction auction) throws AuctionAlreadySetException {
        if (auction == null) {
            throw new NullPointerException("Auction is null");
        } else if (this.theAuction == null) {
            this.theAuction = auction;
            this.minBidIncrement = auction.getMinIncrement();
        } else {
            throw new AuctionAlreadySetException("Auction for this lot already set");
        }
    }

    public Auction getAuction() {
        return theAuction;
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

    /**
     * Checks if bid is appropriate and returns a response.
     * Assumes it is called only by a Bidder object
     * @param bid amount being bid
     * @param bidder Bidder doing the bidding
     * @return a bid response (see class constants)
     */
    public int placeBid( int bid, Bidder bidder ) {
        assert (bid > 0) : "Bid is negative";
        assert (bidder != null) : "Bidder is null";

        int bidderId = bidder.getBidderId();
        int outcome = LotNotAccepting;

        if (!isClosed()) {
            outcome = BidNotAcceptable;

            boolean isBidderNearby =
                    bidder.getBidderRegion() == null ||
                    theAuction.getRegion() == null ||
                    bidder.getBidderRegion().equals(theAuction.getRegion());

            if (isBidderNearby) {

                outcome = checkBid(bid, bidderId);

            }
        }

        return outcome;
    }

    /**
     * For updating winningBids/IDs/etc once a bid is validated.
     * Overwritten by subclasses if winning bid is decided differently
     * @param bid amount being bid
     * @param bidderId ID of bidder
     * @return bid response
     */
    protected int checkBid(int bid, int bidderId) {
        // If the current winner is re-bidding then it's just to increase the current reserve bid
        int outcome = BidAcceptableNotWinning;
        if (bidderId == winningBidder) {
            if (bid > topBid) {
                topBid = bid;
                outcome = BidWinning;
            }
        } else {

            // An acceptable bid must exceed the current bid by the minimum increment or more.
            if (bid >= topBid + minBidIncrement) {

                outcome = BidWinning;
                winningBidder = bidderId;

                topBid = bid;
            }
        }
        return outcome;
    }

    public boolean isClosed() {
        // Assumes new auctions have closed lots
        return (theAuction == null) || (!theAuction.auctionIsOpen());
    }
}
