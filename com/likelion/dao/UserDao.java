package com.likelion.dao;

import com.likelion.domain.User;

import java.sql.*;
import java.util.Map;

public class UserDao {

    // ConnectionMaker class로 분리
    private ConnectionMaker connectionMaker;

    public UserDao() {
        this.connectionMaker = new AwsConnectionMaker();
    }

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException{
        Map<String, String> env = System.getenv();
        try {
            Connection conn = connectionMaker.makeConnection();
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

    public User findById(String id) throws ClassNotFoundException{
        Map<String, String> env = System.getenv();
        Connection conn;
        try {
            conn = connectionMaker.makeConnection();
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

    public int getCount() throws SQLException, ClassNotFoundException{
        Connection conn = connectionMaker.makeConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT count(*) FROM users");
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        pstmt.close();
        conn.close();

        return count;
    }

    public void deleteAll() throws SQLException, ClassNotFoundException{
        Connection conn = connectionMaker.makeConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users");
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }


    public static void main(String[] args) throws ClassNotFoundException{
        UserDao userDao = new UserDao();
        User user = userDao.findById("6");
        System.out.println(user.getName());
    }
}