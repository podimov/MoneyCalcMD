package com.podimov.moneycalcmd;

public class Bank {
    private String bank_name;

    public Bank() {
    }

    public Bank(String bank_name) {
        this.bank_name = bank_name;
    }

    @Override
    public String toString() {
        return bank_name;
    }
}
