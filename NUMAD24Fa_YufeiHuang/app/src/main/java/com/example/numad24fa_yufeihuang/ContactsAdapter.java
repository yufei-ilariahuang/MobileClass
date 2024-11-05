package com.example.numad24fa_yufeihuang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private final List<Contact> contacts;
    private final OnContactActionListener actionListener;  // Added missing field

    // Remove unused OnContactClickListener interface since we're using stars now
    public interface OnContactActionListener {
        void onEditClick(Contact contact);
        void onDeleteClick(Contact contact);
        void onStarClick(Contact contact);
    }

    // Updated constructor to remove unused clickListener
    public ContactsAdapter(List<Contact> contacts, OnContactActionListener actionListener) {
        this.contacts = contacts;
        this.actionListener = actionListener;  // Save the actionListener
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView phoneText;
        private final ImageButton starButton;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            starButton = itemView.findViewById(R.id.buttonStar);
            nameText = itemView.findViewById(R.id.textName);
            phoneText = itemView.findViewById(R.id.textPhone);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);

            // Remove the itemView click listener since we're not using it anymore

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    actionListener.onEditClick(contacts.get(position));
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    actionListener.onDeleteClick(contacts.get(position));
                }
            });

            starButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    actionListener.onStarClick(contacts.get(position));
                }
            });
        }

        public void bind(Contact contact) {
            nameText.setText(contact.getName());
            phoneText.setText(contact.getPhoneNumber());
            starButton.setImageResource(contact.isStarred() ?
                    android.R.drawable.btn_star_big_on :
                    android.R.drawable.btn_star_big_off);
        }
    }
}