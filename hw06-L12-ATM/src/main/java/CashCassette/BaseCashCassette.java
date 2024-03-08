package CashCassette;

public interface BaseCashCassette {
    public void setOwnerATM (int idATM);
    public int getOwnerATM ();
    public void addBanknotes(int banknotesCount);
    public void getBanknotes(int banknotesCount);
    public int getMaxCountBanknotes();
    public CashCassette clone();
}
