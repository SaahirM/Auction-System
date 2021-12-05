import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

class DataFlowTests {
    @Test
    void createAuctionTests() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        try {
            lots = lotFactory.createLots(2, 3);
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        }
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction theAuction = null;
        try {
            theAuction = new Auction( lots, "First", 1, null );
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
        Bidder theBidder = new Bidder("someone", 1, null );

        assertTrue( theAuction.auctionIsReady() );
        assertTrue( theBidder.bidderIsReady() );

        bidders.put(1, theBidder);

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
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }

        assertTrue( theAuction.auctionIsReady() );

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

}