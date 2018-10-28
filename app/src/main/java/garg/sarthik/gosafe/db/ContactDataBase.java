package garg.sarthik.gosafe.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import garg.sarthik.gosafe.ContactData;

@Database(entities = {ContactData.class}, version = 1)
public abstract class ContactDataBase extends RoomDatabase {

    public abstract ContactDAO getContactDAO();
}
