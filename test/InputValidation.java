import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class InputValidation {

    @Test
    void createAuction() {

        LotFactory lotFactory = new LotFactory();
        Auction test1 = null;
        Auction test2 = null;
        Auction test3 = null;
        Auction test4 = null;

        try {
            HashMap<Integer, Lot> lotList1 = lotFactory.createLots(10, 15);
            test1 = new Auction(lotList1, null, 1, null);
        } catch (NullPointerException e) {
            assertEquals("Null param(s) passed", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        try {
            HashMap<Integer, Lot> lotList2 = lotFactory.createLots(16, 20);
            test2 = new Auction(lotList2, "", 1, null);
        } catch (InputMismatchException e) {
            assertEquals("""
                            Bad param(s) passed
                            Min increment (1) must be positive
                            Auction name () cannot be empty"""
                    , e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        try {
            HashMap<Integer, Lot> lotList3 = lotFactory.createLots(21, 25);
            test3 = new Auction(lotList3, "test3", -1, null);
        } catch (InputMismatchException e) {
            assertEquals("""
                            Bad param(s) passed
                            Min increment (-1) must be positive
                            Auction name (test3) cannot be empty"""
                    , e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        try {
            HashMap<Integer, Lot> lotList4 = lotFactory.createLots(26, 30);
            test4 = new Auction(lotList4, "test4", 0, null);
        } catch (InputMismatchException e) {
            assertEquals("""
                            Bad param(s) passed
                            Min increment (0) must be positive
                            Auction name (test4) cannot be empty"""
            , e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        assertNull( test1 );
        assertNull( test2 );
        assertNull( test3 );
        assertNull( test4 );
    }

    @Test
    void createBidder() {
        Bidder bidder1 = null;
        Bidder bidder2 = null;

        try {
            bidder1 = new Bidder(null, 5, null);
        } catch (NullPointerException e) {
            assertEquals("Null bidder name passed", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
        assertNull( bidder1 );

        try {
            bidder2 = new Bidder("", 6, null);
        } catch (InputMismatchException e) {
            assertEquals("""
                            Bad param(s) passed
                            Bidder ID (6) must be positive
                            Bidder Name () cannot be empty"""
            , e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
        assertNull( bidder2 );
    }

    @Test
    void placeBid() {
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