package net.ddns.foxsquad.togglepvp.events;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.ddns.foxsquad.togglepvp.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER) {
            Location loc0 = event.getDamager().getLocation();
            Location loc1 = event.getEntity().getLocation();

            boolean force_allow0 = false;
            boolean force_allow1 = false;

            for (ProtectedRegion region : getRegions(loc0)) {
                if(region.getFlag(Main.FORCE_PVP_FLAG) == StateFlag.State.ALLOW) {
                    force_allow0 = true;
                }
            }
            for (ProtectedRegion region : getRegions(loc1)) {
                if(region.getFlag(Main.FORCE_PVP_FLAG) == StateFlag.State.ALLOW) {
                    force_allow1 = true;
                }
            }

            boolean force_allow = force_allow0 && force_allow1;

            if(force_allow) {
                return;
            }

            Player attack = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            if(!Main.PVP_PLAYERS.contains(attack.getUniqueId())) {
                attack.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou need to enable PvP!\n&ePlease use /pvp"));
                event.setCancelled(true);
                return;
            }

            if(!Main.PVP_PLAYERS.contains(victim.getUniqueId())) {
                attack.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        victim.getDisplayName() + " needs to enable PvP!"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDisallowedWGPvP(DisallowedPVPEvent event) {
        event.setCancelled(true); // we have our own logic
    }

    private Set<ProtectedRegion> getRegions(@Nonnull Location location) {
        BlockVector3 loc3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        if(location.getWorld() == null) {
            System.out.println("location null (how)");
            return Collections.emptySet();
        }
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(
                BukkitAdapter.adapt(location.getWorld()
                ));

        if(regionManager == null) {
            System.out.println("regionManager null :sfyFine:");
            return Collections.emptySet();
        }
        return regionManager.getApplicableRegions(loc3).getRegions();
    }
}
