package tranlong5252.trackingpoints;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceHolder extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "tranlong5252";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "trackingpoints";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String args) {
        MySQL mySQL = MySQL.getInstance();

        String action = args.split("_", 2)[0].toLowerCase();
        if (args.split("_").length < 1) {
            return "";
        }
        if (action.equals("me")) {
            return mySQL.getPoints(player.getName()) + "";
        }
        if (args.split("_").length <= 1) {
            return "";
        }
        String arguments = PlaceholderAPI.setBracketPlaceholders(player, args.split("_", 2)[1]);
        switch (action) {
            case "top" -> {
                return mySQL.getTop(Integer.parseInt(arguments)) + "";
            }
            case "user" -> {
                return mySQL.getPoints(arguments) + "";
            }
        }
        return "";
    }
}
