package tranlong5252.trackingpoints;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Level;

public class MySQL {
    //region Constructor
    private Connection sql;
    private TrackingPoints main;

    public MySQL(TrackingPoints main, String host, String dbname, String user, String pwd, int port) {
        try {
            main.getLogger().info("Dang ket noi den MySQL...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.main = main;
                _instance = this;
            } catch (ClassNotFoundException e) {
                Class.forName("com.mysql.jdbc.Driver");
            }
            sql = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, user, pwd);
            main.getLogger().info("Ket noi den MySQL thanh cong.");
        } catch (Exception ex) {
            ex.printStackTrace();
            main.getPluginLoader().disablePlugin(main);
        }
    }
    //endregion

    //region Statics
    private static MySQL _instance;

    public static MySQL getInstance() {
        return Objects.requireNonNull(_instance);
    }
    //endregion

    //region Queries
    private static final String TOP_SELECT = "SELECT * FROM trackingpoints_players ORDER BY points DESC LIMIT ?, 1";
    private static final String TOP_UPDATE = "UPDATE trackingpoints_players SET points = points + @POINTS:=?, lastupdate = ? WHERE username = ?";
    private static final String TOP_ADD = "INSERT INTO trackingpoints_players(username, points, lastupdate) VALUES (?, @points:=?, ?) ON DUPLICATE KEY UPDATE username = @username";
    private static final String TOP_GET = "SELECT points FROM trackingpoints_players WHERE username = ?";
    //endregion

    //region Data
    public int getPoints(String username) {
        if (username == null) return 0;
        PreparedStatement statement = null;
        ResultSet result = null;
        int points = 0;
        try {
            statement = sql.prepareStatement(TOP_GET);
            statement.setString(1, username);
            result = statement.executeQuery();
            if (result.next()) points = result.getInt(1);
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "SQL ERROR", e);
        } finally {
            cleanup(result, statement);
        }
        return points;
    }
    //endregion

    //region TopUpdater
    public void updatePoints(String username, int points) throws SQLException {
        PreparedStatement ps = null;
        try {
            String query;
            if (topDataExist(username)) {
                query = TOP_UPDATE;
                ps = sql.prepareStatement(query);
                ps.setInt(1, points);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setString(3, username);
            }
            else{
                query = TOP_ADD;
                ps = sql.prepareStatement(query);
                ps.setString(1, username);
                ps.setInt(2, points);
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            }
            ps.executeUpdate();
        } finally {
            cleanup(null, ps);
        }
    }
    //endregion

    public String getTop(int top) {
        PreparedStatement statement = null;
        ResultSet result = null;
        int points = 0;
        String name = "";
        try {
            statement = sql.prepareStatement(TOP_SELECT);
            statement.setInt(1, top-1);
            result = statement.executeQuery();
            if (result.next()) {
                name = result.getString(1);
                points = result.getInt(2);
            }
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "SQL ERROR", e);
        } finally {
            cleanup(result, statement);
        }
        if (name.equals("")) return "";
        return name + " - " + points;
    }
    //endregion

    //region checkData
    public boolean topExist(String username, PreparedStatement statement) {
        if (username == null || username.equals("")) return false;
        ResultSet result = null;
        boolean r = false;
        try {
            statement.setString(1, username);
            result = statement.executeQuery();
            if (result.next()) r = true;
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "SQL ERROR", e);
        } finally {
            cleanup(result, statement);
        }
        return r;
    }

    public boolean topDataExist(String username) {
        try {
            return topExist(username, sql.prepareStatement(TOP_GET));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //endregion

    //region cleaner
    protected void cleanup(ResultSet result, Statement statement) {
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                main.getLogger().log(Level.SEVERE, "SQLException on cleanup", e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                main.getLogger().log(Level.SEVERE, "SQLException on cleanup", e);
            }
        }
    }
    //endregion
}
