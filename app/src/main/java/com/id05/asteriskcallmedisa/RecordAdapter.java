package com.id05.asteriskcallmedisa;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.id05.asteriskcallmedisa.data.Contact;
import java.util.ArrayList;
import static com.id05.asteriskcallmedisa.MainActivity.*;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.DISAViewHolder>  {

    private final ArrayList<Contact> contacts;
    private final Context context;

    interface OnContactClickListener {
        void onContactClick(int position);
    }

    private static OnContactClickListener mListener;


    RecordAdapter(ArrayList<Contact> contacts, Context context){
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public DISAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout, parent, false);
        return new DISAViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DISAViewHolder disaViewHolder, final int i) {
        disaViewHolder.contactName.setText(contacts.get(i).getName());
        disaViewHolder.contactPhone.setText(contacts.get(i).getPhone());
        disaViewHolder.contactLayout.setBackgroundColor(Color.WHITE);
        disaViewHolder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disaViewHolder.contactLayout.setBackgroundColor(Color.GRAY);
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
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class DISAViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        TextView contactName;
        TextView contactPhone;
        LinearLayout contactLayout;

        DISAViewHolder(View itemView)  {
            super(itemView);
            contactName = itemView.findViewById(R.id.nameContact);
            contactPhone = itemView.findViewById(R.id.phoneContact);
            contactLayout = itemView.findViewById(R.id.recordLayout);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.onContactClick(position);
        }
    }
}