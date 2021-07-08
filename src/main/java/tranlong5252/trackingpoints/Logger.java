package tranlong5252.trackingpoints;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Logger {
    TrackingPoints trackingPoints;
    File file;
    File folder;

    public Logger(TrackingPoints plugin) {
        this.trackingPoints = plugin;
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String time = timeFormat.format(now);
        folder = new File(String.valueOf(plugin.getDataFolder()));
        file = new File(plugin.getDataFolder() + "/"+time+".log");
    }

    public void addMessage(String message) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(message);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        if (!file.exists()) {
            try {
                folder.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void formatMessage(Player player, int change, int current) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = timeFormat.format(new Date());
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world != null) {
            String worldName = world.getName();
            String finalMessage;
            if (change>0)
                finalMessage = String.format("[%s]: %s da co bien dong tang so du (+%d). So du moi: %d. Location: %d,%d,%d (%s)\n",
                    date, player.getName(), change, current, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                    worldName);
            else finalMessage = String.format("[%s]: %s da co bien dong giam so du (%d). So du moi: %d. Location: %d,%d,%d (%s)\n",
                    date, player.getName(), change, current, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                    worldName);
            addMessage(finalMessage);
        }
    }
}
