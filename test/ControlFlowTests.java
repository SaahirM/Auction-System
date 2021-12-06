import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ControlFlowTests {

    @Test
    void createAuctionTests() throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        LotFactory lotFactory = new LotFactory();

        HashMap<Integer, Lot> lotList1 = lotFactory.createLots(2, 3);
        Auction auction1 = new Auction(lotList1, "First", 1, null);
        HashMap<Integer, Lot> lotList2 = lotFactory.createLots(4, 5);
        Auction auction2 = new Auction(lotList2, "Second", 1, null);
        HashMap<Integer, Lot> lotList3 = lotFactory.createLots(6, 7);
        Auction auction3 = new Auction(lotList3, "Third", 1, null);


        assertNotNull( auction1 );
        assertNotNull( auction2 );
        assertNotNull( auction3 );
    }

    @Test
    void createBidderTests() {
        Bidder bidder1 = new Bidder("Alice", 1, null);
        Bidder bidder2 = new Bidder("Bob", 2, null );
        Bidder bidder3 = new Bidder("Charlene", 3, null );

        assertNotNull(bidder1);
        assertNotNull(bidder2);
        assertNotNull(bidder3);
    }

    @Test
    void auctionStatusTests() throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        LotFactory lotFactory = new LotFactory();

        HashMap<Integer, Lot> lotList1 = lotFactory.createLots(2, 3);
        Auction auction1 = new Auction(lotList1, "First", 1, null);
        HashMap<Integer, Lot> lotList2 = lotFactory.createLots(4, 5);
        Auction auction2 = new Auction(lotList2, "Second", 1, null);
        HashMap<Integer, Lot> lotList3 = lotFactory.createLots(6, 7);
        Auction auction3 = new Auction(lotList3, "Third", 1, null);


        if (auction2.openAuction() && auction3.openAuction() && auction3.closeAuction()) {
            assertEquals("First\tnew\t0\n", auction1.getStatus());
            assertEquals("Second\topen\t0\n", auction2.getStatus());
            assertEquals("Third\tclosed\t0\n", auction3.getStatus());
        }
    }

    @Test
    void winningBids() throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        LotFactory lotFactory = new LotFactory();

        HashMap<Integer, Lot> lots = lotFactory.createLots(10, 15);
        Auction theAuction = new Auction(lots, "FirstAuction", 2, null);

        assertNotNull( theAuction );

        theAuction.openAuction();

        // Make some bidders to work with

        Bidder bidder1 = new Bidder("Alice", 1, null );
        Bidder bidder2 = new Bidder("Bob", 2, null );
        Bidder bidder3 = new Bidder("Charlie", 3, null );

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
    void feesOwed() throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        LotFactory lotFactory = new LotFactory();

        // Get a mix of auctions to receive bids
        HashMap<Integer, Lot> lotList1 = lotFactory.createLots(10, 19);
        Auction auction1 = new Auction(lotList1, "FirstAuction", 2, null);
        HashMap<Integer, Lot> lotList2 = lotFactory.createLots(20, 29);
        Auction auction2 = new Auction(lotList2, "SecondAuction", 2, null);
        HashMap<Integer, Lot> lotList3 = lotFactory.createLots(30, 39);
        Auction auction3 = new Auction(lotList3, "ThirdAuction", 2, null);
        HashMap<Integer, Lot> lotList4 = lotFactory.createLots(40, 49);
        Auction auction4 = new Auction(lotList4, "FourthAuction", 2, null);
        HashMap<Integer, Lot> lotList5 = lotFactory.createLots(50, 59);
        Auction auction5 = new Auction(lotList5, "NewAuction", 2, null);

        assertNotNull( auction1 );
        assertNotNull( auction2 );
        assertNotNull( auction3 );
        assertNotNull( auction4 );
        assertNotNull( auction5 );

        assertTrue( auction1.openAuction());
        assertTrue( auction2.openAuction());
        assertTrue( auction3.openAuction());
        assertTrue( auction4.openAuction());

        // Set up some bidders too
        Bidder bidder1 = new Bidder("Alice", 1, null); // win 0 closed, 0 open
        Bidder bidder2 = new Bidder("Bob", 2, null); // win 0 closed, 1 open
        Bidder bidder3 = new Bidder("Charlie", 3, null); // win 1 closed, 0 open
        Bidder bidder4 = new Bidder("Denise", 4, null); // win 1 closed, 1 open
        Bidder bidder5 = new Bidder("Edna", 5, null); // win many closed, 0 open
        Bidder bidder6 = new Bidder("Frank", 6, null); // win many closed, many open


        assertNotNull( bidder1 );
        assertNotNull( bidder2 );
        assertNotNull( bidder3 );
        assertNotNull( bidder4 );
        assertNotNull( bidder5 );
        assertNotNull( bidder6 );

        // Set the bids as we'll expect.  Auctions 2 and 3 will get closed.  Auction 5 is never opened.

        // Win 0 closed, 1 open
        assertEquals( 3,bidder2.placeBidOn(lotList1.get( 15 ), 2) );

        // Win 1 closed, 0 open

        assertEquals( 3,bidder3.placeBidOn(lotList2.get( 24 ), 2) );

        // Win 1 closed, 1 open

        assertEquals( 3,bidder4.placeBidOn(lotList2.get( 26 ), 2) );
        assertEquals( 3,bidder4.placeBidOn(lotList1.get( 16 ), 2) );

        // Win many closed, 0 open

        assertEquals( 3,bidder5.placeBidOn(lotList3.get( 30 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lotList3.get( 31 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lotList3.get( 32 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lotList3.get( 33 ), 2) );
        assertEquals( 3,bidder5.placeBidOn(lotList3.get( 34 ), 2) );

        // Win many closed, many open

        assertEquals( 3,bidder6.placeBidOn(lotList3.get( 35 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList3.get( 36 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList3.get( 37 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList3.get( 38 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList4.get( 42 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList4.get( 43 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList4.get( 44 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList4.get( 45 ), 2) );
        assertEquals( 3,bidder6.placeBidOn(lotList4.get( 46 ), 2) );

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
    void lotState() throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        LotFactory lotFactory = new LotFactory();

        // Get a mix of auctions to receive bids
        HashMap<Integer, Lot>lots = lotFactory.createLots(10, 19);
        Auction auction1 = new Auction(lots, "FirstAuction", 2, null);


        // Ensure that the state of a lot matches the state of the auction to which it belongs.

        Lot aLot = lots.get( 15 );

        // new auction
        assertTrue( aLot.isClosed() );

        auction1.openAuction();
        assertFalse( aLot.isClosed() );

        auction1.closeAuction();
        assertTrue( aLot.isClosed() );

        // Lot with no auction
        HashMap<Integer, Lot> lotList = lotFactory.createLots(20, 20);

        Lot anotherLot = lotList.get(20);
        assertTrue( anotherLot.isClosed() );
    }
}