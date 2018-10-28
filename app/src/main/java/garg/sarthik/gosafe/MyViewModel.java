package garg.sarthik.gosafe;

import android.arch.lifecycle.ViewModel;

import java.util.List;

public class MyViewModel extends ViewModel {

    private List<ContactData> contactData;

    List<ContactData> getDataFromDatabase() {

        if (contactData == null) {

            contactData = ContactApplication.getDB().getContactDAO().getAllContactData();

        }
        return contactData;
    }


}
