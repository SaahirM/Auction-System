import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

class BiddingTests {

    @Test
    void NoPriorNoReserveMinBidWins() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(10, 15);
            newAuction = new Auction( lots, "testAuction", 1, null );
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }

        Bidder newBidder = new Bidder("person1", 1, null);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        assertEquals(3, newBidder.placeBidOn(aLot, 1));
    }

    @Test
    void NoPriorBelowMinBid() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(10, 15);
            newAuction = new Auction( lots, "testAuction", 5, null );
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
        Bidder newBidder = new Bidder("person1", 1, null);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );
        assertEquals(2, newBidder.placeBidOn(aLot, 1));

        assertEquals("10\t0\t0\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "Bid is below minimum bid");

    }

    @Test
    void NoPriorOverbidWins() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(10, 15);
            newAuction = new Auction( lots, "testAuction", 1, null );
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
        Bidder newBidder = new Bidder("person1", 1, null);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );
        assertEquals(3, newBidder.placeBidOn(aLot, 100));

        assertEquals("10\t100\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "No reserve bid, overbid wins");

    }


    @Test
    void PriorBidBelowCurrentBid() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(10, 15);
            newAuction = new Auction( lots, "testAuction", 1, null );
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
        Bidder oldBidder = new Bidder("person1", 1, null);
        Bidder middleBidder = new Bidder("person2", 2, null);
        Bidder newBidder = new Bidder("person3", 3, null);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, oldBidder.placeBidOn(aLot, 10));
        // Bump up the current bid to the desired level
        assertEquals(2, middleBidder.placeBidOn(aLot, 10));

        // Now bring in the test bidder
        assertEquals(2, newBidder.placeBidOn(aLot, 5));

        assertEquals("10\t10\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "No reserve bid, current bid not high enough to win");
    }

    @Test
    void PriorBidBeatMaxBid() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(10, 15);
            newAuction = new Auction( lots, "testAuction", 1, null );
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
        Bidder oldBidder = new Bidder("person1", 1, null);
        Bidder middleBidder = new Bidder("person2", 2, null);
        Bidder newBidder = new Bidder("person3", 3, null);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, oldBidder.placeBidOn(aLot, 10));
        // Bump up the current bid to the desired level
        assertEquals(2, middleBidder.placeBidOn(aLot, 10));

        // Now bring in the test bidder
        assertEquals(3, newBidder.placeBidOn(aLot, 20));

        assertEquals("10\t20\t3\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids());

    }

    @Test
    void PriorBidBelowMin() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(10, 15);
            newAuction = new Auction( lots, "testAuction", 1, null );
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
        Bidder oldBidder = new Bidder("person1", 1, null);
        Bidder middleBidder = new Bidder("person2", 2, null);
        Bidder newBidder = new Bidder("person3", 3, null);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, oldBidder.placeBidOn(aLot, 10));
        // Bump up the current bid to the desired level
        assertEquals(2, middleBidder.placeBidOn(aLot, 10));

        // Now bring in the test bidder
        assertEquals(2, newBidder.placeBidOn(aLot, 5));

        assertEquals("10\t10\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids());

    }

    @Test
    void bidTest() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lots = null;
        Auction newAuction = null;

        try {
            lots = lotFactory.createLots(1, 10);
            newAuction = new Auction( lots, "theAuction", 1, null );
        } catch (Exception e) {
            fail("Constructing Auction should not have thrown an exception");
        }
        assertNotNull( newAuction );

        // Check with a bid on a single lot

        Bidder bidder1 = null;
        try {
            bidder1 = new Bidder("newBidder", 1, null);
        } catch (Exception e) {
            fail("Constructor should not have thrown an exception");
        }
        assertNotNull(bidder1);

        Lot lot1 = lots.get( 1 );
        Lot lot2 = lots.get( 2 );
        Lot lot10 = lots.get( 10 );

        newAuction.openAuction();
        assertEquals(3, bidder1.placeBidOn(lot1, 5));
        assertEquals("theAuction\topen\t5\n", newAuction.getStatus());

        // Bid on the other lots and check the outcome of all lots bid upon

        assertEquals(3, bidder1.placeBidOn(lot2, 10));
        assertEquals("theAuction\topen\t15\n", newAuction.getStatus());
        assertEquals(3, bidder1.placeBidOn(lot10, 55));
        assertEquals("theAuction\topen\t70\n", newAuction.getStatus());

    }

}