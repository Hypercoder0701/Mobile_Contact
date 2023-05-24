package com.example.dbsqlite.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbsqlite.R;
import com.example.dbsqlite.databinding.ActivityMainBinding;
import com.example.dbsqlite.db.DatabaseContact;
import com.example.dbsqlite.model.Contact;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Dialog dialog;

    private List<Contact> contacts;
    private ContactAdapter adapter;
    private DatabaseContact db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        db = new DatabaseContact(this);

        contacts = db.getAllContacts();

        adapter = new ContactAdapter(contacts);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        dialog = new BottomSheetDialog(MainActivity.this);
        createDialog();

        binding.fbAddContact.setOnClickListener(v -> {
            dialog.show();
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerView);
        new ItemTouchHelper(simpleCallbackPhone).attachToRecyclerView(binding.recyclerView);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerView);
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog, null, false);
        Button submit = view.findViewById(R.id.submit);
        Button cancel = view.findViewById(R.id.cancel);

        EditText name = view.findViewById(R.id.name);
        EditText number = view.findViewById(R.id.number);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact();
                contact.setName(String.valueOf(name.getText()));
                contact.setPhoneNumber("+998" + number.getText());
                if (db.insertContact(contact)) {
                    if (Double.parseDouble(String.valueOf(number.getText())) * 1 == Double.parseDouble(String.valueOf(number.getText()))) {
                        contacts.add(contact);
                        adapter.notifyItemInserted(contacts.size());
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Conflict", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);

    }

    Contact deleteData;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            Contact person = contacts.get(position);

            deleteData = contacts.get(position);
            contacts.remove(position);
            adapter.notifyDataSetChanged();
            db.deleteContact(person.getId());

            Snackbar.make(binding.recyclerView, "Deleting contact of  " + person.getName(), Snackbar.LENGTH_LONG).setAction("Go Back", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contacts.add(position, deleteData);
                    adapter.notifyDataSetChanged();
                }
            }).show();

        }

    };

    ItemTouchHelper.SimpleCallback simpleCallbackPhone = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            Contact person = contacts.get(position);

            Intent intentPhone = new Intent(Intent.ACTION_CALL);
            intentPhone.setData(Uri.parse("tel:" + person.getPhoneNumber().trim()));
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                startActivity(intentPhone);
                adapter.notifyDataSetChanged();
            }


        }
    };

}