package Banknote;

public class CounterBanknotes{
    private final Banknote banknoteType;
    private int count = 0;

    public CounterBanknotes(Banknote banknoteType, int count) {
        if (banknoteType == null){
            throw new RuntimeException("Empty banknoteType");
        }
        this.banknoteType = banknoteType;
        if (count < 0) {
            throw new RuntimeException("The count is less than zero. denomination:" + banknoteType.getDenomination());
        }
        this.count = count;
    }

    public void addBanknotes(int banknotesCount) {
        if (banknotesCount < 0) {
            throw new RuntimeException("A negative number of bills has been received. banknotesCount=" + banknotesCount);
        }
        if (count + banknotesCount > count) {
            throw new RuntimeException("The number of bills exceeds the available space");
        }
        count += banknotesCount;
    }

    public void getBanknotes(int banknotesCount) {
        if (banknotesCount < 0) {
            throw new RuntimeException("A negative number of bills has been received. banknotesCount=" + banknotesCount);
        }
        if ((this.count - banknotesCount) < 0) {
            throw new RuntimeException("The requested amount is less than zero. banknotesCount=" + banknotesCount + ", countBanknote=" + count);
        }
        count -= banknotesCount;
    }

    public int getAllBanknotes() {
        int result = count;
        count = 0;
        return result;
    }

    public int countBanknotes() {
        return count;
    }
    public int getDenomination(){
        return this.banknoteType.getDenomination();
    }

    @Override
    public String toString() {
        return countBanknotes() + " (" + banknoteType.getDenomination() + ")";
    }
}