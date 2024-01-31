package BaseObjects;

import TestFramework.After;
import TestFramework.Before;
import TestFramework.Test;

import java.util.Objects;

public class TF_Customer {
    Customer customer;
    @Before
    public void testbefore(){
        customer = new Customer("Liza");
    }
    @Test(hint = "Check Creating Date")
    public void testCheckCreatingDate(){
        if(customer.getCreateDate()==null){
            throw new RuntimeException("Wrong create date: "+ customer.getCreateDate());
        }
    }
    @Test(hint = "Check Change Name")
    public void testCheckChangeName(){
        String name = "Kirill";
        customer.setName(name);
        if(! Objects.equals(customer.getName(), name)){
            throw new RuntimeException("Wrong Name: \""+ customer.getName()+"\" expected: \""+name+"\"");
        }
    }
    @After
    public void testAfter(){
        customer = null;
    }
}
