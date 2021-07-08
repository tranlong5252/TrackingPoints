package tranlong5252.trackingpoints;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class TrackingPoints extends JavaPlugin implements Listener {

    private static PlayerPoints playerPoints;
    private Logger logger;

    @Override
    public void onEnable() {
        hookPlayerPoints();
        this.logger = new Logger(this);
        logger.setup();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void hookPlayerPoints() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = (PlayerPoints) plugin;
        getServer().getPluginManager().registerEvents(this, this);
    }

    public static PlayerPoints getPlayerPoints() {
        return playerPoints;
    }


    @EventHandler
    public void onPPChange(PlayerPointsChangeEvent event) {
        int point = event.getChange();
        PlayerPointsAPI api = getPlayerPoints().getAPI();
        UUID uuid = event.getPlayerId();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            int current = api.look(uuid);
            logger.formatMessage(player,point,current+point);
        }
    }
}
