package Banknote;

public enum Banknote {
    Denomination_100 {
        @Override
        public int getDenomination() {
            return 100;
        }
    },
    Denomination_500 {
        @Override
        public int getDenomination() {
            return 500;
        }
    },
    Denomination_1000 {
        @Override
        public int getDenomination() {
            return 1000;
        }
    },
    Denomination_5000 {
        @Override
        public int getDenomination() {
            return 5000;
        }
    };

    public abstract int getDenomination();
}
