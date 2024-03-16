package ATM;

import Banknote.CounterBanknotes;
import CashCassette.CashCassette;

import java.util.List;

public interface HaveCassette {
    public void addCashCassette(CashCassette cashCassette);
    public CashCassette removeCashCassette(int position);
    public List<CounterBanknotes> withdrawCash(int amount);
    public int getMaxCountCassette();
}
