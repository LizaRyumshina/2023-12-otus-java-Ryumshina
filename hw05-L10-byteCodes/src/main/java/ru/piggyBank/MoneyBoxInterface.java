package ru.piggyBank;

public interface MoneyBoxInterface {
    void addMoney(int amount1);
    void addMoney(int amount1, int amount2);
    void addMoney(int amount1, int amount2, int amount3);
    //public void addMoney(int... amounts);
    void withdrawMoney(int amount);
    int getBalance();
}
