package vip.creeper.mcserverplugins.creeperitemownerlocker;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by July on 2018/02/22.
 */
public class PlayerCommand implements CommandExecutor {
    private CreeperItemOwnerLocker plugin;
    private SimpleDateFormat dateSdf;
    private SimpleDateFormat smallDateSdf;
    private CmdConfirmManager cmdConfirmManager;
    private ItemOwnerLockerManager itemOwnerLockerManager;
    private Settings settings;
    private Economy vault;

    PlayerCommand(CreeperItemOwnerLocker plugin) {
        this.plugin = plugin;
        this.cmdConfirmManager = plugin.getCmdConfirmManager();
        this.itemOwnerLockerManager = plugin.getItemOwnerLockerManager();
        this.settings = plugin.getSettings();
        this.vault = plugin.getVault();
        this.dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        this.smallDateSdf = new SimpleDateFormat("HH:mm:ss");
    }

    public boolean onCommand(CommandSender cs, Command cmd, String lable, String[] args) {
        String playerName = cs.getName();
        boolean isPlayer = cs instanceof Player;
        boolean isAdmin = Util.isAdmin(cs);

        // set, clean, confirm
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "confirm": {
                    if (!isPlayer) {
                        Util.sendMsg(cs, "&c认主失败: 该命令必须由玩家来执行.");
                        return true;
                    }

                    Player player = (Player) cs;

                    if (args.length == 2) {
                        boolean isValidType = false;

                        for (CmdConfirmType type : CmdConfirmType.values()) {
                            if (type.name().equals(args[1])) {
                                isValidType = true;
                                break;
                            }
                        }

                        if (!isValidType) {
                            Util.sendMsg(player, "&c确认失败: 确认类型不存在.");
                            return true;
                        }

                        CmdConfirmType confirmType = CmdConfirmType.valueOf(args[1]);

                        if (!cmdConfirmManager.isExistsRequest(playerName, confirmType)) {
                            Util.sendMsg(player, "&c确认失败: &e" + confirmType.name() + " &c请求不存在.");
                            return true;
                        }

                        CmdConfirmRequests cmdConfirmRequests = cmdConfirmManager.getRequests(playerName);
                        CmdConfirmEntry confirmEntry = cmdConfirmRequests.getCmdConfirmEntry(confirmType);

                        confirmEntry.setConfirmed(true);
                        // 让玩家执行指令
                        player.chat("/" + confirmEntry.getConfirmedCmd());
                        cmdConfirmRequests.removeRequest(confirmType);
                        return true;
                    }
                }

                case "help":
                    sendHelpMsg(cs);
                    return true;

                case "set": {
                    if (!isPlayer) {
                        Util.sendMsg(cs, "&c认主失败: 该命令必须由玩家来执行.");
                        return true;
                    }

                    Player player = (Player) cs;
                    ItemStack item = player.getItemInHand();

                    // 判断是否已认主
                    if (itemOwnerLockerManager.isLocked(item)) {
                        Util.sendMsg(player, "&c认主失败: 该物品已被认主.");
                        return true;
                    }

                    if (item.getType() == Material.AIR) {
                        Util.sendMsg(player, "&c认主失败: 被认主的物品不能是空气.");
                        return true;
                    }

                    // 判断是否重叠
                    if (item.getAmount() != 1) {
                        Util.sendMsg(player, "&c认主失败: 被认主的物品数量必须为1.");
                        return true;
                    }

                    // 判断钱够不够
                    if (!isAdmin && vault.getBalance(player) < settings.getCostMoney()) {
                        Util.sendMsg(cs, "&c认主失败: 金币余额不足 &e" + settings.getCostMoney() + "元&c.");
                        return true;
                    }

                    // 确认请求
                    if (!isAdmin && (!cmdConfirmManager.isExistsRequest(playerName, CmdConfirmType.SET) || !cmdConfirmManager.getRequests(playerName).getCmdConfirmEntry(CmdConfirmType.SET).isConfirmed())) {
                        Util.sendMsg(player, "&e您将花费 &c" + settings.getCostMoney() + "元 &e来设置认主!");
                        Util.sendConfirmRawMsg(player, "ciol confirm SET");
                        cmdConfirmManager.putRequest(playerName, CmdConfirmType.SET,"ciol set");
                        return true;
                    }

                    // 扣钱
                    if (!isAdmin) {
                        vault.withdrawPlayer(player, settings.getCostMoney());
                    }

                    if (itemOwnerLockerManager.setOwner(playerName, item)) {
                        Util.sendMsg(cs, "&d认主成功!");
                        Util.sendMsg(cs, "&e温馨提示: 认主的道具可无视权限在箱子等容器中交互, 部分操作不受保护, 不保证100%不丢失!");
                    } else {
                        Util.sendMsg(cs, "&c认主失败: 系统错误, 请联系管理员.");
                    }

                    return true;
                }

                case "clean": {
                    if (!isPlayer) {
                        Util.sendMsg(cs, "&c认主失败: 该命令必须由玩家来执行.");
                        return true;
                    }

                    Player player = (Player) cs;
                    ItemStack item = player.getItemInHand();
                    String ownerName = itemOwnerLockerManager.getOwner(item);

                    if (ownerName == null) {
                        Util.sendMsg(cs, "&c去除认主失败: 该物品还没被认主.");
                        return true;
                    }

                    if (!ownerName.equalsIgnoreCase(playerName) && !Util.isAdmin(player)) {
                        Util.sendMsg(cs, "&c去除认主失败: 您不是物主.");
                        return true;
                    }

                    // 确认请求
                    if (!isAdmin && (!cmdConfirmManager.isExistsRequest(playerName, CmdConfirmType.RESET) || !cmdConfirmManager.getRequests(playerName).getCmdConfirmEntry(CmdConfirmType.RESET).isConfirmed())) {
                        Util.sendMsg(player, "&e您将对您手上的物品取消认主.");
                        Util.sendConfirmRawMsg(player, "ciol confirm RESET");
                        cmdConfirmManager.putRequest(playerName, CmdConfirmType.RESET,"ciol clean");
                        return true;
                    }

                    itemOwnerLockerManager.resetOwner(item);
                    Util.sendMsg(cs, "&d去除认主成功!");
                    return true;
                }
            }
        }

        sendHelpMsg(cs);
        return true;
    }

    private void sendHelpMsg(CommandSender cs) {
        Util.sendMsg(cs, "&f/clio set &b- &f为您手上的物品设置认主");
        Util.sendMsg(cs, "&f/clio clean &b- &f为您手上的物品取消认主");
    }
}
