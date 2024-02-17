package ru.piggyBank;

public interface MoneyBoxInterface {
    void addMoney(int amount);
    public void addMoney(int... amounts);
    void withdrawMoney(int amount);
    int getBalance();
}
