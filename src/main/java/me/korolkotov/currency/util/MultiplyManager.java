package me.korolkotov.currency.util;

import lombok.Getter;
import me.korolkotov.currency.Main;
import me.korolkotov.currency.economy.Account;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Getter
public class MultiplyManager {
    private final BukkitTask task;

    public MultiplyManager(Main plugin) {
        long time = 20L * 60 * plugin.getConfig().getInt("multiply.time");

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isCancelled()) return;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (hasPermission(player)) {
                        double multiply = getMultiply(player);
                        double amount = plugin.getConfig().getDouble("multiply.amount");

                        Account account = plugin.getEconomyManager().getAccount(player);
                        if (account == null) account = plugin.getEconomyManager().createAccount(player);

                        account.add(multiply * amount);
                    }
                }
            }
        }.runTaskTimer(plugin, time, time);
    }

    private boolean hasPermission(Player player) {
        for (PermissionAttachmentInfo permissionInfo : player.getEffectivePermissions()) {
            if (permissionInfo.getValue()) {
                if (permissionInfo.getPermission().startsWith("money."))
                    return true;
            }
        }

        return false;
    }

    private double getMultiply(Player player) {
        double max = 0;
        for (PermissionAttachmentInfo permissionInfo : player.getEffectivePermissions()) {
            if (permissionInfo.getValue()) {
                String permission = permissionInfo.getPermission();
                if (permission.startsWith("money.")) {
                    try {
                        double multiply = Double.parseDouble(permission.replace("money.", ""));
                        if (multiply > max) max = multiply;
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return max;
    }
}
