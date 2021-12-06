import java.util.HashMap;
import java.util.InputMismatchException;

public class LotFactory {
    HashMap<Integer, Lot> allLots;

    // Exception thrown when lot ranges not unique
    public static class UsedLotRangeException extends Exception {
        public UsedLotRangeException(String message) {
            super(message);
        }
    }
    // Exception thrown when a bidder has already bid on a lot, and it can't be changed
    public static class LotInUseException extends Exception {
        public LotInUseException(String message) {
            super(message);
        }
    }

    public LotFactory() {
        allLots = new HashMap<>();
    }

    /**
     * Creates the HashMap of unique lots for an auction to use
     * @param firstLotNum first lot number in range of lots to be created
     * @param lastLotNum last lot number in range of lots to be created
     * @return lots HashMap of unique lots
     * @throws UsedLotRangeException if another auction is using at least one
     *                               of these lots
     */
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

    /**
     * Changes a specific lot in an auction's HashMap of lots. The auction must
     * still manually link itself to the returned Lot (using Auction.replaceLot())
     * @param auctionLots The auction's HashMap of lots
     * @param lotNum the specific lot number
     * @param newType What type to replace it with.
*                     Must be Lot.lotType (or one of its subclasses)
     * @param args Arguments to pass to the new Lot's constructor
     * @return newLot the created lot
     * @throws LotInUseException if this lot number has already been bid on
     */
    public Lot changeLotType(HashMap<Integer, Lot> auctionLots, int lotNum,
                              int newType, int[] args) throws LotInUseException {
        if (auctionLots == null) {
            throw new NullPointerException("list of lots is null");
        } else if (lotNum <= 0) {
            throw new InputMismatchException("lot number is not positive");
        } else if (auctionLots.get(lotNum).winningBidder() != 0) {
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
        auctionLots.put(lotNum, newLot);
        allLots.put(lotNum, newLot);
        return newLot;
    }

    public HashMap<Integer, Lot> getAllLots() {
        return allLots;
    }
}