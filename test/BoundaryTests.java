import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class BoundaryTests {
    @Test
    void createAuction() throws LotFactory.UsedLotRangeException, Lot.AuctionAlreadySetException {
        LotFactory lotFactory = new LotFactory();

        // Single letter auction name
        HashMap<Integer, Lot> lotList1 = lotFactory.createLots(10, 15);
        Auction auction1 = new Auction( lotList1, "a", 5, null);

        // Longer auction names
        // Range of lots
        HashMap<Integer, Lot> lotList2 = lotFactory.createLots(1, 2);
        Auction auction2 = new Auction( lotList2, "test1", 5, null);

        // Single lot
        HashMap<Integer, Lot> lotList3 = lotFactory.createLots(3, 3);
        Auction auction3 = new Auction( lotList3, "test2", 5, null);

        // Minimum bid increment
        HashMap<Integer, Lot> lotList4 = lotFactory.createLots(5, 8);
        Auction auction4 = new Auction( lotList4, "test3", 1, null);

        assertNotNull( auction1 );
        assertNotNull( auction2 );
        assertNotNull( auction3 );
        assertNotNull( auction4 );
    }

    @Test
    void createBidder() {
        Bidder bidder1 = new Bidder("z", 12, null);
        assertNotNull(bidder1);
    }
}