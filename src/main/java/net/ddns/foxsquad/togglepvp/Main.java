package net.ddns.foxsquad.togglepvp;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.ddns.foxsquad.togglepvp.commands.PvPCommand;
import net.ddns.foxsquad.togglepvp.events.PlayerEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static StateFlag FORCE_PVP_FLAG;

    public static ArrayList<UUID> PVP_PLAYERS = new ArrayList<>();

    @Override
    public void onLoad() {
        getLogger().info("Adding WorldGuard Flag");
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("force-pvp-flag", true);
            registry.register(flag);
            FORCE_PVP_FLAG = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type // no lol
            Flag<?> existing = registry.get("force-pvp-flag");
            if (existing instanceof StateFlag) {
                FORCE_PVP_FLAG = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens

                // professional error handling RIGHT HERE
                getLogger().severe("Something bad happened :sfyGasp:");
            }
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        Objects.requireNonNull(getCommand("pvp")).setExecutor(new PvPCommand());
        Objects.requireNonNull(getCommand("pvp")).setTabCompleter(new PvPCommand());

        getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
