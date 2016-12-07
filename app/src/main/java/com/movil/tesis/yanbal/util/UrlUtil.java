package com.movil.tesis.yanbal.util;

import android.content.Context;
import android.net.Uri;


/**
 * util class for assembling urls depending on api request type
 */
public class UrlUtil {

    private static UrlUtil instance;

    private Context context;

    private static final Object mutex = new Object();

    private UrlUtil(Context context) {
        this.context = context;
    }

    public static UrlUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (mutex) {
                // Yes, it's appears kinda redundant, but it's necessary for not having to sync the whole getInstance method or the external calling method to it
                if (instance == null) instance = new UrlUtil(context);
            }
        }
        return instance;
    }

    public String getUrl(RequestType requestType, String username, String password, String code) {
        return getUrl(requestType, username, password, code, null);
    }

    public String getUrl(RequestType requestType, String username, String password, String code, String consultantId){
        return getUrl(requestType, username, password, code, consultantId, null, null, null);
    }


    public String getUrl(RequestType requestType, String username, String password, String code, String consultantId, String campaign, String week, String securityCode) throws IllegalArgumentException {
        Uri.Builder builder = new Uri.Builder();
        PropertiesHelper properties = PropertiesHelper.getInstance(context);

        String authority = properties.getAuthority();

        switch (requestType) {
            case LOGIN: {
                // "http://192.168.1.8:8080/yanbalWs/authenticate/" + username + "/" + password
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(authority);
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getAuthenticationEndpoint());
                builder.appendQueryParameter(properties.getUserIdQueryParameter(), username);
                builder.appendQueryParameter(properties.getPasswordQueryParameter(), password);
                break;
            }
            case CONSULTANT_REGISTER: {
                // "http://192.168.1.8:8080/yanbalWs/registerconsultant";
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(authority);
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getRegisterConsultantEndpoint());
                builder.appendQueryParameter(properties.getSecurityCodeParameter(), securityCode);
                break;
            }
            case CLIENT_REGISTER: {
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(authority);
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getRegisterClientEndpoint());
                break;
            }
            case PRODUCT_EXISTENCE_CHECK: {
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(properties.getAuthority());
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getProductCheckEndpoint());
                builder.appendQueryParameter(properties.getCodeQueryParameter(), code);
                break;
            }
            case CLIENTS_LIST: {
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(authority);
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getClientsListEndpoint());
                builder.appendQueryParameter(properties.getConsultantIdQueryParameter(), consultantId);
                break;
            }
            case ORDER_REGISTER: {
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(authority);
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getRegisterOrderEndpoint());
                break;
            }
            case CONSOLIDATED: {
                builder.scheme(properties.getScheme());
                builder.encodedAuthority(authority);
                builder.appendEncodedPath(properties.getServiceName());
                builder.appendEncodedPath(properties.getConsolidatedEndpoint());
                builder.appendQueryParameter(properties.getCampaignQueryParameter(), campaign);
                builder.appendQueryParameter(properties.getWeekQueryParameter(), week);
                builder.appendQueryParameter(properties.getConsultantIdQueryParameter(), consultantId);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
        return builder.build().toString();

    }
}
