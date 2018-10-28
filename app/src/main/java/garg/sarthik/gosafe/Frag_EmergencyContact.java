package garg.sarthik.gosafe;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Frag_EmergencyContact extends Fragment {

    List<ContactData> contactDataList = new ArrayList<>();
    ContactAdaptor contactAdaptor;
    FloatingActionButton fabAdd;
    EditText etName;
    EditText etNumber;
    RecyclerView rvContacts;

    Fragment newInstance(double latitude, double longitude) {

        Frag_EmergencyContact fragEmergencyContact = new Frag_EmergencyContact();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", latitude);
        bundle.putDouble("long", longitude);
        fragEmergencyContact.setArguments(bundle);
        return fragEmergencyContact;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_emergencycontact, container, false);

        try {
            contactDataList = ContactApplication.getDB().getContactDAO().getAllContactData();
        } catch (NullPointerException e) {
            Log.e("TAG", "Array Is Empty");
        }
        fabAdd = view.findViewById(R.id.fabAdd);
        rvContacts = view.findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null)
            contactAdaptor = new ContactAdaptor(contactDataList, getContext(), getArguments().getDouble("lat"), getArguments().getDouble("long"), true);
        else
            contactAdaptor = new ContactAdaptor(contactDataList, getContext(), 0, 0, false);

        rvContacts.setAdapter(contactAdaptor);
        final View alertview = LayoutInflater.from(getContext()).inflate(R.layout.layout_add, null, true);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("ENTER DETAILS")
                .setView(alertview)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        etName = alertview.findViewById(R.id.etName);
                        etNumber = alertview.findViewById(R.id.etNumber);

                        String name = etName.getText().toString();
                        String number = etNumber.getText().toString();

                        if(number.length() == 10) {
                            ContactData contactData = new ContactData(name, number);
                            contactDataList.add(contactData);
                            ContactApplication.getDB().getContactDAO().inertTask(contactData);
                            if (getArguments() != null)
                                contactAdaptor = new ContactAdaptor(contactDataList, getContext(), getArguments().getDouble("lat"), getArguments().getDouble("long"), true);
                            else
                                contactAdaptor = new ContactAdaptor(contactDataList, getContext(), 0, 0, false);
                            rvContacts.setAdapter(contactAdaptor);
                        }
                        else
                            Snackbar.make(view,"Invalid Number",Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        return view;
    }
}
