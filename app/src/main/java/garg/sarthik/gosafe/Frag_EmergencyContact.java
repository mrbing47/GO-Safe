package garg.sarthik.gosafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
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
    ImageButton btnPolice;
    ImageButton btnAmbulance;
    ImageButton btnWomen;
    ImageButton btnFire;
    ImageButton btnChild;
    ImageButton btnDisaster;

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
        btnAmbulance = view.findViewById(R.id.ambulance);
        btnPolice = view.findViewById(R.id.police);
        btnChild = view.findViewById(R.id.child_care);
        btnDisaster = view.findViewById(R.id.disaster);
        btnFire = view.findViewById(R.id.fireBrigade);
        btnWomen = view.findViewById(R.id.women);

        rvContacts = view.findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));

        // if (getArguments() != null)
        contactAdaptor = new ContactAdaptor(contactDataList, getContext(), true);
//        else
//            contactAdaptor = new ContactAdaptor(contactDataList, getContext(), 0, 0, false);

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
                        etNumber.setText("");
                        etName.setText("");

                        if (number.length() == 10) {
                            boolean isAvailable = false;
                            for (ContactData contact : contactDataList) {
                                if (contact.getNumber().equals(number)) {
                                    isAvailable = true;
                                    Snackbar.make(view, "NUMBER ALREADY EXISTS", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if (!isAvailable) {
                                ContactData contactData = new ContactData(name, number);
                                contactDataList.add(contactData);
                                // ContactApplication.getDB().getContactDAO().inertTask(contactData);
                                contactAdaptor.notifyDataSetChanged();
                            }
                        } else
                            Snackbar.make(view, "Invalid Number", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        btnWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "181";
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + number));
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        btnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "100";
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + number));
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        btnDisaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "108";
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + number));
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        btnChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "1098";
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + number));
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "101";
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + number));
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        btnAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "102";
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + number));
                intent.setAction(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        rvContacts.setAdapter(contactAdaptor);

        return view;
    }


}
