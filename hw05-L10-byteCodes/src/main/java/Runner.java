import ru.piggyBank.MoneyBoxInterface;
import ru.piggyBank.ioc.Ioc;

public class Runner {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        MoneyBoxInterface piggyBank = Ioc.createPiggyBank();
        //for (int i =0; i<=100000; i++){
        piggyBank.addMoney(10);
        piggyBank.addMoney(25, 20);
        piggyBank.addMoney(10, 20, 25);
        piggyBank.withdrawMoney(10);
        System.out.println("Balance:"+piggyBank.getBalance());
        //}
        long delta = System.currentTimeMillis() - startTime;
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));
    }
}