package BaseObjects.parent;

import java.util.Date;

public abstract class BaseObject {
    private int id;
    private Date createDate;
    public BaseObject() {
        this.id = getNextId();
        this.createDate = new Date();
    }
    protected abstract int getNextId();
    public int getId() {
        return this.id;
    }
    protected void SetId(int id) {
        this.id = id;
    }
    public Date getCreateDate() {
        return createDate;
    }
}
