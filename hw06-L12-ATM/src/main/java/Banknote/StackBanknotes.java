package Banknote;

public class StackBanknotes extends Banknote{
    private int count = 0;

    public StackBanknotes(int denomination, int count) {
        super(denomination);
        if (count <0){
            throw new RuntimeException("The denomination is less than zero. denomination:"+denomination);
        }
        this.count = count;
    }

    public void addBanknotes(int banknotesCount) {
        if (banknotesCount<0){
            throw new RuntimeException("A negative number of bills has been received. banknotesCount="+banknotesCount);
        }
        if (count+banknotesCount > count){
            throw new RuntimeException("The number of bills exceeds the available space");
        }
        count +=banknotesCount;
    }

    public void getBanknotes(int banknotesCount) {
        if (banknotesCount<0){
            throw new RuntimeException("A negative number of bills has been received. banknotesCount="+banknotesCount);
        }
        if ((this.count - banknotesCount)<0) {
            throw new RuntimeException("The requested amount is less than zero. banknotesCount="+ banknotesCount+", countBanknote="+count);
        }
        count-=banknotesCount;
    }

    public int getAllBanknotes() {
        int result = count;
        count = 0;
        return result;
    }

    public int countBanknotes() {
        return count;
    }

    @Override
    public String toString() {
        return countBanknotes()+" ("+ getDenomination()+")";
    }
}
