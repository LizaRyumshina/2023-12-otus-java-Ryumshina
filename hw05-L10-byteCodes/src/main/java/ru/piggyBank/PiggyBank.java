package ru.piggyBank;

import ru.piggyBank.ioc.Log;

public class PiggyBank implements MoneyBoxInterface {
    private int balance;

    public PiggyBank() {
        balance = 0;
    }
    @Log
    public void addMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }
    @Log
    public void withdrawMoney(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Lack of funds");
        }
        balance -= amount;
    }

    @Log
    public void addMoney(int... amounts) {
        for(int amount: amounts){
            addMoney(amount);
        }
    }

    public int getBalance() {
        return balance;
    }
}