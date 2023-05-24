package com.example.dbsqlite.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbsqlite.databinding.ItemContactBinding;
import com.example.dbsqlite.model.Contact;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactVH> {

    private List<Contact> list;

    public ContactAdapter(List<Contact> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactVH(ItemContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContactVH extends RecyclerView.ViewHolder {
        ItemContactBinding binding;

        public ContactVH(@NonNull ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Contact contact) {
            binding.txtName.setText(contact.getName());
            binding.txtPhone.setText(contact.getPhoneNumber());
        }

    }

}
