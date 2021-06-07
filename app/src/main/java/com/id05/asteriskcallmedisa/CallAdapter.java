package com.id05.asteriskcallmedisa;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.id05.asteriskcallmedisa.data.Call;

import java.util.ArrayList;
import static com.id05.asteriskcallmedisa.MainActivity.*;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.DISAViewHolder>  {

    private final ArrayList<Call> calls;
    private final Context context;

    interface OnContactClickListener {
        void onContactClick(int position);
        void onDeleteAll();
    }

    private static OnContactClickListener mListener;


    CallAdapter(ArrayList<Call> calls, Context context){
        this.calls = calls;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public DISAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_layout, parent, false);
        return new DISAViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DISAViewHolder disaViewHolder, final int i) {
        disaViewHolder.contactName.setText(calls.get(i).getName());
        disaViewHolder.contactPhone.setText(calls.get(i).getNumber());
        disaViewHolder.callDate.setText(calls.get(i).getCallDate());
        disaViewHolder.cLayout.setBackgroundColor(Color.WHITE);
        disaViewHolder.cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disaViewHolder.cLayout.setBackgroundColor(Color.GRAY);
                notifyItemChanged(i);
                if((!SERVER_IP.equals("")) & (SERVERPORT > 0)
                        & (!amiuser.equals("")) & (!amisecret.equals(""))
                        & (!astercontext.equals("")) & (!myphonenumber.equals(""))) {
                    mListener.onContactClick(i);
                }else{
                    Toast toast = Toast.makeText(context,
                            "Сheck your settings", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        disaViewHolder.cLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, disaViewHolder.cLayout);
                popupMenu.inflate(R.menu.call_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.deleteall) {
                            mListener.onDeleteAll();
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return calls.size();
    }

    static class DISAViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        TextView contactName;
        TextView contactPhone;
        TextView callDate;
        LinearLayout cLayout;

        DISAViewHolder(View itemView)  {
            super(itemView);
            contactName = itemView.findViewById(R.id.nameContact);
            contactPhone = itemView.findViewById(R.id.phoneContact);
            callDate = itemView.findViewById(R.id.callTime);
            cLayout = itemView.findViewById(R.id.callLayout);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.onContactClick(position);
        }
    }
}
