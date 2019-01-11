package com.example.hp.helixtuner;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Float3;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import static com.example.hp.helixtuner.BHSVtoRGB.NoteToHue;
import static com.example.hp.helixtuner.ValidatePublic.*;

public class PresetActivity extends Fragment {
    MyAdapter myAdapter;
    AlertDialog dialog;
    ListView lvPreset;
    LinearLayout linearLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preset, container, false);
          lvPreset = view.findViewById(R.id.lvPreset);
        TextView txtcancePre = view.findViewById(R.id.txtcanePre);
        TextView txtnewPre = view.findViewById(R.id.txtnewPre);
        TextView txtsavePre = view.findViewById(R.id.txtsavePre);
        TextView txtdeletePre = view.findViewById(R.id.txtdeletePre);

        linearLayout = view.findViewById(R.id.layoutPreset);
        final RadioButton rdbPalete1, rdbPalete2, rdbPalete3, rdbPalete4, rdbPalete5, rdbPalete6;
        rdbPalete1 = getActivity().findViewById(R.id.rdbPalete1);
        rdbPalete2 = getActivity().findViewById(R.id.rdbPalete2);
        rdbPalete3 = getActivity().findViewById(R.id.rdbPalete3);
        rdbPalete4 = getActivity().findViewById(R.id.rdbPalete4);
        rdbPalete5 = getActivity().findViewById(R.id.rdbPalete5);
        rdbPalete6 = getActivity().findViewById(R.id.rdbPalete6);
        final RadioButton rdbMainPalete1, rdbMainPalete2, rdbMainPalete3, rdbMainPalete4, rdbMainPalete5, rdbMainPalete6;
        rdbMainPalete1 = getActivity().findViewById(R.id.rdbMainPalete1);
        rdbMainPalete2 = getActivity().findViewById(R.id.rdbMainPalete2);
        rdbMainPalete3 = getActivity().findViewById(R.id.rdbMainPalete3);
        rdbMainPalete4 = getActivity().findViewById(R.id.rdbMainPalete4);
        rdbMainPalete5 = getActivity().findViewById(R.id.rdbMainPalete5);
        rdbMainPalete6 = getActivity().findViewById(R.id.rdbMainPalete6);

        final TextView txtPreset;
        txtPreset = getActivity().findViewById(R.id.txtPreset);
        lvPreset.setBackgroundColor(Color.rgb(red, gren, blue));
        linearLayout.setBackgroundColor(Color.rgb(red, gren, blue));
        myAdapter = new MyAdapter(getActivity());
        lvPreset.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        lvPreset.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Bmpreset = listPreset.get(position).get(BMUS_Key_Preset).toString();
                BMNote1 = listPreset.get(position).get(BMUS_Key_note1).toString();
                BMNote2 = listPreset.get(position).get(BMUS_Key_note2).toString();
                BMNote3 = listPreset.get(position).get(BMUS_Key_note3).toString();
                BMNote4 = listPreset.get(position).get(BMUS_Key_note4).toString();
                BMNote5 = listPreset.get(position).get(BMUS_Key_note5).toString();
                BMNote6 = listPreset.get(position).get(BMUS_Key_note6).toString();
                rdbPalete1.setText(BMNote1);
                rdbPalete2.setText(BMNote2);
                rdbPalete3.setText(BMNote3);
                rdbPalete4.setText(BMNote4);
                rdbPalete5.setText(BMNote5);
                rdbPalete6.setText(BMNote6);

                rdbMainPalete1.setText(BMNote1);
                rdbMainPalete2.setText(BMNote2);
                rdbMainPalete3.setText(BMNote3);
                rdbMainPalete4.setText(BMNote4);
                rdbMainPalete5.setText(BMNote5);
                rdbMainPalete6.setText(BMNote6);
                txtPreset.setText(Bmpreset);
                lvPreset.setVisibility(View.INVISIBLE);
                getFragmentManager().popBackStack();


            }
        });

        txtcancePre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        txtnewPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvPreset.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);

                showDialog();
            }
        });


        return view;
    }

    class MyAdapter extends BaseAdapter {
        private LayoutInflater mlayoutInflater;

        public MyAdapter(Context context) {
            mlayoutInflater = LayoutInflater.from(context);  //Dynamic layout mapping
        }

        @Override
        public int getCount() {
            return listPreset.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mlayoutInflater.inflate(R.layout.custom_lv_hashmap, null);  //  According to the layout of the document to instantiate view
            TextView tv = (TextView) convertView.findViewById(R.id.txtHashmap);
            tv.setText(listPreset.get(position).get(BMUS_Key_Preset).toString());
            return convertView;
        }
    }

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_dialog, null);
        final EditText edtInput = row.findViewById(R.id.edtInput);
        TextView txtcanceinput = row.findViewById(R.id.txtcanceinput);
        final TextView txtdone = row.findViewById(R.id.txtdone);
        edtInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtdone.setTextColor(Color.rgb(200, 0, 0));

            }
        });

        txtdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edtInput.getText().toString();
                if (input.isEmpty()) {
                    edtInput.setHint("input  khong duoc de trong  ");

                } else {

                    //listSetting.add(input);
                    //  rcPreset.setAdapter(adapter);
                    // adapter.notifyDataSetChanged();
                    edtInput.setText("");
                    getFragmentManager().popBackStack();


                    dialog.dismiss();


                }
            }
        });
        txtcanceinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                lvPreset.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);

            }
        });
        builder.setView(row);
        dialog = builder.create();
        dialog.show();

    }


}