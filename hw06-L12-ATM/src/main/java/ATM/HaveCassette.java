package ATM;

import Banknote.StackBanknotes;
import CashCassette.CashCassette;

import java.util.List;

public interface HaveCassette {
    public void addCashCassette(CashCassette cashCassette);
    public CashCassette removeCashCassette(int position);
    public List<StackBanknotes> withdrawCash(int amount);
    public int getMaxCountCassette();
}
