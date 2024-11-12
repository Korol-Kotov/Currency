package me.korolkotov.currency.economy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

@AllArgsConstructor
@Getter
public class Account {
    private OfflinePlayer player;
    private double balance;

    public boolean has(double amount) {
        return balance >= amount;
    }

    public void add(double amount) {
        if (amount <= 0) return;

        balance += amount;
    }

    public void take(double amount) {
        if (amount <= 0) return;

        if (amount >= balance) balance = 0;
        else balance -= amount;
    }

    public void set(double amount) {
        if (amount < 0) return;

        balance = amount;
    }
}
