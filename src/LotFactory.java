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
    public class LotInUseException extends Exception {
        public LotInUseException(String message) {
            super(message);
        }
    }

    public LotFactory() {
        // empty constructor
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

    public void changeLotType(HashMap<Integer, Lot> allLots, int lotNum,
                              int newType, int[] args) throws LotInUseException {
        if (allLots == null) {
            throw new NullPointerException("list of lots is null");
        } else if (lotNum <= 0) {
            throw new InputMismatchException("lot number is not positive");
        } else if (allLots.get(lotNum).winningBidder() != 0) {
            throw new LotInUseException("It's too late to change this lot type");
        }

        Lot newLot = null;
        switch (newType) {
            case Lot.lotType -> newLot = new Lot(lotNum);
            case ReserveLot.lotType -> {
                if (args.length < 1 || args[0] < 0) {
                    throw new InputMismatchException("Bad arguments to create new lot");
                }
                newLot = new ReserveLot(lotNum, args[0]);
            }
            case DualMinLot.lotType -> {
                if (args.length < 3 || args[0] < 0 ||
                        args[1] < 0 || args[2] < 0) {
                    throw new InputMismatchException("Bad arguments to create new lot");
                }
                newLot = new DualMinLot(lotNum, args[0], args[1], args[2]);
            }
        }
        allLots.put(lotNum, newLot);
        this.allLots.put(lotNum, newLot);
    }

    public HashMap<Integer, Lot> getAllLots() {
        return allLots;
    }
}