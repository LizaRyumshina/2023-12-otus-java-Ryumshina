package BaseObjects;

import TestFramework.After;
import TestFramework.Before;
import TestFramework.Test;

import java.util.regex.Pattern;

public class TF_Card {
    Card card;
    @Before
    public void testbefore(){
        card = new Card();
        card.setAnyPan();
        //System.out.println("before card");
    }
    @Test(hint = "Compare card ID")
    public void testCompareCardId(){
        Card card2 = new Card();
        card2.setAnyPan();
        if (card.getId()==card2.getId()){
            throw new RuntimeException("The same card ID.");
        }
    }
    @Test(hint = "Check PAN")
    public void testChekcPan(){
        if (!Pattern.matches("\\d{20}", card.getPAN())){
            throw new RuntimeException("The PAN contains characters other than numbers.");
        }
        else if (!Pattern.matches("\\d{4} \\d{4} \\d{4} \\d{4} \\d{4}", card.getFormatedPan())){
            System.out.println("\""+card.getFormatedPan()+"\"");
            throw new RuntimeException("Wrong PAN format.");
        }
    }
    @Test(hint = "Check Creating Date")
    public void testCheckCreatingDate(){
        if(card.getCreateDate()==null){
            throw new RuntimeException("Wrong create date: "+ card.getCreateDate());
        }
    }
    @After
    public void testAfter(){
        card = null;
        //System.out.println("after card");
    }
}
