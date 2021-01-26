package com.id05.asteriskcallmedisa;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.id05.asteriskcallmedisa.MainActivity.*;
import static java.lang.Thread.sleep;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.DISAViewHolder>  {

    private ArrayList<Contact> contacts;
    private Context context;

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
        //final Contact node = contacts.get(i);
        disaViewHolder.contactName.setText(contacts.get(i).getName());
        disaViewHolder.contactPhone.setText(contacts.get(i).getPhone());
        disaViewHolder.contactLayout.setBackgroundColor(Color.WHITE);

        disaViewHolder.contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disaViewHolder.contactLayout.setBackgroundColor(Color.GRAY);
                notifyItemChanged(i);//notifyDataSetChanged();

                Toast toast = Toast.makeText(context,
                        "Wait Call", Toast.LENGTH_LONG);
                toast.show();

                CallTask callTask = new CallTask();
                callTask.execute(contacts.get(i).getPhone());

//                try {
//                    MainActivity.MainProd("89145077248", contacts.get(i).getPhone());
//                } catch (IOException e) {
//                    Toast toast2 = Toast.makeText(context,
//                            "AMI Error", Toast.LENGTH_SHORT);
//                }

            }
        });
    }

    static class CallTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... param) {
            try{
                TimeUnit.SECONDS.sleep(5);
                try {
                    MainActivity.MainProd(myphonenumber, param[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class DISAViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactPhone;
        LinearLayout contactLayout;

        DISAViewHolder(View itemView)  {
            super(itemView);
            contactName = itemView.findViewById(R.id.nameContact);
            contactPhone = itemView.findViewById(R.id.phoneContact);
            contactLayout = itemView.findViewById(R.id.recordLayout);
        }
    }
}