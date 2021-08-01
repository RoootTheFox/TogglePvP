package net.ddns.foxsquad.togglepvp.commands;

import net.ddns.foxsquad.togglepvp.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class PvPCommand implements TabCompleter, CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("players only");
            return true;
        }
        Player p = ((Player) sender);
        if(Main.PVP_PLAYERS.contains(p.getUniqueId())) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDisabled PvP!"));
            Main.PVP_PLAYERS.remove(p.getUniqueId());
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEnabled PvP!"));
            Main.PVP_PLAYERS.add(p.getUniqueId());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        return null; // todo: /pvp on and /pvp off and tab complete for that
    }
}
