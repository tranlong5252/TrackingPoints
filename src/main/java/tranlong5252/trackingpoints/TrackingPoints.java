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

import java.sql.SQLException;
import java.util.UUID;

public final class TrackingPoints extends JavaPlugin implements Listener {

    private static PlayerPoints playerPoints;
    private static TrackingPoints main;
    private Logger logger;
    MySQL mySQL;

    public static TrackingPoints getMain() {
        return main;
    }

    @Override
    public void onEnable() {
        main = this;
        hookPlayerPoints();
        logger = new Logger(this);
        Config.init();
        logger.setup();
        new PlaceHolder().register();
        mySQL = new MySQL(this, Config.host, Config.db, Config.user, Config.pwd, Config.port);
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
            try {
                mySQL.updatePoints(player.getName(), Math.abs(point));
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                logger.formatMessage(player, point, current + point);
            }
        }
    }
}
