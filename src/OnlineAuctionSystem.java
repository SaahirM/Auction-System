import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;

public class OnlineAuctionSystem {
    // The state of all the context for the auction system
    private ArrayList<Auction> auctions = null;  // all the auctions
    private HashMap<Integer, Bidder> bidders = null;    // all the bidders
    private HashMap<Integer, Lot> lots = null;  // all the lots among the auctions

    public OnlineAuctionSystem( ) {
        // Create places to store all fo the auctions, all of the bidders, and all of the auction lots.

        auctions = new ArrayList<Auction>();
        bidders = new HashMap<Integer, Bidder>();
        lots = new HashMap<Integer, Lot>();
    }

    public Auction createAuction( String auctionName, int firstLotNumber, int lastLotNumber, int minBidIncrement, String region ) {
        Auction theAuction = null;

        // Check that the lot ranges don't overlap.  If two ranges are distinct then one ends before the next one starts

        boolean distinctLotRange = true;
        for (Auction anAuction : auctions) {
            int lotStart = anAuction.getMinLot();
            int lotEnd = anAuction.getMaxLot();

            if (!((lastLotNumber < lotStart) || (firstLotNumber > lotEnd))) {
                distinctLotRange = false;
                break;
            }
        }

	    // Only create an auction if the auction lot numbers won't overlap with another auction

        if (distinctLotRange) {
            // Make the auction.
            theAuction = new Auction(lots, bidders, auctionName, firstLotNumber, lastLotNumber, minBidIncrement, region);
            if (theAuction.auctionIsReady()) {
                auctions.add(theAuction);
            } else {
                theAuction = null;
            }
        }

        return theAuction;
    }

    public Bidder createBidder( String bidderName, String region ) {
        Bidder theBidder = null;
        int id = 1 + bidders.size();

        // Create the bidder
        theBidder = new Bidder( lots, bidderName, id, region);
        if (theBidder.bidderIsReady()) {
            // Make sure we have space to store the bidder information

            if (bidders == null) {
                bidders = new HashMap<>();
            }

            bidders.put(id, theBidder);
        } else {
            theBidder = null;
        }

        return theBidder;
    }

    public String auctionStatus( ) {
        String status = "";
        Auction auction = null;

	    // Gather the status information from each of the individual auctions that we know about

        Iterator<Auction> iterate = auctions.iterator();

        while (iterate.hasNext()) {
            auction = iterate.next();

            // The status line is the auction name, the state, and the total bids, separated by tabs and ending with
            // a carriage return.

            status = status + auction.getAuctionName() + "\t";

            if (auction.auctionIsOpen()) {
                status = status + "open\t";
            } else if (auction.auctionIsClosed()) {
                status = status + "closed\t";
            } else {
                status = status + "new\t";
            }

            status = status + auction.auctionBidTotal() + "\n";
        }

        return status;
    }

    public int placeBid( int lotNumber, int bidderId, int bid ) {
        if (bidderId < 0 || lotNumber < 0) {
            throw new InputMismatchException("Negative input: " +
                    "\nBidder ID: " + bidderId +
                    "\nLot Number: " + lotNumber);
        }

        Bidder bidder = bidders.get(bidderId);
        Lot bidLot = lots.get( lotNumber );
        if (bidder == null || bidLot == null) {
            throw new NullPointerException("Bidder or Lot doesn't exist");
        }

        return bidder.placeBidOn(bidLot, bid);
    }

    public String feesOwed( ) {
        StringBuilder owed = new StringBuilder();

	    // Each bidder knows what they owe, so gather the status from them directly
        for (int i = 0; i < bidders.size(); i++) {
            Bidder bidder = bidders.get(i);
            owed.append(bidder.feesOwed());
        }

        return owed.toString();
    }
}
