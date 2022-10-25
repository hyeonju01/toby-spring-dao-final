package com.likelion.dao;

import com.likelion.domain.User;

import java.sql.*;
import java.util.Map;

public class UserDao {
    public void add(User user) {
        Map<String, String> env = System.getenv();
        try {
            Connection conn = DriverManager.getConnection(env.get("DB_HOST"),
                    env.get("DB_USER"), env.get("DB_PASSWORD"));

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users(id, name, password) VALUES (?, ?, ?);");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());

            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(String id) {
        Map<String, String> env = System.getenv();
        Connection conn;
        try {
            conn = DriverManager.getConnection(env.get("DB_HOST"),
                    env.get("DB_USER"), env.get("DB_PASSWORD"));
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            User user = new User(rs.getString("id"), rs.getString("name"),
                    rs.getString("password"));

            rs.close();
            pstmt.close();
            conn.close();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User user = userDao.findById("6");
        System.out.println(user.getName());
    }
}
