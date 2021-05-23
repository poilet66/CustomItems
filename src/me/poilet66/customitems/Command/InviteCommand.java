package me.poilet66.customitems.Command;

import me.poilet66.customitems.CustomItems;
import me.poilet66.customitems.InviteRequestManager;
import me.poilet66.customitems.Objects.RequestInvite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteCommand implements CommandExecutor {

    private final CustomItems main;
    private final InviteRequestManager inviteManager;

    public InviteCommand(CustomItems main) {
        this.main = main;
        this.inviteManager = main.getIRM();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lbl, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return  true;
        }
        Player playerSender = (Player) sender;
        if(args.length < 2) {
            playerSender.sendMessage(ChatColor.RED + "You have not provided enough arguments.");
            return true;
        }
        Player targetPlayer;
        RequestInvite validInvite = null;
        switch(args[0]) {
            case("deny"):
                if(Bukkit.getPlayer(args[1]) == null) { //TODO: Offline player first, check if UUID is in list, if not deny if offline THEN
                    sender.sendMessage(ChatColor.RED + "The person you are trying to deny is offline.");
                    return true;
                }
                targetPlayer = Bukkit.getPlayer(args[1]);
                for(RequestInvite invite : inviteManager.getInviteList()) {
                    if(!(invite.getReceiver().equals(playerSender.getUniqueId()) && invite.getSender().equals(targetPlayer.getUniqueId()))) {
                        continue;
                    }
                    validInvite = invite;
                }
                if(validInvite == null) {
                    playerSender.sendMessage(ChatColor.RED + "That invite is invalid.");
                    return true;
                }
                inviteManager.removeInvite(validInvite);
                playerSender.sendMessage(String.format(ChatColor.GREEN + "Successfully denied %s's invite.", Bukkit.getOfflinePlayer(validInvite.getSender()).getPlayer().getName()));
                break;
            case("accept"):
                if(Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage(ChatColor.RED + "The person you are trying to accept is offline.");
                    return true;
                }
                targetPlayer = Bukkit.getPlayer(args[1]);
                for(RequestInvite invite : inviteManager.getInviteList()) {
                    if(!(invite.getReceiver().equals(playerSender.getUniqueId()) && invite.getSender().equals(targetPlayer.getUniqueId()))) {
                        continue;
                    }
                    validInvite = invite;
                }
                if(validInvite == null) {
                    playerSender.sendMessage(ChatColor.RED + "That invite is invalid.");
                    return true;
                }
                validInvite.getItem().onAccept(validInvite.getSender(), validInvite.getReceiver());
                break;
            default:
                break;
        }
        return true;
    }
}
