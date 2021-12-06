import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class BoundaryTests {
    @Test
    void createAuction() {
        LotFactory lotFactory = new LotFactory();

        HashMap<Integer, Lot> lotList1;
        HashMap<Integer, Lot> lotList2;
        HashMap<Integer, Lot> lotList3;
        HashMap<Integer, Lot> lotList4;

        Auction auction1 = null;
        Auction auction2 = null;
        Auction auction3 = null;
        Auction auction4 = null;

        try {
            // Single letter auction name
            lotList1 = lotFactory.createLots(10, 15);
            auction1 = new Auction( lotList1, "a", 5, null);

            // Longer auction names
            // Range of lots
            lotList2 = lotFactory.createLots(1, 2);
            auction2 = new Auction( lotList2, "test1", 5, null);

            // Single lot
            lotList3 = lotFactory.createLots(3, 3);
            auction3 = new Auction( lotList3, "test2", 5, null);

            // Minimum bid increment
            lotList4 = lotFactory.createLots(5, 8);
            auction4 = new Auction( lotList4, "test3", 1, null);
        } catch (Exception e) {
            fail("Constructing Auctions should not have thrown an exception");
        }
        assertNotNull( auction1 );
        assertNotNull( auction2 );
        assertNotNull( auction3 );
        assertNotNull( auction4 );
    }

    @Test
    void createBidder() {
        Bidder bidder1 = null;

        try {
            bidder1 = new Bidder("z", 12, null);
        } catch (Exception e) {
            fail("Constructor should not have thrown an exception");
        }
        assertNotNull(bidder1);
    }
}