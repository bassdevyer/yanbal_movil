package com.movil.tesis.yanbal.model;

import java.util.Collection;

/**
 * Created by mac on 11/14/16.
 */
public class PedidosCabecera {
    private int codigoPedidoCabecera;
    private String fechaCompra;
    private Consultora consultora;
    private Cliente cliente;
    private Collection<PedidosDetalle> pedidosDetalles;

    public int getCodigoPedidoCabecera() {
        return codigoPedidoCabecera;
    }

    public void setCodigoPedidoCabecera(int codigoPedidoCabecera) {
        this.codigoPedidoCabecera = codigoPedidoCabecera;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PedidosCabecera that = (PedidosCabecera) o;

        if (codigoPedidoCabecera != that.codigoPedidoCabecera) return false;
        if (fechaCompra != null ? !fechaCompra.equals(that.fechaCompra) : that.fechaCompra != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codigoPedidoCabecera;
        result = 31 * result + (fechaCompra != null ? fechaCompra.hashCode() : 0);
        return result;
    }

    public Consultora getConsultora() {
        return consultora;
    }

    public void setConsultora(Consultora consultora) {
        this.consultora = consultora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Collection<PedidosDetalle> getPedidosDetalles() {
        return pedidosDetalles;
    }

    public void setPedidosDetalles(Collection<PedidosDetalle> pedidosDetalles) {
        this.pedidosDetalles = pedidosDetalles;
    }
}
