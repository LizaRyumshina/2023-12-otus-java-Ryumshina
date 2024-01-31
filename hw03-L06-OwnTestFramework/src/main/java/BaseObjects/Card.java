package BaseObjects;

import BaseObjects.parent.BaseObject;

public class Card extends BaseObject {
    private static int nextId = 0;
    private String PAN;
    public Card() {
        super(); // вызов конструктора родительского класса BaseObject
    }
    @Override
    public String toString() {
        return "id="+this.getId()+", PAN="+getFormatedPan();
    }

    @Override
    public int getNextId() {
        return ++nextId;
    }
    public void setAnyPan(){
        this.PAN =  String.format("22%018d", this.getId());
    }
    public String getPAN(){return PAN;};
    public String getFormatedPan(){
        StringBuilder result = new StringBuilder(PAN.substring(1, 5));
        for (int i = 4; i < 20; i += 4) {
            result.append(" ");
            result.append(PAN.substring(i, i + 4));
        }
        return result.toString();
    }
}
