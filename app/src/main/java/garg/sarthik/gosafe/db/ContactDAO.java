package garg.sarthik.gosafe.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import garg.sarthik.gosafe.ContactData;

@Dao
public interface ContactDAO {


    @Query("SELECT * FROM contactdata")
    List<ContactData> getAllContactData();

    @Insert
    void inertTask(ContactData contactData);

    @Delete
    void deleteContactData(ContactData contactData);


}
