package Banknote;

public class Banknote implements BaseBanknote {
    private final int denomination;

    public Banknote(int denomination) {
        if (denomination <0){
            throw new RuntimeException("The denomination is less than zero. denomination:"+denomination);
        }
        this.denomination = denomination;
    }
    @Override
    public int getDenomination() {
        return denomination;
    }
    public Banknote clone(){
        return new Banknote(this.denomination);
    }

    @Override
    public String toString() {
        return String.valueOf(this.denomination);
    }
}
