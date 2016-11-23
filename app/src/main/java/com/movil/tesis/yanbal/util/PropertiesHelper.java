package com.movil.tesis.yanbal.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties file handler class
 */
public class PropertiesHelper {

    private static PropertiesHelper instance = null;

    private Context context;

    private static final String TAG = PropertiesHelper.class.getName();

    private static final Object mutex = new Object();

    Properties properties;

    private String scheme;
    private String authority;
    private String serviceName;
    private String authenticationEndpoint;
    private String userIdQueryParameter;
    private String passwordQueryParameter;
    private String registerConsultantEndpoint;
    private String registerClientEndpoint;
    private String productCheckEndpoint;
    private String codeQueryParameter;
    private String clientsListEndpoint;
    private String registerOrderEndpoint;
    private String consultantIdQueryParameter;


    private PropertiesHelper(Context context) {
        this.context = context;
        initPropertiesFile();
    }

    public static PropertiesHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (mutex) {
                // Yes, it's appears kinda redundant, but it's necessary for not having to sync the whole getInstance method or the external calling method to it
                if (instance == null) instance = new PropertiesHelper(context);
            }
        }
        return instance;
    }

    private void initPropertiesFile() {
        try {
            properties = new Properties();
            InputStream inputStream = context.getAssets().open("yanbal.properties");
            properties.load(inputStream);
            scheme = properties.getProperty("scheme");
            authority = properties.getProperty("authority");
            serviceName = properties.getProperty("service_name");
            authenticationEndpoint = properties.getProperty("authentication_endpoint");
            userIdQueryParameter = properties.getProperty("user_id_query_parameter");
            passwordQueryParameter = properties.getProperty("password_query_parameter");
            registerConsultantEndpoint = properties.getProperty("register_consultant_endpoint");
            registerClientEndpoint = properties.getProperty("register_client_endpoint");
            productCheckEndpoint = properties.getProperty("product_check_endpoint");
            codeQueryParameter = properties.getProperty("code_query_parameter");
            clientsListEndpoint = properties.getProperty("clients_list_endpoint");
            registerOrderEndpoint = properties.getProperty("register_order_endpoint");
            consultantIdQueryParameter = properties.getProperty("consultant_id_query_parameter");
            Log.i(TAG, "initPropertiesFile: Properties file successfully loaded");
        } catch (IOException | NumberFormatException ex) {
            Log.e(TAG, "Error cargando archivo de configuración", ex);
        }
    }

    public String getScheme() {
        return scheme;
    }

    public String getAuthority() {
        return authority;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getAuthenticationEndpoint() {
        return authenticationEndpoint;
    }

    public String getUserIdQueryParameter() {
        return userIdQueryParameter;
    }

    public String getPasswordQueryParameter() {
        return passwordQueryParameter;
    }

    public String getRegisterConsultantEndpoint() {
        return registerConsultantEndpoint;
    }

    public String getRegisterClientEndpoint() {
        return registerClientEndpoint;
    }

    public String getProductCheckEndpoint() {
        return productCheckEndpoint;
    }

    public String getCodeQueryParameter() {
        return codeQueryParameter;
    }

    public String getClientsListEndpoint() {
        return clientsListEndpoint;
    }

    public String getRegisterOrderEndpoint() {
        return registerOrderEndpoint;
    }

    public String getConsultantIdQueryParameter() {
        return consultantIdQueryParameter;
    }
}
