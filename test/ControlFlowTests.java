import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ControlFlowTests {

    @Test
    void createAuctionTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction auction1 = new Auction( lots, bidders, "First", 2, 3, 1, null );
        Auction auction2 = new Auction( lots, bidders, "Second", 4, 5, 1, null );
        Auction auction3 = new Auction( lots, bidders, "Third", 6, 7, 1, null);

        assertTrue( auction1.auctionIsReady() );
        assertTrue( auction2.auctionIsReady() );
        assertTrue( auction3.auctionIsReady() );
    }

    @Test
    void createBidderTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Bidder bidder1 = new Bidder( lots, "Alice", 1, null );
        Bidder bidder2 = new Bidder( lots, "Bob", 2, null );
        Bidder bidder3 = new Bidder( lots, "Charlene", 3, null );

        assertTrue( bidder1.bidderIsReady() );
        assertTrue( bidder2.bidderIsReady() );
        assertTrue( bidder3.bidderIsReady() );
    }

    @Test
    void auctionStatusTests() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction auction1 = new Auction( lots, bidders, "First", 2, 3, 1, null );
        Auction auction2 = new Auction( lots, bidders, "Second", 4, 5, 1, null );
        Auction auction3 = new Auction( lots, bidders, "Third", 6, 7, 1, null );

        if (auction2.openAuction() && auction3.openAuction() && auction3.closeAuction()) {
            assertEquals("First\tnew\t0\n", auction1.getStatus());
            assertEquals("Second\topen\t0\n", auction2.getStatus());
            assertEquals("Third\tclosed\t0\n", auction3.getStatus());
        }
    }

    @Test
    void winningBids() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();
        Auction theAuction = new Auction( lots, bidders, "FirstAuction", 10, 15, 2, null );
        assertNotNull( theAuction );

        theAuction.openAuction();

        // Make some bidders to work with

        Bidder bidder1 = new Bidder( lots, "Alice", 1, null );
        Bidder bidder2 = new Bidder( lots, "Bob", 2, null );
        Bidder bidder3 = new Bidder( lots, "Charlie", 3, null );

        bidders.put(1, bidder1);
        bidders.put(2, bidder2);
        bidders.put(3, bidder3);

        // Set up some bids on lots.  Leave lot 11 without a bid.

        Lot lot12 = lots.get( 12 );
        Lot lot13 = lots.get( 13 );
        Lot lot14 = lots.get( 14 );

        assertEquals( 3, bidder1.placeBidOn(lot12, 2) );

        assertEquals( 3, bidder2.placeBidOn(lot13, 4) );
        assertEquals( 3, bidder3.placeBidOn(lot13, 6) );

        assertEquals( 3, bidder2.placeBidOn(lot14, 10) );
        assertEquals( 2, bidder3.placeBidOn(lot14, 6) );

        // Check out the outcomes

        assertEquals( "10\t0\t0\n11\t0\t0\n12\t2\t1\n13\t6\t3\n14\t10\t2\n15\t0\t0\n", theAuction.winningBids() );

    }

    @Test
    void feesOwed() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        // Get a mix of auctions to receive bids

        Auction auction1 = new Auction( lots, bidders, "FirstAuction", 10, 19, 2, null );
        Auction auction2 = new Auction( lots, bidders, "SecondAuction", 20, 29, 2, null );
        Auction auction3 = new Auction( lots, bidders, "ThirdAuction", 30, 39, 2, null );
        Auction auction4 = new Auction( lots, bidders, "FourthAuction", 40, 49, 2, null );
        Auction auction5 = new Auction( lots, bidders, "NewAuction", 50, 59, 2, null );

        assertTrue( auction1.auctionIsReady() );
        assertTrue( auction2.auctionIsReady() );
        assertTrue( auction3.auctionIsReady() );
        assertTrue( auction4.auctionIsReady() );
        assertTrue( auction5.auctionIsReady() );

        assertTrue( auction1.openAuction());
        assertTrue( auction2.openAuction());
        assertTrue( auction3.openAuction());
        assertTrue( auction4.openAuction());

        // Set up some bidders too

        Bidder bidder1 = new Bidder( lots, "Alice", 1, null ); // win 0 closed, 0 open
        Bidder bidder2 = new Bidder( lots, "Bob", 2, null ); // win 0 closed, 1 open
        Bidder bidder3 = new Bidder( lots, "Charlie", 3, null ); // win 1 closed, 0 open
        Bidder bidder4 = new Bidder( lots, "Denise", 4, null ); // win 1 closed, 1 open
        Bidder bidder5 = new Bidder( lots, "Edna", 5, null ); // win many closed, 0 open
        Bidder bidder6 = new Bidder( lots, "Frank", 6, null ); // win many closed, many open

        assertTrue( bidder1.bidderIsReady() );
        assertTrue( bidder2.bidderIsReady() );
        assertTrue( bidder3.bidderIsReady() );
        assertTrue( bidder4.bidderIsReady() );
        assertTrue( bidder5.bidderIsReady() );
        assertTrue( bidder6.bidderIsReady() );

        bidders.put(1, bidder1);
        bidders.put(2, bidder2);
        bidders.put(3, bidder3);
        bidders.put(4, bidder4);
        bidders.put(5, bidder5);
        bidders.put(6, bidder6);

        // Set the bids as we'll expect.  Auctions 2 and 3 will get closed.  Auction 5 is never opened.

        // Win 0 closed, 1 open
        assertEquals( 3,bidder2.placeBidOn(lots.get( 15 ), 2) );

        // Win 1 closed, 0 open

        assertEquals( 3,bidder3.placeBidOn(lots.get( 24 ), 2) );

        // Win 1 closed, 1 open

        assertEquals( 3,bidder4.placeBidOn(lots.get( 26 ), 2) );
        assertEquals( 3,bidder4.placeBidOn(lots.get( 16 ), 2) );

        // Win many closed, 0 open

        assertEquals( 3,bidder5.placeBidOn(lots.get( 30 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lots.get( 31 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lots.get( 32 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lots.get( 33 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lots.get( 34 ), 2) );

        // Win many closed, many open

        assertEquals( 3,bidder6.placeBidOn(lots.get( 35 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 36 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 37 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 38 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 42 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 43 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 44 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 45 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lots.get( 46 ), 2) );

        // Close off the relevant auctions

        assertTrue( auction2.closeAuction() );
        assertTrue( auction3.closeAuction() );

        // Check out the outcomes

        assertEquals( "Alice\t0\t0\n", bidder1.feesOwed() );
        assertEquals( "Bob\t0\t0\n", bidder2.feesOwed() );
        assertEquals( "Charlie\t1\t2\n", bidder3.feesOwed() );
        assertEquals( "Denise\t1\t2\n", bidder4.feesOwed() );
        assertEquals( "Edna\t5\t10\n", bidder5.feesOwed() );
        assertEquals( "Frank\t4\t8\n", bidder6.feesOwed() );

    }

    @Test
    void lotState() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        // Get a mix of auctions to receive bids

        Auction auction1 = new Auction(lots, bidders, "FirstAuction", 10, 19, 2, null);

        // Ensure that the state of a lot matches the state of the auction to which it belongs.

        Lot aLot = lots.get( 15 );

        assertFalse( aLot.isClosed() );

        auction1.openAuction();
        assertFalse( aLot.isClosed() );

        auction1.closeAuction();
        assertTrue( aLot.isClosed() );
    }
}