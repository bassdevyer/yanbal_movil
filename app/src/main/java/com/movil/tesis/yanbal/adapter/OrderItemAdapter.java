package com.movil.tesis.yanbal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movil.tesis.yanbal.OrderFragment;
import com.movil.tesis.yanbal.R;
import com.movil.tesis.yanbal.model.PedidosDetalle;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by mac on 11/15/16.
 */

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private List<PedidosDetalle> orderItems;
    private DecimalFormat df = new DecimalFormat("#.00");
    private OrderFragment.OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView code, unitPrice, description, quantity, total;

        public ViewHolder(View view) {
            super(view);
            code = (TextView) view.findViewById(R.id.code);
            unitPrice = (TextView) view.findViewById(R.id.unitPrice);
            description = (TextView) view.findViewById(R.id.description);
            quantity = (TextView) view.findViewById(R.id.quantity);
            total = (TextView) view.findViewById(R.id.total);
        }
    }

    public OrderItemAdapter(List<PedidosDetalle> orderItems, OrderFragment.OnItemClickListener onItemClickListener) {
        this.orderItems = orderItems;
        this.listener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_row, parent, false);
        itemView.setOnClickListener(listener);
        itemView.setOnLongClickListener(listener);
        ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PedidosDetalle item = orderItems.get(position);
        holder.code.setText(item.getNombreProducto());
        holder.unitPrice.setText(String.valueOf(item.getPrecio()));
        holder.description.setText(item.getDescripcionProducto());
        holder.quantity.setText(String.valueOf(item.getCantidad()));
        holder.total.setText(df.format(item.getPrecio() * (double) item.getCantidad()));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

}
