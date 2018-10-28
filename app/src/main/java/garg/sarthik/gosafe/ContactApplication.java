package garg.sarthik.gosafe;

import android.app.Application;
import android.arch.persistence.room.Room;

import garg.sarthik.gosafe.db.ContactDataBase;

public class ContactApplication extends Application {

    static ContactDataBase contactDataBase;

    public static ContactDataBase getDB() {
        return contactDataBase;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        contactDataBase = Room.databaseBuilder(getApplicationContext(),
                ContactDataBase.class,
                "contact-db")
                //Thia is not a good practice bcoz never run it on the UI thread
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

}
