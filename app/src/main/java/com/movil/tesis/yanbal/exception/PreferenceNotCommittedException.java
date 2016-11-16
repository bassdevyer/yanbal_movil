package com.movil.tesis.yanbal.exception;

/**
 * Exception thrown when a preference or preferences set are not commited correctly
 * Created by mac on 6/23/16.
 */
public class PreferenceNotCommittedException extends Exception {

    public PreferenceNotCommittedException(String detailMessage) {
        super(detailMessage);
    }
}
