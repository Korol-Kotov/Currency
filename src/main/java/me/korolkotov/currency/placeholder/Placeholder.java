package me.korolkotov.currency.placeholder;

import lombok.AllArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.korolkotov.currency.Main;
import me.korolkotov.currency.economy.Account;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class Placeholder extends PlaceholderExpansion {
    private Main plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "money";
    }

    @Override
    public @NotNull String getAuthor() {
        return "KoroLKotov";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return null;

        Account account = plugin.getEconomyManager().getAccount(player);

        return (account == null) ? "0" : account.getBalance() + "";
    }
}
