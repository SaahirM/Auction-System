import java.util.HashMap;
import java.util.InputMismatchException;

public class LotFactory {
    // Common HashMap of lots for every lot factory to ensure no duplicates
    static HashMap<Integer, Lot> allLots = new HashMap<>();

    //Exception thrown when lots range not unique
    public class UsedLotRangeException extends Exception {

        public UsedLotRangeException(String message) {
            super(message);
        }
    }

    public LotFactory() {

    }

    public HashMap<Integer, Lot> createLots(Auction auction,
            int firstLotNum, int lastLotNum) throws UsedLotRangeException {
        // Validate lot nums
        if (lastLotNum < firstLotNum || lastLotNum < 0 || firstLotNum < 0) {
            throw new InputMismatchException("Invalid lot numbers. " +
                    "\nFirst: " + firstLotNum +
                    "\nLast: " + lastLotNum);
        } // else, continue

        HashMap<Integer, Lot> auctionLots = new HashMap<>();
        for (int i = firstLotNum; i < lastLotNum; i++) {
            if (allLots.get(i) != null) {
                throw new UsedLotRangeException("This lot number is already being used: " + i);
            } else {
                Lot lot = new Lot(auction, i);
                auctionLots.put(i, lot);
                allLots.put(i, lot);
            }
        }
        return auctionLots;
    }

    public HashMap<Integer, Lot> getAllLots() {
        return allLots;
    }
}
