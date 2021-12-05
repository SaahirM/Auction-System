import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class BoundaryTests {
    @Test
    void createAuction() {
        LotFactory lotFactory = new LotFactory();
        HashMap<Integer, Bidder> bidderList = new HashMap<>();

        HashMap<Integer, Lot> lotList1 = null;
        HashMap<Integer, Lot> lotList2 = null;
        HashMap<Integer, Lot> lotList3 = null;
        HashMap<Integer, Lot> lotList4 = null;

        Auction auction1 = null;
        Auction auction2 = null;
        Auction auction3 = null;
        Auction auction4 = null;

        try {
            // Single letter auction name
            lotList1 = lotFactory.createLots(10, 15);
            auction1 = new Auction( lotList1, "a", 5, null);
            assertTrue( auction1.auctionIsReady() );

            // Longer auction names
            // Range of lots
            lotList2 = lotFactory.createLots(1, 2);
            auction2 = new Auction( lotList2, "test1", 5, null);
            assertTrue( auction2.auctionIsReady() );

            // Single lot
            lotList3 = lotFactory.createLots(3, 3);
            auction3 = new Auction( lotList3, "test2", 5, null);
            assertTrue( auction3.auctionIsReady() );

            // Minimum bid increment
            lotList4 = lotFactory.createLots(5, 8);
            auction4 = new Auction( lotList4, "test3", 1, null);
            assertTrue( auction4.auctionIsReady() );
        } catch (Lot.AuctionAlreadySetException e) {
            fail("Unexpected exception");
        } catch (LotFactory.UsedLotRangeException e) {
            fail("creating unique lots should not have raised an exception");
        }
    }

    @Test
    void createBidder() {
        HashMap<Integer, Lot> lotList = new HashMap<>();
        Bidder bidder1 = null;

        bidder1 = new Bidder("z", 12, null );
        assertTrue( bidder1.bidderIsReady() );
    }


}