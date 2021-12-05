public class ReserveLot extends Lot{
    private int reserveValue;

    // Type of lot (so it is not confused with other types of lots)
    public final static int lotType = 1;

    public ReserveLot(int lotNumber, int reserve) {
        super(lotNumber);
        this.reserveValue = reserve;
    }

    public int getReserveValue() {
        return reserveValue;
    }
}
