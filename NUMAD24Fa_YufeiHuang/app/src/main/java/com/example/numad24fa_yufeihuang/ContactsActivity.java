package com.example.numad24fa_yufeihuang;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<Contact> contacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contacts = new ArrayList<>();
        setupRecyclerView();
        setupFAB();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactsAdapter(contacts,
                new ContactsAdapter.OnContactActionListener() {
                    @Override
                    public void onEditClick(Contact contact) {
                        showEditDialog(contact);
                    }

                    @Override
                    public void onDeleteClick(Contact contact) {
                        deleteContact(contact);
                    }

                    @Override
                    public void onStarClick(Contact contact) {
                        toggleStarredStatus(contact);
                    }
                }
        );
        recyclerView.setAdapter(adapter);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddContactDialog());
    }

    private void showAddContactDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText nameEdit = dialogView.findViewById(R.id.editTextName);
        EditText phoneEdit = dialogView.findViewById(R.id.editTextPhone);

        setupPhoneNumberFormatting(phoneEdit);

        new AlertDialog.Builder(this)
                .setTitle("Add New Contact")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEdit.getText().toString().trim();
                    String phone = phoneEdit.getText().toString().trim();

                    if (validateInput(name, phone)) {
                        addNewContact(name, phone);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupPhoneNumberFormatting(EditText phoneEdit) {
        phoneEdit.addTextChangedListener(new TextWatcher() {
            private boolean mFormatting = false;
            private int mLastStartLocation;
            private String mLastBeforeText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mFormatting) return;
                mLastStartLocation = start;
                mLastBeforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mFormatting) return;
                mFormatting = true;

                // First, remove any existing formatting
                String digits = s.toString().replaceAll("[^\\d]", "");

                // Now format the number
                StringBuilder formatted = new StringBuilder();
                if (digits.length() >= 3) {
                    formatted.append(digits.substring(0, 3));
                    if (digits.length() >= 6) {
                        formatted.append("-").append(digits.substring(3, 6));
                        if (digits.length() >= 10) {
                            formatted.append("-").append(digits.substring(6, 10));
                        } else {
                            formatted.append("-").append(digits.substring(6));
                        }
                    } else {
                        formatted.append("-").append(digits.substring(3));
                    }
                } else {
                    formatted.append(digits);
                }

                s.replace(0, s.length(), formatted);
                mFormatting = false;
            }
        });
    }

    private boolean validateInput(String name, String phone) {
        if (name.isEmpty()) {
            showSnackbar("Please enter a name", null, null);
            return false;
        }

        String digits = phone.replaceAll("[^\\d]", "");
        if (digits.length() != 10) {
            showSnackbar("Please enter a valid 10-digit phone number", null, null);
            return false;
        }

        return true;
    }

    private void addNewContact(String name, String phone) {
        Contact newContact = new Contact(name, phone);
        contacts.add(newContact);
        adapter.notifyItemInserted(contacts.size() - 1);

        showSnackbar("Contact added successfully", "View", view -> {
            recyclerView.smoothScrollToPosition(contacts.size() - 1);
        });
    }

    private void showEditDialog(Contact contact) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText nameEdit = dialogView.findViewById(R.id.editTextName);
        EditText phoneEdit = dialogView.findViewById(R.id.editTextPhone);

        nameEdit.setText(contact.getName());
        phoneEdit.setText(contact.getPhoneNumber());

        setupPhoneNumberFormatting(phoneEdit);

        new AlertDialog.Builder(this)
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameEdit.getText().toString().trim();
                    String newPhone = phoneEdit.getText().toString().trim();

                    if (validateInput(newName, newPhone)) {
                        updateContact(contact, newName, newPhone);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateContact(Contact contact, String newName, String newPhone) {
        contact.setName(newName);
        contact.setPhoneNumber(newPhone);
        adapter.notifyDataSetChanged();

        showSnackbar("Contact updated successfully", "Undo", view -> {
            // Implementation for undo functionality could go here
            showSnackbar("Undo feature coming soon", null, null);
        });
    }

    private void deleteContact(Contact contact) {
        int position = contacts.indexOf(contact);
        contacts.remove(contact);
        adapter.notifyItemRemoved(position);

        showSnackbar("Contact deleted", "Undo", view -> {
            contacts.add(position, contact);
            adapter.notifyItemInserted(position);
        });
    }

    private void showSnackbar(String message, String actionText, View.OnClickListener action) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);

        if (actionText != null && action != null) {
            snackbar.setAction(actionText, action);
        }

        snackbar.show();
    }
    private void toggleStarredStatus(Contact contact) {
        contact.setStarred(!contact.isStarred());
        adapter.notifyDataSetChanged();

        String message = contact.isStarred() ?
                "Contact marked as important" :
                "Contact unmarked";

        showSnackbar(message, "Move to Top", view -> {
            if (contact.isStarred()) {
                moveContactToTop(contact);
            }
        });
    }

    private void moveContactToTop(Contact contact) {
        if (contacts.remove(contact)) {
            contacts.add(0, contact);
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
        }
    }
}
