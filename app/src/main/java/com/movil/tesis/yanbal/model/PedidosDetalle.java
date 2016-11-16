package com.movil.tesis.yanbal.model;


/**
 * Created by mac on 11/14/16.
 */
public class PedidosDetalle {
    private int codigoPedidosDetalle;
    private String nombreProducto;
    private String descripcionProducto;
    private int cantidad;
    private double precio;
    private String estado;
    private Integer codCampana;
    private PedidosCabecera pedidosCabecera;

    public int getCodigoPedidosDetalle() {
        return codigoPedidosDetalle;
    }

    public void setCodigoPedidosDetalle(int codigoPedidosDetalle) {
        this.codigoPedidosDetalle = codigoPedidosDetalle;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCodCampana() {
        return codCampana;
    }

    public void setCodCampana(Integer codCampana) {
        this.codCampana = codCampana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PedidosDetalle that = (PedidosDetalle) o;

        if (codigoPedidosDetalle != that.codigoPedidosDetalle) return false;
        if (cantidad != that.cantidad) return false;
        if (Double.compare(that.precio, precio) != 0) return false;
        if (nombreProducto != null ? !nombreProducto.equals(that.nombreProducto) : that.nombreProducto != null)
            return false;
        if (descripcionProducto != null ? !descripcionProducto.equals(that.descripcionProducto) : that.descripcionProducto != null)
            return false;
        if (estado != null ? !estado.equals(that.estado) : that.estado != null) return false;
        if (codCampana != null ? !codCampana.equals(that.codCampana) : that.codCampana != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = codigoPedidosDetalle;
        result = 31 * result + (nombreProducto != null ? nombreProducto.hashCode() : 0);
        result = 31 * result + (descripcionProducto != null ? descripcionProducto.hashCode() : 0);
        result = 31 * result + cantidad;
        temp = Double.doubleToLongBits(precio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        result = 31 * result + (codCampana != null ? codCampana.hashCode() : 0);
        return result;
    }

    public PedidosCabecera getPedidosCabecera() {
        return pedidosCabecera;
    }

    public void setPedidosCabecera(PedidosCabecera pedidosCabecera) {
        this.pedidosCabecera = pedidosCabecera;
    }
}
