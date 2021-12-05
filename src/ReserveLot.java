import java.util.HashMap;

public class ReserveLot extends Lot{
    private int reserveValue;

    public ReserveLot(Auction auction, int lotNumber, int reserve) {
        super(auction, lotNumber);
        this.reserveValue = reserve;
    }

    public int getReserveValue() {
        return reserveValue;
    }
}
