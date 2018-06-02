package com.blindstick.macros;

public class MacServer 
{    
    //sharedPreferences
    public static final String SHARED_PREF_NAME           = "app_details";
    public static final String IMEI                       = "imei";

    //SOS Contacts
    public static final String CONTACT_1                       = "contact_1";
    public static final String CONTACT_2                       = "contact_2";
    public static final String CONTACT_3                       = "contact_3";

    //Location Information
    public static final String LATITUDE                        = "latitude";
    public static final String LONGITUDE                       = "longitude";


    // ----------------------- Constants ----------------------- //
    // General Request properties
    public static final int CONNECTION_TIMEOUT_MS               = 3000;
    public static final int READ_TIMEOUT_MS                     = 15000;

    // Request Methods for server requests
    public static final int REQUEST_METHOD_GET                  = 1;
    public static final int REQUEST_METHOD_POST                 = 2;

    // JSON Keys for request and response
    // Mandatory keys
    public static final String  KEY_GET_PARAMS                 = "get_paramas";     // Header Key in Get Request
    public static final String 	KEY_REQUEST_TYPE               = "request_type";

    public static final int         REQUEST_TYPE_REGISTER              = 1;
    public static final int         REQUEST_TYPE_LOCATION              = 2;
}
