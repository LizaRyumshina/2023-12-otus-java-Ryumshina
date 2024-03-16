package CashCassette;

import Banknote.Banknote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashCassette implements BaseCashCassette {
    private static final Logger logger = LoggerFactory.getLogger(CashCassette.class);
    private static int globalID = 1;
    private final int id;
    private final Banknote banknoteType;
    private int currCountBanknote = 0;
    private int idATM;
    private boolean insideATM = false;
    private final int maxCountBanknotes;

    public CashCassette(Banknote banknoteType, int maxBanknotes) {
        this.id = globalID;
        globalID++;
        logger.debug("Created Cassette id={}, banknoteType= {}, maxBanknotes = {}", this.id, banknoteType.toString(), maxBanknotes);
        if (maxBanknotes < 0) {
            throw new RuntimeException("The number of banknotes cannot be negative");
        }
        this.banknoteType = banknoteType;
        this.maxCountBanknotes = maxBanknotes;
    }

    @Override
    public void setOwnerATM(int idATM) {
        logger.debug("setOwnerATM for {} idATM= {}", this.id, idATM);
        if (insideATM) {
            throw new RuntimeException("The cartridge is already installed in the terminal");
        } else {
            this.idATM = idATM;
            insideATM = true;
        }
    }

    @Override
    public int getOwnerATM() {
        logger.debug("getOwnerATM of {} current idATM= {}", this.id, idATM);
        if (!insideATM) {
            throw new RuntimeException("The cartridge don't have owner");
        }
        return idATM;
    }

    public void removedFromATMCassette() {
        logger.debug("removedFromATMCassette from {} before remove idATM= {}", this.id, idATM);
        if (!insideATM) {
            throw new RuntimeException("An attempt to remove a non-existent owner.");
        }
        insideATM = false;
    }

    @Override
    public void addBanknotes(int count) {
        logger.debug("addBanknotes to {} count= {}, currCountBanknote = {}", this.id, count, currCountBanknote);
        if (count < 0) {
            throw new RuntimeException("A negative number of bills has been received. count=" + count);
        }
        if (currCountBanknote + count > maxCountBanknotes) {
            throw new RuntimeException("The number of bills exceeds the available space");
        }
        currCountBanknote += count;
    }

    @Override
    public void getBanknotes(int count) {
        logger.debug("getBanknotes from {}, count= {}, currCountBanknote = {}", this.id, count, currCountBanknote);
        if (count < 0) {
            throw new RuntimeException("A negative number of bills has been received. count=" + count);
        }
        if ((this.currCountBanknote - count) < 0) {
            throw new RuntimeException("The requested amount is less than zero. count=" + count + ", countBanknote=" + currCountBanknote);
        }
        currCountBanknote -= count;
    }

    @Override
    public int getMaxCountBanknotes() {
        return maxCountBanknotes;
    }

    public Banknote getBanknoteType() {
        return banknoteType;
    }

    public int getDenomination() {
        return banknoteType.getDenomination();
    }

    public int getCurrCountBanknote() {
        return currCountBanknote;
    }

    @Override
    public CashCassette clone() {
        CashCassette cashCassette = new CashCassette(getBanknoteType(), getMaxCountBanknotes());
        cashCassette.setOwnerATM(this.idATM);
        cashCassette.addBanknotes(this.currCountBanknote);
        return cashCassette;
    }

    @Override
    public String toString() {
        return "id=" + this.id + " " + getCurrCountBanknote() + "(" + getDenomination() + ")";
    }
    public boolean isInsideATM (){
        return insideATM;
    }
}
