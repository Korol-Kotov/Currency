package me.korolkotov.currency.economy;

import lombok.AllArgsConstructor;
import me.korolkotov.currency.Main;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class VaultHandler implements Economy {
    private Main plugin;
    private final Plugin vault;

    @Override
    public boolean isEnabled() {
        return vault.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return amount + "";
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("currency.multiple");
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("currency.single");
    }

    @Override
    public boolean hasAccount(String playerName) {
        return plugin.getEconomyManager().hasAccount(Bukkit.getOfflinePlayerIfCached(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return plugin.getEconomyManager().hasAccount(player);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        Account account = plugin.getEconomyManager().getAccount(player);
        return (account == null) ? 0 : account.getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        Account account = plugin.getEconomyManager().getAccount(player);
        return (account == null) ? 0 : account.getBalance();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        Account account = plugin.getEconomyManager().getAccount(player);
        return (account == null) ? 0 >= amount : account.has(amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        Account account = plugin.getEconomyManager().getAccount(player);
        return (account == null) ? 0 >= amount : account.has(amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        Account account = plugin.getEconomyManager().getAccount(player);
        if (amount <= 0) return new EconomyResponse(
                amount,
                (account == null) ? 0 : account.getBalance(),
                EconomyResponse.ResponseType.FAILURE,
                plugin.getMessageUtil().getText("cant-take-negative").asString()
        );

        if (account == null) return new EconomyResponse(
                amount,
                0,
                EconomyResponse.ResponseType.FAILURE,
                plugin.getMessageUtil().getText("not-have-money", Map.of("%player%", playerName)).asString()
        );

        account.take(amount);
        return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Account account = plugin.getEconomyManager().getAccount(player);
        String name = (player.getName() == null) ? "Undefined" : player.getName();
        if (amount <= 0) return new EconomyResponse(
                amount,
                (account == null) ? 0 : account.getBalance(),
                EconomyResponse.ResponseType.FAILURE,
                plugin.getMessageUtil().getText("cant-take-negative").asString()
        );

        if (account == null) return new EconomyResponse(
                amount,
                0,
                EconomyResponse.ResponseType.FAILURE,
                plugin.getMessageUtil().getText("not-have-money", Map.of("%player%", name)).asString()
        );

        account.take(amount);
        return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        Account account = plugin.getEconomyManager().getAccount(player);

        if (amount <= 0) return new EconomyResponse(
                amount,
                (account == null) ? 0 : account.getBalance(),
                EconomyResponse.ResponseType.FAILURE,
                plugin.getMessageUtil().getText("cant-add-negative").asString()
        );

        if (account == null) account = plugin.getEconomyManager().createAccount(player);

        account.add(amount);

        return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Account account = plugin.getEconomyManager().getAccount(player);

        if (amount <= 0) return new EconomyResponse(
                amount,
                (account == null) ? 0 : account.getBalance(),
                EconomyResponse.ResponseType.FAILURE,
                plugin.getMessageUtil().getText("cant-add-negative").asString()
        );

        if (account == null) account = plugin.getEconomyManager().createAccount(player);

        account.add(amount);

        return new EconomyResponse(amount, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        String errorMessage = plugin.getName() + "Плагин не поддерживает банки!";
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);

        if (!plugin.getEconomyManager().hasAccount(player))
            plugin.getEconomyManager().createAccount(player);

        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (!plugin.getEconomyManager().hasAccount(player))
            plugin.getEconomyManager().createAccount(player);

        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
