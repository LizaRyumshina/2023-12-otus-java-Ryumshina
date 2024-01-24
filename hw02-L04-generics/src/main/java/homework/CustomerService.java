package homework;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private TreeMap<Customer, String> map  = new TreeMap<>((o1, o2) -> Long.compare( o1.getScores(), o2.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = map.firstEntry();
        return new AbstractMap.SimpleEntry<>(firstEntry.getKey().getClone(), firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = map.higherEntry(customer);
        if (higherEntry == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(higherEntry.getKey().getClone(), higherEntry.getValue());
    }
    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
