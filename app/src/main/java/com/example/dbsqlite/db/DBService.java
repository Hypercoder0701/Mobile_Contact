package com.example.dbsqlite.db;

import com.example.dbsqlite.model.Contact;

import java.util.List;

public interface DBService {

    boolean insertContact(Contact contact);

    boolean updateContact(Contact contact);

    boolean deleteContact(int id);

    List<Contact> getAllContacts();

    Contact getContactById();

}
