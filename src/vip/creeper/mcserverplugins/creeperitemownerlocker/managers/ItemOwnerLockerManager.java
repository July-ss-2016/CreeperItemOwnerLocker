package vip.creeper.mcserverplugins.creeperitemownerlocker.managers;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vip.creeper.mcserverplugins.creeperitemownerlocker.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by July on 2018/02/22.
 */
public class ItemOwnerLockerManager {
    // 判断是否有主人
    public boolean isLocked(ItemStack item) {
        return getOwner(item) != null;
    }

    // 得到主人
    public String getOwner(ItemStack item) {
        if (!Util.isValidItem(item)) {
            return null;
        }

        List<String> lores = item.getItemMeta().getLore();
        int loresSize;

        // 如果有认主lores.size()必大于等于1
        if (lores == null || (loresSize = lores.size()) < 1) {
            return null;
        }

        /*
        - 已认主
        - {ID}

        - 认主 {ID}
         */

        for (int i = 0; i < loresSize; i ++) {
            String noColorCodeLore = ChatColor.stripColor(lores.get(i));

            // 符合新版认主的lore 且 i +1 没越界
            if (noColorCodeLore.equalsIgnoreCase("- 已认主") && i +1 < loresSize) {
                return ChatColor.stripColor(lores.get(i + 1)).replace("- ", "");
            }

            // 符合旧版认主的lore
            if (noColorCodeLore.startsWith("- 认主")) {
                return noColorCodeLore.replace("- 认主 ", "");
            }
        }

        return null;
    }

    // 设置主人
    public boolean setOwner(String owner, ItemStack item) {
        if (!Util.isValidItem(item)) {
            return false;
        }

        if (owner == null) {
            throw new IllegalArgumentException("主人不能为NULL!");
        }

        if (getOwner(item) != null) {
            throw new IllegalArgumentException("该物品已被认主!");
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lores = meta.getLore();

        if (lores == null) {
            lores = new ArrayList<>();
        }

        lores.remove("§7- §f已认主");
        lores.add("§7- §f已认主");
        lores.add("§7- §f" + owner);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return true;
    }

    // 重置主人
    public boolean resetOwner(ItemStack item) {
        if (!Util.isValidItem(item)) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lores = meta.getLore();
        int loresSize;

        if (lores == null || (loresSize = lores.size()) < 1) {
            return false;
        }

        for (int i = 0; i < loresSize; i ++) {
            String noColorCodeLore = ChatColor.stripColor(lores.get(i));

            // 符合新版认主的lore 且 i +1 没越界
            if (noColorCodeLore.equalsIgnoreCase("- 已认主") && i +1 < loresSize) {
                // remove时index发生了改变，所以要这么做
                lores.remove(i);
                lores.remove(i);
                meta.setLore(lores);
                item.setItemMeta(meta);
                return true;
            }

            // 符合旧版认主的lore
            if (noColorCodeLore.startsWith("- 认主")) {
                lores.remove(i);
                meta.setLore(lores);
                item.setItemMeta(meta);
                return true;
            }
        }

        return false;
    }
}
