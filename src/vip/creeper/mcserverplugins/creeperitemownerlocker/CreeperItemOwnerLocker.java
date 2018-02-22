package vip.creeper.mcserverplugins.creeperitemownerlocker;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import vip.creeper.mcserverplugins.creeperitemownerlocker.managers.CmdConfirmManager;
import vip.creeper.mcserverplugins.creeperitemownerlocker.managers.ItemOwnerLockerManager;

/**
 * Created by July on 2018/02/22.
 */
public class CreeperItemOwnerLocker extends JavaPlugin {
    private static CreeperItemOwnerLocker instance;
    private Economy vault;
    private Settings settings;
    private CmdConfirmManager cmdConfirmManager;
    private ItemOwnerLockerManager itemOwnerLockerManager;

    public void onEnable() {
        instance = this;
        this.settings = new Settings();
        this.cmdConfirmManager = new CmdConfirmManager();
        this.itemOwnerLockerManager = new ItemOwnerLockerManager();

        loadConfig();

        if (!hookVault()) {
            getLogger().warning("Vault Hook 失败!");
            setEnabled(false);
            return;
        }

        getCommand("ciol").setExecutor(new PlayerCommand(this));
        Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);
        getLogger().info("插件初始化完毕!");
    }

    private boolean hookVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        this.vault = rsp.getProvider();

        return vault != null;
    }

    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();

        FileConfiguration config = getConfig();

        settings.setCostMoney(config.getInt("cost_money"));
    }

    public Economy getVault() {
        return vault;
    }

    public Settings getSettings() {
        return settings;
    }

    public static CreeperItemOwnerLocker getInstance() {
        return instance;
    }

    public ItemOwnerLockerManager getItemOwnerLockerManager() {
        return itemOwnerLockerManager;
    }

    public CmdConfirmManager getCmdConfirmManager() {
        return cmdConfirmManager;
    }
}
