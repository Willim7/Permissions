package me.willis.permissions.configuration;

import me.willis.permissions.Permissions;

import java.sql.*;
import java.util.UUID;

public class SQLConfig {

    private Permissions plugin;

    public SQLConfig(Permissions plugin) {
        this.plugin = plugin;
    }

    private Connection connection;

    private boolean isConnected() {
        return (connection == null ? false : true);
    }

    public void connect() {
        if (!isConnected()) {

            String host = plugin.getConfig().getString("Host");
            String port = plugin.getConfig().getString("Port");
            String database = plugin.getConfig().getString("Database");
            String username = plugin.getConfig().getString("Username");
            String password = plugin.getConfig().getString("Password");

            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        if (!isConnected()) {
            connect();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (UUID VARCHAR(100),RANK VARCHAR(100))");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasGroup(UUID uuid) {
        if (!isConnected()) {
            connect();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT UUID FROM players WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setGroup(UUID uuid, String group) {
        if (!isConnected()) {
            connect();
        }
        try {
            if (!hasGroup(uuid)) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (UUID,RANK) VALUES (?,?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, group.toLowerCase());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroup(UUID uuid, String group) {
        if (!isConnected()) {
            connect();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET RANK = ? WHERE UUID = ?");
            preparedStatement.setString(1, group.toLowerCase());
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getGroup(UUID uuid) {
        if (!isConnected()) {
            connect();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT RANK FROM players WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("RANK").toLowerCase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plugin.getConfig().getString("DefaultGroup");
    }
}
