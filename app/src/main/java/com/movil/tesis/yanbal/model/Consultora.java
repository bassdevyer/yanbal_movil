package com.movil.tesis.yanbal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 10/13/16.
 */

public class Consultora implements Parcelable {

    private String identificacionConsultora;
    private String tipoIdentificacionConsultora;
    private String nombresConsultora;
    private String apellidosConsultora;
    private String emailConsultora;
    private String celularConsultora;
    private String telefonoConsultora;
    private String fechaNacimientoConsultora;
    private String generoConsultora;
    private String password;

    public Consultora() {
    }

    protected Consultora(Parcel in) {
        identificacionConsultora = in.readString();
        tipoIdentificacionConsultora = in.readString();
        nombresConsultora = in.readString();
        apellidosConsultora = in.readString();
        emailConsultora = in.readString();
        celularConsultora = in.readString();
        telefonoConsultora = in.readString();
        fechaNacimientoConsultora = in.readString();
        generoConsultora = in.readString();
        password = in.readString();
    }

    public static final Creator<Consultora> CREATOR = new Creator<Consultora>() {
        @Override
        public Consultora createFromParcel(Parcel in) {
            return new Consultora(in);
        }

        @Override
        public Consultora[] newArray(int size) {
            return new Consultora[size];
        }
    };

    public String getIdentificacionConsultora() {
        return identificacionConsultora;
    }

    public void setIdentificacionConsultora(String identificacionConsultora) {
        this.identificacionConsultora = identificacionConsultora;
    }

    public String getTipoIdentificacionConsultora() {
        return tipoIdentificacionConsultora;
    }

    public void setTipoIdentificacionConsultora(String tipoIdentificacionConsultora) {
        this.tipoIdentificacionConsultora = tipoIdentificacionConsultora;
    }

    public String getNombresConsultora() {
        return nombresConsultora;
    }

    public void setNombresConsultora(String nombresConsultora) {
        this.nombresConsultora = nombresConsultora;
    }

    public String getApellidosConsultora() {
        return apellidosConsultora;
    }

    public void setApellidosConsultora(String apellidosConsultora) {
        this.apellidosConsultora = apellidosConsultora;
    }

    public String getEmailConsultora() {
        return emailConsultora;
    }

    public void setEmailConsultora(String emailConsultora) {
        this.emailConsultora = emailConsultora;
    }

    public String getCelularConsultora() {
        return celularConsultora;
    }

    public void setCelularConsultora(String celularConsultora) {
        this.celularConsultora = celularConsultora;
    }

    public String getTelefonoConsultora() {
        return telefonoConsultora;
    }

    public void setTelefonoConsultora(String telefonoConsultora) {
        this.telefonoConsultora = telefonoConsultora;
    }

    public String getFechaNacimientoConsultora() {
        return fechaNacimientoConsultora;
    }

    public void setFechaNacimientoConsultora(String fechaNacimientoConsultora) {
        this.fechaNacimientoConsultora = fechaNacimientoConsultora;
    }

    public String getGeneroConsultora() {
        return generoConsultora;
    }

    public void setGeneroConsultora(String generoConsultora) {
        this.generoConsultora = generoConsultora;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identificacionConsultora);
        dest.writeString(tipoIdentificacionConsultora);
        dest.writeString(nombresConsultora);
        dest.writeString(apellidosConsultora);
        dest.writeString(emailConsultora);
        dest.writeString(celularConsultora);
        dest.writeString(telefonoConsultora);
        dest.writeString(fechaNacimientoConsultora);
        dest.writeString(generoConsultora);
        dest.writeString(password);
    }
}
