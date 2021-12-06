import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;

public class OnlineAuctionSystem {
    // The state of all the context for the auction system
    private ArrayList<Auction> auctions = null;  // all the auctions
    private HashMap<Integer, Bidder> bidders = null;    // all the bidders
    private LotFactory theLotFactory = null; // produces (unique) lots for auctions
    private HashMap<Integer, Lot> lots = null;  // all the lots among the auctions

    public OnlineAuctionSystem( ) {
        // Create places to store all fo the auctions, all of the bidders, and all of the auction lots.

        auctions = new ArrayList<Auction>();
        bidders = new HashMap<Integer, Bidder>();
        theLotFactory = new LotFactory();
        lots = theLotFactory.getAllLots();
    }

    public Auction createAuction( String auctionName, int firstLotNumber,
            int lastLotNumber, int minBidIncrement, String region )
            throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        Auction theAuction = null;

        HashMap<Integer, Lot> auctionLots = theLotFactory.createLots(firstLotNumber, lastLotNumber);
        theAuction = new Auction(auctionLots, auctionName, minBidIncrement, region);
        auctions.add(theAuction);

        return theAuction;
    }

    public Bidder createBidder( String bidderName, String region ) {
        Bidder theBidder;
        int id = 1 + bidders.size();

        try {
            // Create the bidder
            theBidder = new Bidder(bidderName, id, region);

            // Make sure we have space to store the bidder information

            if (bidders == null) {
                bidders = new HashMap<>();
            }

            bidders.put(id, theBidder);
        } catch (Exception e){
            theBidder = null;
        }

        return theBidder;
    }

    public boolean changeLot(int lotNum, int lotType,
                          int[] args) {

        try {
            Auction auction = lots.get(lotNum).getAuction();
            theLotFactory.changeLotType(lots, lotNum, lotType, args);
            Lot newLot = lots.get(lotNum);
            auction.replaceLot(newLot, lotNum);
        } catch (Lot.AuctionAlreadySetException | LotFactory.LotInUseException e) {
            return false;
        }
        return true;
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
        for (Bidder bidder : bidders.values()) {
            owed.append(bidder.feesOwed());
        }

        return owed.toString();
    }
}
