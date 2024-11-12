package me.korolkotov.currency.economy;

import lombok.AllArgsConstructor;
import me.korolkotov.currency.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class EconomyManager {
    private final List<Account> accounts = new ArrayList<>();

    private Main plugin;

    @Nullable
    public Account getAccount(OfflinePlayer player) {
        if (player == null) return null;

        for (Account account : accounts)
            if (account.getPlayer().getUniqueId() == player.getUniqueId())
                return account;

        return null;
    }

    public boolean hasAccount(OfflinePlayer player) {
        return getAccount(player) != null;
    }

    public Account createAccount(OfflinePlayer player) {
        if (hasAccount(player)) return getAccount(player);

        Account account = new Account(player, 0);
        accounts.add(account);

        return account;
    }

    public void loadAccounts() {
        accounts.clear();
        int amount = 0;

        if (plugin.getAccounts().contains("accounts")) {
            for (String name : plugin.getAccounts().getConfigurationSection("accounts").getKeys(false)) {
                if (name == null) continue;

                OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
                if (player != null) {
                    double balance = plugin.getAccounts().getDouble("accounts." + name);
                    accounts.add(new Account(player, balance));
                    amount++;
                }
            }
        }

        plugin.getLogger().info("Loaded " + amount + " accounts!");
    }

    public void saveAccounts() {
        plugin.getAccounts().set("accounts", null);
        int amount = 0;

        if (!accounts.isEmpty()) {
            for (Account account : accounts) {
                String name = account.getPlayer().getName();
                plugin.getAccounts().set("accounts." + name, account.getBalance());
                amount++;
            }

            try {
                plugin.getAccounts().save(new File(plugin.getDataFolder(), "accounts.yml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        plugin.getLogger().info("Saved " + amount + " accounts!");
    }
}
