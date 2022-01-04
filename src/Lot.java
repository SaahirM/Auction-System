import java.util.InputMismatchException;

/**
 * @author Saahir Monowar
 * Desc: A lot object, which stores bid information
 */
public class Lot {
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
     * Assumes it is called only by Bidder.placeBidOn()
     * @param bid amount being bid
     * @param bidder Bidder doing the bidding
     * @return true if bid is winning
     */
    public boolean placeBid( int bid, Bidder bidder ) {
        assert (bid > 0) : "Bid is negative";
        assert (bidder != null) : "Bidder is null";

        int bidderId = bidder.getBidderId();
        boolean isBidderNearby =
                    bidder.getBidderRegion() == null ||
                    theAuction.getRegion() == null ||
                    bidder.getBidderRegion().equals(theAuction.getRegion());

        if (isClosed() || !isBidderNearby) {
            return false;
        }

        return checkBid(bid, bidderId);


    }

    /**
     * For updating winningBids/IDs/etc once a bid is validated.
     * Overwritten by subclasses if winning bid is decided differently
     * @param bid amount being bid
     * @param bidderId ID of bidder
     * @return true if bid is winning
     */
    protected boolean checkBid(int bid, int bidderId) {
        // If the current winner is re-bidding then it's just to increase the current reserve bid
        if (bidderId == winningBidder) {
            if (bid > topBid) {
                topBid = bid;
                return true;
            }

        } else if (bid >= topBid + minBidIncrement) {
                // An acceptable bid must exceed the current bid by the minimum increment or more.
                winningBidder = bidderId;
                topBid = bid;
                return true;

        }
        return false;
    }

    public boolean isClosed() {
        // Assumes new auctions have closed lots
        return (theAuction == null) || (!theAuction.auctionIsOpen());
    }
}
