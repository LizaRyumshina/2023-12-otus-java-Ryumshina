package ATM;

import CashCassette.CashCassette;
import Banknote.Banknote;
import Banknote.CounterBanknotes;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ATMTest {
    ATM atm;

    @BeforeEach
    void createATMBeforeEach() {
        atm = new ATM(4);
    }

    @Test
    @Name("принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка")
    void putBanknoteATMTest() {
        // нет кассет
        assertThrows(Throwable.class, () -> {
            atm.depositCash(Banknote.Denomination_5000, 1);
        });

        // добавили кассету для наминала 1000 и 10 купюр
        CashCassette cassette = new CashCassette(Banknote.Denomination_1000, 2000);
        atm.addCashCassette(cassette);
        int addedBanknote1 = 10;
        atm.depositCash(Banknote.Denomination_1000, addedBanknote1);
        assertThat(atm.getBalanceCash()).isEqualTo(addedBanknote1 * Banknote.Denomination_1000.getDenomination());

        // нет подходящей кассеты для номинала 5000
        assertThrows(Throwable.class, () -> {
            atm.depositCash(Banknote.Denomination_5000, 1);
        });

        // добавили кассету для наминала 5000 и 5 купюр
        CashCassette cassette2 = new CashCassette(Banknote.Denomination_5000, 2000);
        atm.addCashCassette(cassette2);
        int addedBanknote2 = 5;
        atm.depositCash(Banknote.Denomination_5000, addedBanknote2);
        assertThat(atm.getBalanceCash()).isEqualTo(addedBanknote1 * Banknote.Denomination_1000.getDenomination() + addedBanknote2 * Banknote.Denomination_5000.getDenomination());
    }

    @Test
    @Name("выдавать запрошенную сумму минимальным количеством банкнот или ошибку, если сумму нельзя выдать")
    void getBanknoteATMTest() {
        // нет кассет
        assertThrows(Throwable.class, () -> {
            atm.withdrawCash(123);
        });

        // добавили кассету для наминала 100 и 100 купюр
        CashCassette cassette = new CashCassette(Banknote.Denomination_100, 2000);
        atm.addCashCassette(cassette);
        int addedBanknote1 = 100;
        atm.depositCash(Banknote.Denomination_100, addedBanknote1);
        int balance = addedBanknote1 * Banknote.Denomination_100.getDenomination();

        //пытаемся получить сумму меньше минимального наминала
        assertThrows(Throwable.class, () -> {
            atm.withdrawCash(50);
        });

        //получаем 5 купюр по 100
        atm.withdrawCash(500);
        balance -= 500;
        assertThat(atm.getBalanceCash()).isEqualTo(balance);

        //добавляем кассету для наминала 5000 и 100 купюр
        CashCassette cassette2 = new CashCassette(Banknote.Denomination_5000, 2000);
        atm.addCashCassette(cassette2);
        int addedBanknote2 = 100;
        atm.depositCash(Banknote.Denomination_5000, addedBanknote2);
        balance += Banknote.Denomination_5000.getDenomination() * addedBanknote2; //balance = 509500

        // получаем 80 купюр наминалом 5000 и 11 наминалом 100
        List<CounterBanknotes> counterBanknotes = atm.withdrawCash(401100);
        //System.out.println(counterBanknotes);
        balance -= 401100;
        assertThat(atm.getBalanceCash()).isEqualTo(balance);
    }

    @Test
    @Name("выдавать сумму остатка денежных средств")
    void getBalanceCashATMTest() {
        // нет кассет
        assertThat(atm.getBalanceCash()).isEqualTo(0);

        // 1 кассета
        CashCassette cassette = new CashCassette(Banknote.Denomination_1000, 2000);
        int addedBanknote1 = 100;
        cassette.addBanknotes(addedBanknote1);
        atm.addCashCassette(cassette);
        assertThat(atm.getBalanceCash()).isEqualTo(addedBanknote1 * Banknote.Denomination_1000.getDenomination());

        // 2 кассеты разных наминалов
        CashCassette cassette2 = new CashCassette(Banknote.Denomination_100, 2000);
        int addedBanknote2 = 2000;
        cassette2.addBanknotes(addedBanknote2);
        atm.addCashCassette(cassette2);
        assertThat(atm.getBalanceCash()).isEqualTo(addedBanknote1 * Banknote.Denomination_1000.getDenomination() + addedBanknote2 * Banknote.Denomination_100.getDenomination());

    }

    @Test
    void addRemoveCassetteATMTest() {
        CashCassette cassette1 = new CashCassette(Banknote.Denomination_1000, 100);
        atm.addCashCassette(cassette1);
        assertThrows(Throwable.class, () -> {
            atm.addCashCassette(cassette1);
        }); //already installed

        assertDoesNotThrow(()->{
            atm.addCashCassette(new CashCassette(Banknote.Denomination_1000, 10));
            atm.addCashCassette(new CashCassette(Banknote.Denomination_1000, 20));
            atm.addCashCassette(new CashCassette(Banknote.Denomination_1000, 30));
            atm.removeCashCassette(0);});
    }
}
