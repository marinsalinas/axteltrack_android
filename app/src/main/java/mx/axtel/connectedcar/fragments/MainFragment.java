package mx.axtel.connectedcar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import mx.axtel.connectedcar.LoginActivity;
import mx.axtel.connectedcar.R;
import mx.axtel.connectedcar.helpers.Session;

/**
 * Created by marinsalinas on 3/30/15.
 */
public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);

        Session ss = new Session(getActivity().getApplicationContext());



        TextView tv = (TextView) view.findViewById(R.id.tv_main);

        tv.setText(ss.getUserSession().getContactName());

        ButtonFlat btn = (ButtonFlat) view.findViewById(R.id.buton_main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((new Session(getActivity().getApplicationContext()).logOut())) {

                    startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        });
        return view;

    }

}
