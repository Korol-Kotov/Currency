package me.korolkotov.currency.commands;

import lombok.AllArgsConstructor;
import me.korolkotov.currency.Main;
import me.korolkotov.currency.economy.Account;
import me.korolkotov.currency.util.chat.ChatUtil;
import me.korolkotov.currency.util.chat.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MoneyCMD implements TabExecutor {

    private Main plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("money.admin")) {
            ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("not-enough-perms").asComponentList());
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("help", Map.of("%alias%", label)).asComponentList());
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 3) {
                ChatUtil.sendMessage(sender, getCommandUsage("give", label).asComponentList());
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                if (amount <= 0) {
                    ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("cant-add-negative").asComponentList());
                    return true;
                }
            } catch (NumberFormatException error) {
                ChatUtil.sendMessage(sender, getCommandUsage("give", label).asComponentList());
                return true;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[2]);
            if (player == null) {
                ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("player-not-found", Map.of("%player%", args[2])).asComponentList());
                return true;
            }

            Account account = plugin.getEconomyManager().getAccount(player);
            if (account == null) account = plugin.getEconomyManager().createAccount(player);

            account.add(amount);

            String name = (player.getName() == null) ? args[2] : player.getName();
            ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("given",
                    Map.of("%player%", name, "%currency%", plugin.getConfig().getString("currency.multiple", ""), "%amount%", amount + "")).asComponentList());
        } else if (args[0].equalsIgnoreCase("take")) {
            if (args.length < 3) {
                ChatUtil.sendMessage(sender, getCommandUsage("take", label).asComponentList());
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                if (amount <= 0) {
                    ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("cant-take-negative").asComponentList());
                    return true;
                }
            } catch (NumberFormatException error) {
                ChatUtil.sendMessage(sender, getCommandUsage("take", label).asComponentList());
                return true;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[2]);
            if (player == null) {
                ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("player-not-found", Map.of("%player%", args[2])).asComponentList());
                return true;
            }

            Account account = plugin.getEconomyManager().getAccount(player);
            if (account == null) account = plugin.getEconomyManager().createAccount(player);

            account.take(amount);

            String name = (player.getName() == null) ? args[2] : player.getName();
            ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("took",
                    Map.of("%player%", name, "%currency%", plugin.getConfig().getString("currency.multiple", ""), "%amount%", amount + "")).asComponentList());
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                ChatUtil.sendMessage(sender, getCommandUsage("set", label).asComponentList());
                return true;
            }

            double amount;
            try {
                amount = Double.parseDouble(args[1]);
                if (amount < 0) {
                    ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("cant-set-negative").asComponentList());
                    return true;
                }
            } catch (NumberFormatException error) {
                ChatUtil.sendMessage(sender, getCommandUsage("set", label).asComponentList());
                return true;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[2]);
            if (player == null) {
                ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("player-not-found", Map.of("%player%", args[2])).asComponentList());
                return true;
            }

            Account account = plugin.getEconomyManager().getAccount(player);
            if (account == null) account = plugin.getEconomyManager().createAccount(player);

            account.set(amount);

            String name = (player.getName() == null) ? args[2] : player.getName();
            ChatUtil.sendMessage(sender, plugin.getMessageUtil().getText("set",
                    Map.of("%player%", name, "%currency%", plugin.getConfig().getString("currency.multiple", ""), "%amount%", amount + "")).asComponentList());
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("money.admin")) return new ArrayList<>();

        if (args.length == 1) {
            return sortContains(List.of("help", "give", "take", "set"), args[0]);
        }
        if (args.length == 2) {
            if (List.of("give", "take", "set").contains(args[0].toLowerCase()))
                return sortContains(List.of("0", "10", "100", "1000"), args[1]);
        }
        if (args.length == 3) {
            if (List.of("give", "take", "set").contains(args[0].toLowerCase())) {
                List<String> players = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                return sortContains(players, args[2]);
            }
        }

        return new ArrayList<>();
    }

    private List<String> sortContains(List<String> list, String value) {
        List<String> result = new ArrayList<>();
        for (String string : list)
            if (string.toLowerCase().contains(value.toLowerCase()))
                result.add(string);
        return result;
    }

    private Text getCommandUsage(String command, String label) {
        return plugin.getMessageUtil().getText(command + "-usage", Map.of("%alias%", label));
    }
}
