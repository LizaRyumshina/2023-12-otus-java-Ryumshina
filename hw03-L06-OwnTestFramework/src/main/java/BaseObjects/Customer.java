package BaseObjects;

import BaseObjects.parent.BaseObject;
public class Customer extends BaseObject {
    private static int nextId = 0;
    private String name;
    public Customer(String name) {
        super(); // вызов конструктора родительского класса
        this.name = name;
    }

    @Override
    public String toString() {
        return "id="+this.getId()+", name="+this.name;
    }

    @Override
    protected int getNextId() {
        return ++nextId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
