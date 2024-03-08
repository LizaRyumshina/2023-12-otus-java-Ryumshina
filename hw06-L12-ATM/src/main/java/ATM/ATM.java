package ATM;

import Banknote.Banknote;
import Banknote.CounterBanknotes;
import CashCassette.CashCassette;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ATM implements HaveCassette {
    private static final Logger logger = LoggerFactory.getLogger(ATM.class);
    private static int globalID = 1;
    private final int id;
    private final int maxCountCassette;
    private CashCassette[] cashCassettes;

    public ATM(int maxCountCassette) {
        this.id = globalID;
        globalID++;
        logger.debug("create {}, maxCountCassette = {}", this.id, maxCountCassette);
        this.maxCountCassette = maxCountCassette;
        this.cashCassettes = new CashCassette[maxCountCassette];
    }

    public int getId() {
        return this.id;
    }

    @Override
    public void addCashCassette(CashCassette cashCassette) {
        logger.debug("addCashCassette into {}, cashCassette = {}", this.id, cashCassette.toString());
        int position = findEmptyPositionCassette();
        cashCassette.setOwnerATM(this.getId());
        cashCassettes[position] = cashCassette.clone();
    }

    private int findEmptyPositionCassette() {
        logger.debug("findEmptyPositionCassette inside {}", this.id);
        for (int i = 0; i < maxCountCassette; i++) {
            if (!isFilledPlaceCassette(i)) {
                return i;
            }
        }
        throw new RuntimeException("There are no free place for new cassette.");
    }

    @Override
    public CashCassette removeCashCassette(int position) {
        logger.debug("removeCashCassette from {}, position={}", this.id, position);
        if (isFilledPlaceCassette(position)) {
            CashCassette cashCassette =  cashCassettes[position].clone();
            cashCassettes[position] = null;
            return cashCassette;
        } else {
            throw new RuntimeException("There is no cassette in the position =" + position);
        }
    }

    public boolean[] getFilledCashCassettes() {
        boolean[] isFilledPlaceCassette = new boolean[maxCountCassette];
        for (int i = 0; i < maxCountCassette; i++) {
            if (cashCassettes[i] == null) {
                isFilledPlaceCassette[i] = false;
            } else {
                isFilledPlaceCassette[i] = cashCassettes[i].isInsideATM();
            }
        }
        return isFilledPlaceCassette;
    }

    public CashCassette getCashCassettes(int position) {
        logger.debug("getCashCassettes from {}, position={}", this.id, position);
        if (isFilledPlaceCassette(position)) {
            return cashCassettes[position].clone();
        } else {
            throw new RuntimeException("There is no cassette in the position =" + position);
        }
    }

    private boolean isFilledPlaceCassette(int position) {
        logger.debug("isFilledPlaceCassette inside {}, position={}", this.id, position);
        if (position < 0 && position >= maxCountCassette) {
            throw new RuntimeException("Wrong position =" + position);
        }
        if (cashCassettes[position] == null) {
            return false;
        }
        return cashCassettes[position].isInsideATM();
    }

    @Override
    public List<CounterBanknotes> withdrawCash(int amount) {
        logger.debug("withdrawCash inside {}, position={}", this.id, amount);
        if (amount < 0) {
            throw new RuntimeException("A negative amount has been received. amount=" + amount);
        }
        int remainingAmount = amount;
        List<CounterBanknotes> counterBanknotes = new ArrayList<>();
        List<CashCassette> sortedByDenominationCassettes = new ArrayList<>();
        for (int i = 0; i < maxCountCassette; i++) {
            if (isFilledPlaceCassette(i)) {
                sortedByDenominationCassettes.add(cashCassettes[i]);
            }
        }
        sortedByDenominationCassettes.sort((c1, c2) -> Integer.compare(c2.getDenomination(), c1.getDenomination()));
        for (CashCassette cassette : sortedByDenominationCassettes) {
            int banknotesCount = remainingAmount / cassette.getDenomination();
            if (banknotesCount > cassette.getCurrCountBanknote()) {
                banknotesCount = cassette.getCurrCountBanknote();
            }

            if (banknotesCount > 0) {
                counterBanknotes.add(new CounterBanknotes(cassette.getBanknoteType(), banknotesCount));
                remainingAmount -= banknotesCount * cassette.getDenomination();
            }
        }
        if (remainingAmount != 0) {
            throw new RuntimeException("Not enough suitable banknotes");
        }
        for (CounterBanknotes banknote : counterBanknotes) {
            for (CashCassette cassettes : sortedByDenominationCassettes) {
                if (cassettes.getDenomination() == banknote.getDenomination()) {
                    cassettes.getBanknotes(banknote.countBanknotes());
                    break;
                }
            }
        }
        logger.debug("withdrawCash inside {}, counterBanknotes={}", this.id, counterBanknotes.toString());
        return counterBanknotes;
    }

    public void depositCash(Banknote banknote, int count) {
        logger.debug("depositCash inside {}, banknote={}, count={}", this.id, banknote.toString(), count);
        for (int i = 0; i < maxCountCassette; i++) {
            if (isFilledPlaceCassette(i) && cashCassettes[i].getDenomination() == banknote.getDenomination()) {
                cashCassettes[i].addBanknotes(count);
                return;
            }
        }
        throw new RuntimeException("Could not find a matching cassette!");
    }

    @Override
    public int getMaxCountCassette() {
        return maxCountCassette;
    }

    public int getBalanceCashCassette(int position) {
        logger.debug("getBalanceCashCassette of {}, position={}", this.id, position);
        if (isFilledPlaceCassette(position)) {
            CashCassette cassette = getCashCassettes(position);
            logger.debug("getBalanceCashCassette of {} is {}", this.id, cassette.getCurrCountBanknote() * cassette.getDenomination());
            return cassette.getCurrCountBanknote() * cassette.getDenomination();
        } else {
            logger.debug("getBalanceCashCassette of {} is 0", this.id);
            return 0;
        }
    }

    public int getBalanceCash() {
        logger.debug("getBalanceCash of {}", this.id);
        int vBalance = 0;
        for (int i = 0; i < maxCountCassette; i++) {
            vBalance += getBalanceCashCassette(i);
        }
        return vBalance;
    }
}