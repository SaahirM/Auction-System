import java.util.HashMap;
import java.util.InputMismatchException;

public class LotFactory {
    HashMap<Integer, Lot> allLots = new HashMap<>();

    //Exception thrown when lot ranges not unique
    public class UsedLotRangeException extends Exception {
        public UsedLotRangeException(String message) {
            super(message);
        }
    }

    public LotFactory() {

    }

    public HashMap<Integer, Lot> createLots(int firstLotNum, int lastLotNum)
                                            throws UsedLotRangeException {
        // Validate lot nums
        if (lastLotNum < firstLotNum || lastLotNum < 0 || firstLotNum < 0) {
            throw new InputMismatchException("Invalid lot numbers. " +
                    "\nFirst: " + firstLotNum +
                    "\nLast: " + lastLotNum);
        } else {
            // Check they are unique
            for (int i = firstLotNum; i <= lastLotNum; i++) {
                if (allLots.get(i) != null) {
                    throw new UsedLotRangeException("This lot number is already being used: " + i);
                }
            }
        }

        HashMap<Integer, Lot> auctionLots = new HashMap<>();
        for (int i = firstLotNum; i <= lastLotNum; i++) {
            Lot lot = new Lot(i);
            auctionLots.put(i, lot);
            allLots.put(i, lot);
        }
        return auctionLots;
    }

    public HashMap<Integer, Lot> getAllLots() {
        return allLots;
    }
}
