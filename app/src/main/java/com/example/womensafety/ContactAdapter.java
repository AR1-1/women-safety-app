package com.example.womensafety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private  Context mContext;
    private LayoutInflater mInflater;
    private  ArrayList<Contacts> contacts;
    public  ContactClickListener mListener;

    //constructor for intializing adapter
public ContactAdapter(Context mContext, ArrayList<Contacts> contacts,ContactClickListener listener){
    System.out.println("setting contact adpater");
    this.mContext = mContext;
    this.contacts = contacts;
    this.mInflater = LayoutInflater.from(mContext);
    this.mListener = listener;

}


    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating layout for a contact items
        View view = mInflater.inflate(R.layout.data_item_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
//binding data to view with viewholder
        Contacts contact = contacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone_number());

        //item click listener
        if(mListener !=null){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notify on clicking
                mListener.onContactClick(contact);
            }
        });
        }
    }



    @Override
    public int getItemCount() {
    //return number of items in contactlist
       if(contacts != null){
           return contacts.size();
       }
    else {
        return 0;
       }

        }

    public static class ViewHolder extends RecyclerView.ViewHolder{
    //defining the views textview,imageview
        TextView tvName,tvPhone;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            //intializing views using itemview,findviewbyid
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
        }
        //binding data to view within viewholder
        public void bind(Contacts contact){
            //method to set data(settext,setimage)
            tvName.setText(contact.getName());
            tvPhone.setText(contact.getPhone_number());
        }
        public interface ContactClickListener{
            void onContactClick(Contacts contact);

}

        }

    }

