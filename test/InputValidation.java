import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class InputValidation {

    @Test
    void createAuction() {

        LotFactory lotFactory = new LotFactory();

        try {
            HashMap<Integer, Lot> lotList1 = lotFactory.createLots(10, 15);
            Auction test1 = new Auction( lotList1, null, 1, null);
            assertFalse( test1.auctionIsReady() );

            HashMap<Integer, Lot> lotList2 = lotFactory.createLots(16, 20);
            Auction test2 = new Auction( lotList2, "", 1, null);
            assertFalse( test2.auctionIsReady() );

            HashMap<Integer, Lot> lotList3 = lotFactory.createLots(21, 25);
            Auction test3 = new Auction( lotList3, "test3", -1, null);
            assertFalse( test3.auctionIsReady() );

            HashMap<Integer, Lot> lotList4 = lotFactory.createLots(26, 30);
            Auction test4 = new Auction( lotList4, "test4", 0, null);
            assertFalse( test4.auctionIsReady() );

        } catch (LotFactory.UsedLotRangeException e) {
            fail("Creating unique lots should not have raised an exception");
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    void createBidder() {
        Bidder bidder1 = new Bidder(null, 5, null );
        assertFalse( bidder1.bidderIsReady() );

        Bidder bidder2 = new Bidder("", 6, null );
        assertFalse( bidder2.bidderIsReady() );
    }

    @Test
    void placeBid() {
        HashMap<Integer, Bidder> bidderList = new HashMap<>();
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Lot> lotList1 = null;
        HashMap<Integer, Lot> lotList2 = null;
        try {
            lotList1 = lotFactory.createLots(1, 3);
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        }
        try {
            lotList2 = lotFactory.createLots(4, 7);
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        }

        // Load a basic auction to ensure the failure isn't from having no auction data

        try {
            Auction auction1 = new Auction( lotList1, "first", 10, null );
            Auction auction2 = new Auction( lotList2, "second", 20, null );
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        }

        Bidder bidder1 = new Bidder("Alice ", 1, null );
        bidderList.put(1, bidder1);

        // Make bids with bad parameters.  Only one parameter is bad at a time.
        try {
            assertEquals(0, bidder1.placeBidOn(lotList1.get(1), -1));
        } catch (InputMismatchException e) {
            assertEquals("Trying to bid non-positive amount: -1", e.getMessage());
        }try {
            assertEquals(0, bidder1.placeBidOn(lotList1.get(1), 0));
        } catch (InputMismatchException e) {
            assertEquals("Trying to bid non-positive amount: 0", e.getMessage());
        }
    }
}