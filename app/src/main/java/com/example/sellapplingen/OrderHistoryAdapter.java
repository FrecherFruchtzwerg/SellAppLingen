package com.example.sellapplingen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context context;
    private List<PlacedOrder> orderList;


    public OrderHistoryAdapter(Context context, List<PlacedOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlacedOrder order = orderList.get(position);
        holder.orderID.setText(order.getOrderID());
        holder.recipient.setText(String.format("%s%s", order.getFirstName(), order.getLastName()));
        holder.deliveryDateRecipient.setText(order.getDeliveryDate());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, recipient, deliveryDateRecipient;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderID);
            recipient = itemView.findViewById(R.id.recipient);
            deliveryDateRecipient = itemView.findViewById(R.id.deliveryDateRecipient);
        }
    }
}
