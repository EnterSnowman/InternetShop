package com.entersnowman.internetshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entersnowman.internetshop.R;
import com.entersnowman.internetshop.model.Order;

import java.util.ArrayList;

/**
 * Created by Valentin on 22.05.2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderHolder> {
    ArrayList<Order> orders;
    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        OrdersAdapter.OrderHolder orderHolder = new OrdersAdapter.OrderHolder(v);
        return orderHolder;
    }

    public OrdersAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        Log.d("order",orders.get(position).getStatus()+" "+orders.get(position).getCountOfProducts());
        holder.status.setText(orders.get(position).getStatus());
        holder.countOfProducts.setText(orders.get(position).getCountOfProducts());
        holder.dateOfMaking.setText(orders.get(position).getDateOfMaking());
        holder.city.setText(orders.get(position).getCity());
        holder.warehouse.setText(orders.get(position).getWarehouse());
        holder.kindOfPayment.setText(orders.get(position).getKindOfPayment());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        TextView status;
        TextView countOfProducts;
        TextView dateOfMaking;
        TextView city;
        TextView warehouse;
        TextView kindOfPayment;
        public OrderHolder(View itemView) {
            super(itemView);
            status = (TextView) itemView.findViewById(R.id.statusOfOrder);
            countOfProducts = (TextView) itemView.findViewById(R.id.countOfProducts);
            dateOfMaking = (TextView) itemView.findViewById(R.id.dateOfOrder);
            city = (TextView) itemView.findViewById(R.id.city);
            warehouse = (TextView) itemView.findViewById(R.id.warehouse);
            kindOfPayment = (TextView) itemView.findViewById(R.id.payment_kind);
        }
    }
}
