package vip.creeper.mcserverplugins.creeperitemownerlocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;

/**
 * Created by July on 2018/02/22.
 */
public class Util {
    static boolean isValidItem(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    static boolean isAdmin(CommandSender player) {
        return player.hasPermission("CreeperItemOwnerLocker.admin");
    }

    static void sendConfirmRawMsg(Player player, String cmd) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + "[\"\",{\"text\":\"[CreeperItemOwnerLocker] \",\"color\":\"green\"},{\"text\":\"点击确认\",\"color\":\"red\",\"italic\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/" + cmd + "\"}}]");
    }

    static void sendMsg(CommandSender cs, String msg) {
        cs.sendMessage("§a[CreeperItemOwnerLocker] §b" + ChatColor.translateAlternateColorCodes('&', msg));
    }

/*    static boolean appendDataToFile(File file, String data) {
        try {
            if (!file.exists() && !file.createNewFile()) {
                return false;
            }

            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));

            bf.write(data + System.getProperty("line.separator"));
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }*/
}
