package com.example.quanlibenhvien.DATABASE;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
public class Connect {
    private static final String IP = "192.168.246.180";
    private static final String DATABASE = "quanlibenhvien";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123";
    private static final String PORT = "1433";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    @SuppressLint("NewApi")
    private Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connUrl = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + "/" + DATABASE;
            connection = DriverManager.getConnection(connUrl, USERNAME, PASSWORD);
            Log.d("Connect", "Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Connect", "Connection failed!", e);
        }
        return connection;
    }  // mở kết nối
    public ResultSet read(String query) {
        connection = getConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }  // đọc dữ liệu
    public boolean exec(String query) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    } // thực hiện truy vấn
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
            connection = null;
        }
    }  // đóng kết nối
}