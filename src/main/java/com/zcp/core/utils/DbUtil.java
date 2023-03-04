package com.zcp.core.utils;

import java.sql.*;

/**
 * @author ZCP
 * @title: DBUtil
 * @projectName JavaGen
 * @description: DBUtil
 * @date 2023/1/15 22:29
 */
public class DbUtil {

    public static Connection getConnection(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cleanUp(Object ...resources) {
        try {
            for (int i = 0; i < resources.length; i++) {
                Object resource = resources[i];
                if (resource instanceof Connection){
                    ((Connection) resource).close();
                }else if(resource instanceof Statement){
                    ((Statement) resource).close();
                }else if (resource instanceof ResultSet){
                    ((ResultSet) resource).close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
