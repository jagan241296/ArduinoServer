/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blindstick.servlets;

import com.blindstick.helper.DBHelper;
import com.blindstick.macros.MacServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Sai_Kameswari
 */
public class locationServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param requestJson
     * @param responseStatus
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, JSONObject requestJson, int responseStatus)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {

            int requestType;
            String IMEI;
            double latitude, longitude;
            JSONObject responseJson = new JSONObject();
            if (responseJson == null) {
                responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }
            System.out.println("Reached process request");
            //check request Type
            requestType = requestJson.getInt(MacServer.KEY_REQUEST_TYPE);
            System.out.println("Request type: " + requestType);
            switch (requestType) {
                case 2: //register the user in DB
                {                    
                    IMEI = requestJson.getString(MacServer.IMEI);
                    latitude = requestJson.getDouble(MacServer.LATITUDE);
                    longitude = requestJson.getDouble(MacServer.LONGITUDE);
                    
                    System.out.println("IMEI:" + IMEI);
                    System.out.println("Latitude:" + latitude);            
                    System.out.println("Longitude:" + longitude);
                    
                    responseJson.put(MacServer.KEY_REQUEST_TYPE, requestType);                   
                 
                    String updateQuery = "update locations set latitude=?, longitude=? where imei=?";
                    PreparedStatement preparedStatement = DBHelper.preparedstmtInstance(updateQuery);
                    preparedStatement.setDouble(1, latitude);
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setString(3, IMEI);
                    int updateStatus = preparedStatement.executeUpdate();
                    
                    if(updateStatus == 0)
                    {
                        //insert record...
                        String query = "insert into locations values(?,?,?)";
                        PreparedStatement preparedStatement1 = DBHelper.preparedstmtInstance(query);
                         preparedStatement1.setDouble(2, latitude);
                         preparedStatement1.setDouble(3, longitude);
                        preparedStatement1.setString(1, IMEI);
                        int insertStatus = preparedStatement1.executeUpdate();
                        //check if entry is added
                        if (insertStatus != 0) 
                        {
                            System.out.println("LatLng saved successfully in DB");
                        } 
                        else 
                        {
                            responseStatus = HttpServletResponse.SC_BAD_REQUEST;
                        }  
                    }
                    else
                    {
                        System.out.println("IMEI Location Updated");
                    }                                     
                    break;
                }
            }

            System.out.println("Response data " + responseJson.toString());
            //put response in stream
            if (responseStatus == HttpServletResponse.SC_OK) {
                // Send Response
                
                response.getOutputStream().print(responseJson.toString());
            }

            //send the response back & set status           
            response.setStatus(responseStatus);
            System.out.println("sent status");
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(userServlet.class.getName()).log(Level.SEVERE, null, ex);
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

        int responseStatus = HttpServletResponse.SC_OK;

        // Get Request JSON
        JSONObject requestJson = ParseGetParams(request);

        System.out.println("Reached get method" + requestJson.toString());

        if (requestJson.isNull(MacServer.KEY_REQUEST_TYPE)) {
            response.getWriter().println("Illegal request");
            responseStatus = HttpServletResponse.SC_BAD_REQUEST;
        }

        processRequest(request, response, requestJson, responseStatus);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int responseStatus = HttpServletResponse.SC_OK;

        // Get Request JSON
        JSONObject requestJson = ParsePostParams(request);

        System.out.println("Reached post method " + requestJson);

        if (requestJson.isNull(MacServer.KEY_REQUEST_TYPE)) {

            response.getWriter().println("Illegal request");
            responseStatus = HttpServletResponse.SC_BAD_REQUEST;
        }
        //send request JSON to the method
        processRequest(request, response, requestJson, responseStatus);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    protected JSONObject ParseGetParams(HttpServletRequest request) {
        JSONObject args = new JSONObject();

        String params = request.getHeader(MacServer.KEY_GET_PARAMS);

        if (params == null) {
            return args;
        }

        try {
            args = new JSONObject(params);
        } catch (JSONException e) {
            //error caught 
        }

        return args;
    }

    protected JSONObject ParsePostParams(HttpServletRequest request) {
        JSONObject args = new JSONObject();

        try {
            StringBuilder sb;
            try (BufferedReader reader = request.getReader()) {
                String line = "";
                sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            args = new JSONObject(sb.toString());
        } catch (JSONException | IOException e) {
            //Dbg.error(TAG, "JSON exception while argument parsing");
            e.printStackTrace();
        }
        //Dbg.error(TAG, "IO exception while argument parsing");

        return args;
    }

}

