package com.example.project.Adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Object.Cart;
import com.example.project.Object.Order;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ItemViewHolder> {
    public static final class OrderState {
        public static final String PENDING = "Pending";
        public static final String CONFIRMED = "Confirmed";
        public static final String COMPLETED = "Completed";
        public static final String REJECTED = "Rejected";
    }

    List<Order> orderList = new ArrayList<>();
    private String role;

    public void setData(List<Order> mList) {
        orderList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_order, parent, false);
        return new ItemViewHolder(view);
    }

    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
    private FirebaseUser user;

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order == null) return;

        holder.tvOrderId.setText(order.getOrderId());
        holder.tvOrdering.setText(order.getOrderingMethod());
        holder.tvDate.setText(order.getOrderTime());
        holder.tvPayment.setText(order.getPayment());
        holder.tvName.setText(order.getAddress().getUserName());
        holder.tvPhoneNumber.setText(order.getAddress().getPhoneNumber());
        holder.tvPrice.setText(numberFormat.format(order.getTotalPrice()) + " VND");

        //hide address if empty
        String a = order.getAddress().getAddress();
        if (!a.isEmpty()) holder.layoutAddress.setVisibility(View.VISIBLE);
        else holder.layoutAddress.setVisibility(View.GONE);
        holder.tvAddress.setText(a);


        //cart
        String name,
                mCart = "";
        int quantity, subPrice;

        List<Cart> cartList = order.getCart();
        for (Cart ca : cartList) {
            mCart += " - " + ca.getFoodName() + " (" + numberFormat.format(ca.getSubPrice()) + " VND) - Quantity: " + ca.getQuantity() + "\n";
        }
        holder.tvCart.setText(mCart);

        //check role

        //default
        holder.layoutState.setVisibility(View.GONE);
        holder.btnCompleted.setVisibility(View.GONE);
        holder.btnCompleted.setEnabled(false);



        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference refUsers = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid())
                .child("role");

        refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                role = snapshot.getValue(String.class);
                boolean isAdmin = "admin".equals(role);

                DatabaseReference refState = FirebaseDatabase.getInstance()
                        .getReference("order")
                        .child(order.getOrderId())
                        .child("state");

                refState.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String state = snapshot.getValue(String.class);
                        updateUIForOrderState(holder, state, isAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });



        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Order ord = orderList.get(pos);
                    String orderId = ord.getOrderId();
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle("Reject order")
                            .setMessage("Are you sure to reject this order?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updateOrderState(holder, order.getOrderId(), OrderState.REJECTED);

                                }
                            }).setNegativeButton("No",null).show();
                }
            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Order ord = orderList.get(pos);
                    String orderId = ord.getOrderId();
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("order").child(orderId).child("state");
                    ref.setValue("Confirmed");
                    notifyDataSetChanged();
                }
            }
        });
        holder.btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Order ord = orderList.get(pos);
                    String orderId = ord.getOrderId();
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle("Complete order")
                            .setMessage("Are you sure this order has been completed?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updateOrderState(holder, order.getOrderId(), OrderState.COMPLETED);
                                }
                            }).setNegativeButton("No",null).show();

                }
            }
        });
    }
    private void updateOrderState(@NonNull ItemViewHolder holder, String orderId, String newState) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("order")
                .child(orderId)
                .child("state");

        ref.setValue(newState).addOnCompleteListener(task -> {
            if (task.isSuccessful() && newState.equals(OrderState.REJECTED)) {
                Toast.makeText(holder.itemView.getContext(),
                        "Order has been rejected", Toast.LENGTH_SHORT).show();
            } else if (!task.isSuccessful()) {
                Toast.makeText(holder.itemView.getContext(),
                        "An unknown error occurred", Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();
        });
    }

    private void updateUIForOrderState(@NonNull ItemViewHolder holder, String state, boolean isAdmin) {
        if (isAdmin) {
            switch (state) {
                case OrderState.PENDING:
                    holder.layoutState.setVisibility(View.VISIBLE);
                    holder.btnCompleted.setVisibility(View.GONE);
                    holder.btnCompleted.setEnabled(false);
                    holder.layoutOrder.setBackgroundColor(Color.TRANSPARENT); // Reset background
                    holder.btnCompleted.setText("Completed"); // Reset text
                    break;

                case OrderState.CONFIRMED:
                    holder.layoutState.setVisibility(View.GONE);
                    holder.btnCompleted.setVisibility(View.VISIBLE);
                    holder.btnCompleted.setEnabled(true);
                    holder.layoutOrder.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.confirm));
                    holder.btnCompleted.setText("Completed"); // Reset text
                    break;

                case OrderState.COMPLETED:
                    holder.layoutState.setVisibility(View.GONE);
                    holder.btnCompleted.setVisibility(View.VISIBLE);
                    holder.btnCompleted.setEnabled(false);
                    holder.layoutOrder.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.complete));
                    holder.btnCompleted.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_system));
                    holder.btnCompleted.setText("Completed");
                    break;

                case OrderState.REJECTED:
                    holder.layoutState.setVisibility(View.GONE);
                    holder.btnCompleted.setVisibility(View.VISIBLE);
                    holder.btnCompleted.setEnabled(false);
                    holder.layoutOrder.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.reject));
                    holder.btnCompleted.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_system));
                    holder.btnCompleted.setText("Rejected");
                    break;
            }
        } else {
            // not admin
            holder.layoutState.setVisibility(View.GONE);
            holder.btnCompleted.setVisibility(state.equals(OrderState.COMPLETED) ? View.VISIBLE : View.GONE);
            holder.btnCompleted.setEnabled(false);


            switch (state) {
                case OrderState.CONFIRMED:
                    holder.layoutOrder.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.confirm));
                    break;
                case OrderState.COMPLETED:
                    holder.layoutOrder.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.complete));
                    break;
                case OrderState.REJECTED:
                    holder.layoutOrder.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.reject));
                    break;
                default:
                    holder.layoutOrder.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (orderList != null) return orderList.size();
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvName, tvPhoneNumber, tvAddress, tvOrdering,
                tvDate, tvPrice, tvPayment, tvCart;
        private Button btnReject, btnAccept, btnCompleted;
        private LinearLayout layoutAddress, layoutState, layoutOrder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutOrder = itemView.findViewById(R.id.layout_item_order);
            layoutState = itemView.findViewById(R.id.layout_btn_status_order);
            btnCompleted = itemView.findViewById(R.id.btn_completed_order);
            btnAccept = itemView.findViewById(R.id.btn_accept_order);
            btnReject = itemView.findViewById(R.id.btn_reject_order);
            tvOrderId = itemView.findViewById(R.id.tv_order_code);
            tvName = itemView.findViewById(R.id.tv_full_name_order);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number_order);
            tvAddress = itemView.findViewById(R.id.tv_address_order);
            tvOrdering = itemView.findViewById(R.id.tv_ordering);
            tvDate = itemView.findViewById(R.id.tv_date_time_order);
            tvPrice = itemView.findViewById(R.id.tv_price_order);
            tvPayment = itemView.findViewById(R.id.tv_payment_order);
            btnReject = itemView.findViewById(R.id.btn_reject_order);
            btnAccept = itemView.findViewById(R.id.btn_accept_order);
            tvCart = itemView.findViewById(R.id.tv_food_cart_order);
            layoutAddress = itemView.findViewById(R.id.layout_address_order);
        }
    }
}
