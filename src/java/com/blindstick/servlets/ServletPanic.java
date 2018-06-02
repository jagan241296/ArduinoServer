/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blindstick.servlets;

import com.blindstick.helper.DBHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sai_Kameswari
 */
public class ServletPanic extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           
            
                    System.out.println("ESP working");
                    String contact_1="", contact_2="", contact_3="";
                    String query = "select * from contacts";
                    String latitude="18.7020401", longitude="73.6816144";
                    PreparedStatement preparedStatement = DBHelper.preparedstmtInstance(query);
                    ResultSet rs = preparedStatement.executeQuery();                    
                    while(rs.next())
                    {                        
                        contact_1 =  rs.getString(2);
                        contact_2 =  rs.getString(3);
                        contact_3 =  rs.getString(4);
                    }
                    rs.close();
                    
                    //fetch LatLng
                    String loc_query = "select * from locations";
                    PreparedStatement ps1 = DBHelper.preparedstmtInstance(loc_query);
                    ResultSet rs1 = ps1.executeQuery();                    
                    while(rs1.next())
                    {                        
                        latitude = String.valueOf(rs1.getDouble(2));
                        longitude = String.valueOf(rs1.getDouble(3));
                    }
                    rs1.close();
                    DBHelper.close(preparedStatement);
                    
                    //Text to send 
                    String TTS = "User is in emergency..Click here to get location: https://maps.google.com/?q="
                            + latitude
                            + ","
                            + longitude;
                    
                    //send sms
                    sendsms.init();
                    sendsms.server = "http://127.0.0.1:8800/";
                    sendsms.user = "jagan241296";
                    sendsms.password = "jagan241296";                    
                    sendsms.text = TTS;
                    sendsms.phonenumber = contact_1;
                    sendsms.send();
                    sendsms.phonenumber = contact_2;
                    sendsms.send();
                    sendsms.phonenumber = contact_3;
                    sendsms.send();
        } catch (SQLException ex) {
            Logger.getLogger(ServletPanic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
