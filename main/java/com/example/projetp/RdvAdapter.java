package com.example.projetp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RdvAdapter extends ArrayAdapter<RdvDetails> {

    public RdvAdapter(Context context, int resource, List<RdvDetails> rdv )
    {
        super(context,resource, rdv);
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, null, false);

        Button buttonRdv = (Button) view.findViewById(R.id.buttonRdv);
        RdvDetails rdv = getItem(position);
        buttonRdv.setText(rdv.getTitle()+" - "+rdv.getDate());
        DetailAdapter detailAdapter = new DetailAdapter(getContext(),R.layout.details_activity, rdv.getID());

        Button buttonSuppr = (Button) view.findViewById(R.id.buttonSuppr);


        buttonRdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.INSTANCE.setContentView(detailAdapter.getView(position, null, null ));
            }
        });

        buttonSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.INSTANCE.getDatabaseHelper().delete(rdv.getID());
                Intent intent = new Intent(view.getContext(), MainActivity.class) ;
                view.getContext().startActivity(intent);
            }
        });


        return view;
    }

}
