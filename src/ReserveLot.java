public class ReserveLot extends Lot{
    private int reserveValue;

    public ReserveLot(int lotNumber, int reserve) {
        super(lotNumber);
        this.reserveValue = reserve;
    }

    public int getReserveValue() {
        return reserveValue;
    }
}
