package vip.creeper.mcserverplugins.creeperitemownerlocker;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by July on 2018/02/22.
 */
class ItemListener implements Listener {
    private ItemOwnerLockerManager itemOwnerLockerManager;

    ItemListener(CreeperItemOwnerLocker plugin) {
        this.itemOwnerLockerManager = plugin.getItemOwnerLockerManager();
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!Util.isAdmin(player) && itemOwnerLockerManager.isLocked(event.getItemDrop().getItemStack())) {
            if (player.getInventory().firstEmpty() == -1) {
                Util.sendMsg(player, "&c警告: 背包空间不足, 物品已掉落到地上!");
                return;
            }

            Util.sendMsg(player, "&c你不能丢弃该物品, 你可以将其存放至箱子中!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        // 为了确保安全，当指针物品有主人时，禁止捡起
        if (itemOwnerLockerManager.isLocked(player.getItemOnCursor())) {
            event.setCancelled(true);
            return;
        }

        String droppedItemOwner = itemOwnerLockerManager.getOwner(event.getItem().getItemStack());

        if (!Util.isAdmin(player) && droppedItemOwner != null) {
            // 检测指针物品
            if (player.getItemOnCursor().getType() != Material.AIR) {
                event.setCancelled(true);
            }

            if (!droppedItemOwner.equalsIgnoreCase(player.getName())) {
                Util.sendMsg(player, "&c这不是你的物品!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!Util.isAdmin(player) && !itemOwnerLockerManager.canUse(player.getName(), player.getInventory().getItem(event.getNewSlot()))) {
            Util.sendMsg(player, "&c这不是你的物品!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (!Util.isAdmin(player) && itemOwnerLockerManager.isLocked(item) && event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            Util.sendMsg(player, "&c你不能将被认主的物品放置在物品展示框中!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();

        if (!Util.isAdmin(player) && itemOwnerLockerManager.isLocked(player.getItemInHand())) {
            event.setCancelled(true);
            Util.sendMsg(player, "&c你不能将被认主的物品放置在盔甲架中!");
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (Util.isAdmin(player)) {
            return;
        }

        String currentItemOwner = itemOwnerLockerManager.getOwner(event.getCurrentItem());

        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            if (currentItemOwner != null && player.getInventory().firstEmpty() == -1) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                player.closeInventory();
                Util.sendMsg(player, "&c你必须给你背包留出至少一个空间才能对穿戴物品进行交互!");
            }

            return;
        }

        String playerName = player.getName();

        if (event.getInventory().getType() != InventoryType.CHEST
                && (currentItemOwner != null && !playerName.equalsIgnoreCase(currentItemOwner) || !itemOwnerLockerManager.canUse(playerName, event.getCursor()))) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            player.closeInventory();
            Util.sendMsg(player, "&c这不是你的物品!");
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!Util.isAdmin(player) && itemOwnerLockerManager.isLocked(event.getItemInHand())) {
            event.setBuild(false);
            event.setCancelled(true);
            Util.sendMsg(player, "&c你不能放置被认主的物品!");
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!Util.isAdmin(player) && !itemOwnerLockerManager.canUse(player.getName(), player.getItemInHand())) {
            event.setCancelled(true);

            PlayerInventory playerInventory = player.getInventory();
            int heldItemSlot = player.getInventory().getHeldItemSlot();

            if (heldItemSlot == 8) {
                playerInventory.setHeldItemSlot(7);
            } else {
                playerInventory.setHeldItemSlot(heldItemSlot + 1);
            }

            Util.sendMsg(player, "&c这不是你的物品!");
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity entityDamager = event.getDamager();

        if (entityDamager instanceof Player) {
            Player playerDamager = (Player) entityDamager;

            if (!Util.isAdmin(playerDamager) && !itemOwnerLockerManager.canUse(playerDamager.getName(), playerDamager.getItemInHand())) {
                event.setCancelled(true);
                Util.sendMsg(playerDamager, "&c这不是你的物品");
            }
        }
    }
}
