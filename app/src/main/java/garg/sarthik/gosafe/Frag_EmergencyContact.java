package garg.sarthik.gosafe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Frag_EmergencyContact extends Fragment {

    Fragment newInstance(double latitude, double longitude){

        Frag_EmergencyContact fragEmergencyContact = new Frag_EmergencyContact();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",latitude);
        bundle.putDouble("long",longitude);
        fragEmergencyContact.setArguments(bundle);
        return fragEmergencyContact;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_emergencycontact,container,false);



        return view;
    }
}
