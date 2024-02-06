package BaseObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardManager { //"Посредник"
    private Map<Integer, List<Card>> customerCards;

    public CardManager() {
        customerCards = new HashMap<>();
    }
    public void addCardToCustomer(Customer customer, Card card) {
        List<Card> cards = customerCards.computeIfAbsent(customer.getId(), k -> new ArrayList<>());
        cards.add(card);
    }
    public List<Card> getCustomerCards(Customer customer) {
        int customerId = customer.getId();
        return customerCards.getOrDefault(customerId, new ArrayList<>());
    }
}
