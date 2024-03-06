package CashCassette;

import ATM.ATM;
import Banknote.Banknote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashCassetteTest {
    Banknote banknote;
    CashCassette cashCassette;
    @BeforeEach
    void createCashCassetteTest() {
        banknote = new Banknote(1000);
        cashCassette = new CashCassette(banknote, 100);
    }
    @Test
    void addRemoveCashCassetteTest() {
        int banknotes = 50;
        cashCassette.addBanknotes(banknotes);
        assertThat(cashCassette.getCurrCountBanknote()).isEqualTo(banknotes);

        cashCassette.getBanknotes(banknotes);
        assertThat(cashCassette.getCurrCountBanknote()).isEqualTo(0);

        assertThrows(Throwable.class, () -> {cashCassette.addBanknotes(-100);});
        assertThrows(Throwable.class, () -> {cashCassette.getBanknotes(-100);});
    }

    @Test
    void maxCountCashCassetteTest() {
        CashCassette cshCassette = new CashCassette(banknote, 10);
        int banknotes = 123;
        assertThrows(Throwable.class, () -> {
            cshCassette.addBanknotes(banknotes);
        });
    }

    @Test
    void lessZeroCashCassetteTest() {
        int banknotes = 123;
        assertThrows(Throwable.class, () -> {
            cashCassette.getBanknotes(banknotes);
        });
    }
    @Test
    void checkNoOwnerATMCashCassetteTest() {
        assertThrows(Throwable.class, () -> {cashCassette.getOwnerATM();});
    }

    @Test
    void checkChangeOwnerATMCashCassetteTest() {
        ATM atm = new ATM(0);
        cashCassette.setOwnerATM(atm.getId());
        assertThat(cashCassette.getOwnerATM()).isEqualTo(atm.getId()); // correct owner

        ATM atm2 = new ATM(0);
        assertThrows(Throwable.class, () -> {cashCassette.setOwnerATM(atm2.getId());}); // already has owner
        assertThat(cashCassette.getOwnerATM()).isEqualTo(atm.getId()); //check that it hasn't changed

        cashCassette.removedFromATMCassette();
        assertThrows(Throwable.class, () -> {cashCassette.getOwnerATM();}); // no owner
        assertThrows(Throwable.class, () -> {cashCassette.removedFromATMCassette();}); // no owner

        cashCassette.setOwnerATM(atm2.getId());
        assertThat(cashCassette.getOwnerATM()).isEqualTo(atm2.getId()); // correct owner
    }

}
