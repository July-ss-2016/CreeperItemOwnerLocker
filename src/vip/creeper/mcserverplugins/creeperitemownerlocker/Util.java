package vip.creeper.mcserverplugins.creeperitemownerlocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by July on 2018/02/22.
 */
public class Util {
    public static boolean isValidItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    public static boolean isAdmin(Player player) {
        return player.hasPermission("CreeperItemOwnerLocker.admin");
    }

    public static void sendConfirmRawMsg(Player player, String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + "[\"\",{\"text\":\"[CreeperItemOwnerLocker] \",\"color\":\"green\"},{\"text\":\"点击确认\",\"color\":\"red\",\"italic\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + cmd + "\"}}]");
    }

    public static void sendMsg(CommandSender cs, String msg) {
        cs.sendMessage("§a[CreeperItemOwnerLocker] §b" + ChatColor.translateAlternateColorCodes('&', msg));
    }
}
