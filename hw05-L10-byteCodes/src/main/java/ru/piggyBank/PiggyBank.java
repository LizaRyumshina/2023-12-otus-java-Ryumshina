package ru.piggyBank;

import ru.piggyBank.ioc.Log;

public class PiggyBank implements MoneyBoxInterface {
    private int balance;

    public PiggyBank() {
        balance = 0;
    }
    @Log
    public void addMoney(int amount1) {
        if (amount1 <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount1;
    }

    public void addMoney(int amount1, int amount2) {
        addMoney(amount1);
        addMoney(amount2);
    }

    @Log
    public void addMoney(int amount1, int amount2, int amount3) {
        addMoney(amount1);
        addMoney(amount2);
        addMoney(amount3);
    }
    @Log
    public void withdrawMoney(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Lack of funds");
        }
        balance -= amount;
    }
    @Log
    public int getBalance() {
        return balance;
    }
}