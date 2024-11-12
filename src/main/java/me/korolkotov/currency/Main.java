package me.korolkotov.currency;

import lombok.Getter;
import me.korolkotov.currency.commands.MoneyCMD;
import me.korolkotov.currency.economy.EconomyManager;
import me.korolkotov.currency.economy.VaultHandler;
import me.korolkotov.currency.placeholder.Placeholder;
import me.korolkotov.currency.util.MultiplyManager;
import me.korolkotov.currency.util.chat.MessageUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class Main extends JavaPlugin {

    private MessageUtil messageUtil;
    private EconomyManager economyManager;
    private MultiplyManager multiplyManager;

    private YamlConfiguration config;
    private YamlConfiguration messages;
    private YamlConfiguration accounts;

    @Override
    public void onEnable() {
        loadFiles();

        this.messageUtil = new MessageUtil(this);
        this.economyManager = new EconomyManager(this);
        this.multiplyManager = new MultiplyManager(this);

        this.economyManager.loadAccounts();

        setupVault();

        new Placeholder(this).register();

        getCommand("money").setExecutor(new MoneyCMD(this));

        getLogger().info("Plugin " + getName() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (this.economyManager != null) this.economyManager.saveAccounts();
        if (this.multiplyManager != null) this.multiplyManager.getTask().cancel();
        getLogger().info("Plugin " + getName() + " disabled!");
    }

    private void loadFiles() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) saveResource("config.yml", false);
        config = YamlConfiguration.loadConfiguration(configFile);

        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) saveResource("messages.yml", false);
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        File accountsFile = new File(getDataFolder(), "accounts.yml");
        accounts = YamlConfiguration.loadConfiguration(accountsFile);
    }

    private void setupVault() {
        Plugin vault = getServer().getPluginManager().getPlugin("Vault");

        if (vault == null) {
            return;
        }

        getServer().getServicesManager().register(Economy.class, new VaultHandler(this, vault), this, ServicePriority.Highest);
    }
}
