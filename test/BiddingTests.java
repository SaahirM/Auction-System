import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.ArrayList;

class BiddingTests {

    @Test
    void NoPriorNoReserveMinBidWins() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction newAuction = new Auction( lots, bidders, "testAuction", 10, 15, 1, null );
        Bidder newBidder = new Bidder( lots,"person1", 1, null);
        bidders.put(1, newBidder);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        assertEquals(3, newBidder.placeBidOn(aLot, 1));
    }

    @Test
    void NoPriorBelowMinBid() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 5, null);
        Bidder newBidder = new Bidder( lots,"person1", 1, null);
        bidders.put(1, newBidder);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );
        assertEquals(2, newBidder.placeBidOn(aLot, 1));

        assertEquals("10\t0\t0\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "Bid is below minimum bid");

    }

    @Test
    void NoPriorOverbidWins() {

        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 1, null);
        Bidder newBidder = new Bidder( lots,"person1", 1, null);
        bidders.put(1, newBidder);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );
        assertEquals(3, newBidder.placeBidOn(aLot, 100));

        assertEquals("10\t100\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids(), "No reserve bid, overbid wins");

    }


    @Test
    void PriorBidBelowCurrentBid() {

        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction newAuction = new Auction( lots, bidders,"testAuction", 10, 15, 5, null);
        Bidder oldBidder = new Bidder( lots,"person1", 1, null);
        Bidder middleBidder = new Bidder(lots, "person2", 2, null);
        Bidder newBidder = new Bidder(lots, "person3", 3, null);

        bidders.put(1, oldBidder);
        bidders.put(2, middleBidder);
        bidders.put(3, newBidder);

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

        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 5, null);
        Bidder oldBidder = new Bidder(lots, "person1", 1, null);
        Bidder middleBidder = new Bidder(lots, "person2", 2, null);
        Bidder newBidder = new Bidder(lots, "person3", 3, null);

        bidders.put(1, oldBidder);
        bidders.put(2, middleBidder);
        bidders.put(3, newBidder);

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

        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();

        Auction newAuction = new Auction(lots, bidders, "testAuction", 10, 15, 5, null);
        Bidder oldBidder = new Bidder(lots, "person1", 1, null);
        Bidder middleBidder = new Bidder(lots, "person2", 2, null);
        Bidder newBidder = new Bidder(lots, "person3", 3, null);

        bidders.put(1, oldBidder);
        bidders.put(2, middleBidder);
        bidders.put(3, newBidder);

        assertTrue(newAuction.openAuction());

        Lot aLot = lots.get( 10 );

        // Get in a first bidder
        assertEquals(3, oldBidder.placeBidOn(aLot, 10));
        // Bump up the current bid to the desired level
        assertEquals(2, middleBidder.placeBidOn(aLot, 10));

        // Now bring in the test bidder
        assertEquals(2, newBidder.placeBidOn(aLot, 11));

        assertEquals("10\t10\t1\n11\t0\t0\n12\t0\t0\n13\t0\t0\n14\t0\t0\n15\t0\t0\n", newAuction.winningBids());

    }

    @Test
    void bidTest() {
        HashMap<Integer, Lot> lots = new HashMap<>();
        HashMap<Integer, Bidder> bidders = new HashMap<>();


        Auction auction1 = new Auction(lots, bidders, "theAuction", 1, 10, 5, null);
        assertTrue( auction1.auctionIsReady() );

        // Check with a bid on a single lot

        Bidder bidder1 = new Bidder(lots, "newBidder", 1, null);
        assertTrue(bidder1.bidderIsReady());
        bidders.put(1, bidder1);

        Lot lot1 = lots.get( 1 );
        Lot lot2 = lots.get( 2 );
        Lot lot10 = lots.get( 10 );

        auction1.openAuction();
        assertEquals(3, bidder1.placeBidOn(lot1, 5));
        assertEquals("theAuction\topen\t5\n", auction1.getStatus());

        // Bid on the other lots and check the outcome of all lots bid upon

        assertEquals(3, bidder1.placeBidOn(lot2, 10));
        assertEquals("theAuction\topen\t15\n", auction1.getStatus());
        assertEquals(3, bidder1.placeBidOn(lot10, 55));
        assertEquals("theAuction\topen\t70\n", auction1.getStatus());

    }

}