package company.handsome.markappproject;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MarkSettingFragment extends Fragment {
    private Button back;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        View v  = inflater.inflate(R.layout.fragment_setting,parent,false);
        back = (Button)v.findViewById(R.id.button_setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MarkMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }
}
