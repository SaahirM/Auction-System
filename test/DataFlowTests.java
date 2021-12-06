import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

class DataFlowTests {
    @Test
    void createAuctionTests() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;

        Auction theAuction = null;
        try {
            lots = lotFactory.createLots(2, 3);
            theAuction = new Auction( lots, "First", 1, null );
        } catch (Exception e) {
            fail("Constructing Auction should not have thrown an exception");
        }

        Bidder theBidder = null;
        try {
            theBidder = new Bidder("someone", 1, null );
        } catch (Exception e) {
            fail("Constructor should not have thrown an exception");
        }

        assertNotNull( theAuction );
        assertNotNull( theBidder );

        // Try to bid on a new auction.  Should fail.

        Lot lot2 = lots.get( 2 );
        Lot lot3 = lots.get( 3 );

        assertEquals( 0,theBidder.placeBidOn(lot2, 5) );

        // Open the auction then try a bid then that should succeed.

        assertTrue( theAuction.openAuction() );
        assertEquals( 3,theBidder.placeBidOn(lot2, 10) );

        // Close the auction then try a bid then that should fail.

        assertTrue( theAuction.closeAuction() );
        assertEquals( 0,theBidder.placeBidOn(lot3, 10) );

    }

    @Test
    void openAndCloseTests() {
        HashMap<Integer, Lot> lots = null;
        LotFactory lotFactory = new LotFactory();
        try {
            lots = lotFactory.createLots(2, 3);
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        }

        Auction theAuction = null;
        try {
            theAuction = new Auction( lots, "First", 1, null );
        } catch (Exception e) {
            fail("Constructor should not have thrown an exception");
        }

        assertNotNull( theAuction );

        //  Try the options on an auction that is new

        assertFalse( theAuction.closeAuction() );
        assertTrue( theAuction.openAuction() );

        // Try the options on an auction that is open

        assertFalse( theAuction.openAuction() );
        assertTrue( theAuction.closeAuction() );

        // Try the options on an auction that is now closed

        assertFalse( theAuction.openAuction() );
        assertFalse( theAuction.closeAuction() );
    }

    @Test
    public void regionTests() {
        LotFactory lotFactory = new LotFactory();
        ArrayList<Auction> allAuctions = new ArrayList<>();

        HashMap<Integer, Lot> lots1;
        HashMap<Integer, Lot> lots2;
        HashMap<Integer, Lot> lots3;

        Auction noRegionAuction = null;
        Auction cityAuction = null;
        Auction forestAuction = null;
        try {
            lots1 = lotFactory.createLots(1, 1);
            noRegionAuction = new Auction( lots1, "NoRegion", 1, null );
            lots2 = lotFactory.createLots(2, 2);
            cityAuction = new Auction( lots2, "Region", 1, "City" );
            lots3 = lotFactory.createLots(3, 3);
            forestAuction = new Auction( lots3, "Region2", 1, "Forest" );
        } catch (Exception e) {
            fail("Constructing Auctions should not have thrown an exception");
        }

        Bidder noRegionBidder = null;
        Bidder cityBidder = null;
        try {
            noRegionBidder = new Bidder("someone", 1, null );
            cityBidder = new Bidder("someone else", 2, "City" );
        } catch (Exception e) {
            fail("Constructors should not have thrown exceptions");
        }

        assertNotNull( noRegionAuction );
        assertNotNull( cityAuction );
        assertNotNull( forestAuction );
        assertNotNull( noRegionBidder );
        assertNotNull( cityBidder );
        allAuctions.add(noRegionAuction);
        allAuctions.add(cityAuction);
        allAuctions.add(forestAuction);

        // Should be able to see all auctions
        ArrayList<Auction> result1 = noRegionBidder.openAuctions(allAuctions);
        assertEquals(3, result1.size());
        assertEquals(noRegionAuction, result1.get(0));
        assertEquals(cityAuction, result1.get(1));
        assertEquals(forestAuction, result1.get(2));

        // Should not return forest auction
        ArrayList<Auction> result2 = cityBidder.openAuctions(allAuctions);
        assertEquals(2, result2.size());
        assertEquals(noRegionAuction, result2.get(0));
        assertEquals(cityAuction, result2.get(1));

    }

    @Test
    public void changeLots() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;

        Auction theAuction = null;
        try {
            lots = lotFactory.createLots(1, 3);
            theAuction = new Auction( lots, "AuctionName", 1, null );
        } catch (Exception e) {
            fail("Constructing Auction should not have thrown an exception");
        }

        Bidder defaultBidder = null;
        Bidder secondBidder = null;
        try {
            defaultBidder = new Bidder("BidderName1", 1, null );
            secondBidder = new Bidder("BidderName2", 2, null );
        } catch (Exception e) {
            fail("Constructors should not have thrown an exception");
        }

        assertNotNull( theAuction );
        assertNotNull( defaultBidder );
        assertNotNull( secondBidder );
        assertTrue( theAuction.openAuction() );

        //Change lot 2 to Reserve, 3 to DualMin
        int[] rParams = {10}; //just reserve value
        int[] dParams = {10, 2, 5}; //IncLim, min1, min2
        try {
            Lot newLot1 = lotFactory.changeLotType(lots, 2, 1, rParams);
            Lot newLot2 = lotFactory.changeLotType(lots, 3, 2, dParams);
            theAuction.replaceLot(newLot1, 2);
            theAuction.replaceLot(newLot2, 3);
        } catch (Exception e) {
            fail("changing lots should not have raised exception");
        }

        // Manually check lot array
        assertTrue(lots.get(2) instanceof ReserveLot);
        assertTrue(lots.get(3) instanceof DualMinLot);

        // Place some bids
        // setup
        assertEquals(3, defaultBidder.placeBidOn(lots.get(1), 1));
        assertEquals(3, defaultBidder.placeBidOn(lots.get(3), 15));

        // only regular lot will accept this
        assertEquals(3, secondBidder.placeBidOn(lots.get(1), 2));

        // only reserve lot will reject this
        assertEquals(2, secondBidder.placeBidOn(lots.get(2), 9));

        // only dualmin lot will reject this
        assertEquals(2, secondBidder.placeBidOn(lots.get(3), 19));
    }

}