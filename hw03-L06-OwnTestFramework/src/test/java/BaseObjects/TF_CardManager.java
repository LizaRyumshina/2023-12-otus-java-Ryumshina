package BaseObjects;

import TestFramework.After;
import TestFramework.Before;
import TestFramework.Test;

public class TF_CardManager {
    CardManager cardManager;
    @Before
    public void testbefore(){
        cardManager = new CardManager();
    }
    @Test(hint = "Check add card to customer")
    public void testCheckAddCardToCustomer(){
        Customer customer1 = new Customer("Liza");
        Card card1 = new Card();
        card1.setAnyPan();
        Card card2 = new Card();
        card2.setAnyPan();
        Card card3 = new Card();
        card3.setAnyPan();
        cardManager.addCardToCustomer(customer1, card1);
        cardManager.addCardToCustomer(customer1, card2);
        cardManager.addCardToCustomer(customer1, card3);
        if (cardManager.getCustomerCards(customer1).size() !=3){
            throw new RuntimeException("Incorrect card count for customer");
        }
    }
    @Test(hint = "Check add null card to customer")
    public void CheckEmptyCard(){
        Customer customer2 = new Customer("Anna");
        try{
            cardManager.addCardToCustomer(customer2, null);
            cardManager.addCardToCustomer(customer2, null);
            cardManager.addCardToCustomer(customer2, null);
        }
        catch (Exception e){}
        if (cardManager.getCustomerCards(customer2).size()>0){
            throw new RuntimeException("Added NULL card to customer.");
        }
    }
    @After
    public void testAfter(){
        cardManager = null;
    }
}
