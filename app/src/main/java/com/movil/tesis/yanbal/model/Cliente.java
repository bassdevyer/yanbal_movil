package com.movil.tesis.yanbal.model;

import java.math.BigDecimal;


/**
 * Created by mac on 10/20/16.
 */

public class Cliente {

    private String identificacionCliente;
    private String tipoIdentificacionCliente;
    private String nombresCliente;
    private String apellidosCliente;
    private String emailCliente;
    private String celularCliente;
    private String telefonoCliente;
    private String fechaNacimientoCliente;
    private String generoCliente;
    private BigDecimal latitudCliente;
    private BigDecimal longitudCliente;
    private String codConsultora;

    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getTipoIdentificacionCliente() {
        return tipoIdentificacionCliente;
    }

    public void setTipoIdentificacionCliente(String tipoIdentificacionCliente) {
        this.tipoIdentificacionCliente = tipoIdentificacionCliente;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getFechaNacimientoCliente() {
        return fechaNacimientoCliente;
    }

    public void setFechaNacimientoCliente(String fechaNacimientoCliente) {
        this.fechaNacimientoCliente = fechaNacimientoCliente;
    }

    public String getGeneroCliente() {
        return generoCliente;
    }

    public void setGeneroCliente(String generoCliente) {
        this.generoCliente = generoCliente;
    }

    public BigDecimal getLatitudCliente() {
        return latitudCliente;
    }

    public void setLatitudCliente(BigDecimal latitudCliente) {
        this.latitudCliente = latitudCliente;
    }

    public BigDecimal getLongitudCliente() {
        return longitudCliente;
    }

    public void setLongitudCliente(BigDecimal longitudCliente) {
        this.longitudCliente = longitudCliente;
    }

    public String getCodConsultora() {
        return codConsultora;
    }

    public void setCodConsultora(String codConsultora) {
        this.codConsultora = codConsultora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        if (identificacionCliente != null ? !identificacionCliente.equals(cliente.identificacionCliente) : cliente.identificacionCliente != null)
            return false;
        if (tipoIdentificacionCliente != null ? !tipoIdentificacionCliente.equals(cliente.tipoIdentificacionCliente) : cliente.tipoIdentificacionCliente != null)
            return false;
        if (nombresCliente != null ? !nombresCliente.equals(cliente.nombresCliente) : cliente.nombresCliente != null)
            return false;
        if (apellidosCliente != null ? !apellidosCliente.equals(cliente.apellidosCliente) : cliente.apellidosCliente != null)
            return false;
        if (emailCliente != null ? !emailCliente.equals(cliente.emailCliente) : cliente.emailCliente != null)
            return false;
        if (celularCliente != null ? !celularCliente.equals(cliente.celularCliente) : cliente.celularCliente != null)
            return false;
        if (telefonoCliente != null ? !telefonoCliente.equals(cliente.telefonoCliente) : cliente.telefonoCliente != null)
            return false;
        if (fechaNacimientoCliente != null ? !fechaNacimientoCliente.equals(cliente.fechaNacimientoCliente) : cliente.fechaNacimientoCliente != null)
            return false;
        if (generoCliente != null ? !generoCliente.equals(cliente.generoCliente) : cliente.generoCliente != null)
            return false;
        if (latitudCliente != null ? !latitudCliente.equals(cliente.latitudCliente) : cliente.latitudCliente != null)
            return false;
        if (longitudCliente != null ? !longitudCliente.equals(cliente.longitudCliente) : cliente.longitudCliente != null)
            return false;
        if (codConsultora != null ? !codConsultora.equals(cliente.codConsultora) : cliente.codConsultora != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = identificacionCliente != null ? identificacionCliente.hashCode() : 0;
        result = 31 * result + (tipoIdentificacionCliente != null ? tipoIdentificacionCliente.hashCode() : 0);
        result = 31 * result + (nombresCliente != null ? nombresCliente.hashCode() : 0);
        result = 31 * result + (apellidosCliente != null ? apellidosCliente.hashCode() : 0);
        result = 31 * result + (emailCliente != null ? emailCliente.hashCode() : 0);
        result = 31 * result + (celularCliente != null ? celularCliente.hashCode() : 0);
        result = 31 * result + (telefonoCliente != null ? telefonoCliente.hashCode() : 0);
        result = 31 * result + (fechaNacimientoCliente != null ? fechaNacimientoCliente.hashCode() : 0);
        result = 31 * result + (generoCliente != null ? generoCliente.hashCode() : 0);
        result = 31 * result + (latitudCliente != null ? latitudCliente.hashCode() : 0);
        result = 31 * result + (longitudCliente != null ? longitudCliente.hashCode() : 0);
        result = 31 * result + (codConsultora != null ? codConsultora.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.apellidosCliente + " " + this.nombresCliente;
    }
}
