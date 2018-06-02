
package com.blindstick.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHelper {

    //JDBC Drivers Details
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //public static final String DB_URL_AWS = "jdbc:mysql://brandslam.cciah3xt8bos.ap-south-1.rds.amazonaws.com/brandslam";
    public static final String DB_URL_AWS = "jdbc:mysql://localhost:3306/blindStick";

    //MySQL Login Credentials
    public static final String USER = "root";
    public static final String PASS = "root";

    static Connection con = null;

    public static Statement DBConnection() {

        Statement statement = null;
        try {
            if(con==null)
            {
            //Register JDBC Driver
            Class.forName(JDBC_DRIVER);

            //Creating Connection Object and connecting to MySQL
            con = DriverManager.getConnection(DB_URL_AWS, USER, PASS);
            }
            //create a statement object and send it to fire queries
            statement = con.createStatement();
           
        } catch (Exception e) {

        }        
        return statement;
    }

    public static PreparedStatement preparedstmtInstance(String sqlQuery) throws SQLException {

        PreparedStatement ps = null;
        try {
            if(con==null)
            {
            //Register JDBC Driver
            Class.forName(JDBC_DRIVER);
            
            //Creating Connection Object and connecting to MySQL
            con = DriverManager.getConnection(DB_URL_AWS, USER, PASS);
            }
            //create a preparedstatement instance and return it
            ps = con.prepareStatement(sqlQuery);
            
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ps;
    }
    
    public static void close(Statement st)
    {
        try {
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void close(PreparedStatement pst)
    {
        try {
            pst.close();
            if(con!=null)
            {
            con.close();
            con=null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
