package com.movil.tesis.yanbal.model;

import java.math.BigDecimal;

/**
 * Created by mac on 11/8/16.
 */

public class ProductosYanbal {

    private Integer codigo;
    private Integer codigoRapido;
    private String nombreProducto;
    private BigDecimal valor;
    private Integer disponible;
    private Integer stock;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigoRapido() {
        return codigoRapido;
    }

    public void setCodigoRapido(Integer codigoRapido) {
        this.codigoRapido = codigoRapido;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDisponible() {
        return disponible;
    }

    public void setDisponible(Integer disponible) {
        this.disponible = disponible;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductosYanbal{" +
                "codigo=" + codigo +
                ", codigoRapido=" + codigoRapido +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", valor=" + valor +
                ", disponible=" + disponible +
                ", stock=" + stock +
                '}';
    }
}
