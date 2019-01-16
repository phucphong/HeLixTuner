package com.example.hp.helixtuner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Float3;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nightonke.jellytogglebutton.JellyToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import info.hoang8f.android.segmented.SegmentedGroup;

import static com.example.hp.helixtuner.BHSVtoRGB.NoteToHue;
import static com.example.hp.helixtuner.ValidatePublic.*;


public class SettingActivity extends Fragment {

    String[] arrayPicker;
    PrefManager prefManager;
    private String subFolder = "/userdata";
    private String file = "helix.txt";
    ConVertNote conVertNote;
    AlertDialog dialog;
    Bundle mBundle = new Bundle();
    RadioButton rdbFifthsSetting, rdbFourthsSetting, rdbOctevalSetting;
    boolean anHien = false;
    TextView txtNotePalettePreset;
    ImageView imgbottomMain;
    SegmentedGroup segmentedGroupSelection, segmentedGrouppalete, segmentedGroupInterval;
    NumberPicker numberPicker;
    TextView txtNotePalete, txtNoteSection, txtPitch, txtTutorial, txtBackApp, txtvolume;
    Typeface typefont;
    JellyToggleButton swvolume;
    RadioButton rdbPalete1, rdbPalete2, rdbPalete3, rdbPalete4, rdbPalete5, rdbPalete6, rdbPalete, rdbAutomatic, rdbChomatic;
    RadioButton rdbMainPalete1, rdbMainPalete2, rdbMainPalete3, rdbMainPalete4, rdbMainPalete5, rdbMainPalete6;
    TextView txtPreset;
    Button btnmainPre, btnmainPreOctive, btnmainnext, btnmainNextOctive;


    @TargetApi(28)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seting_activity, container, false);
        rdbPalete1 = view.findViewById(R.id.rdbPalete1);
        rdbPalete2 = view.findViewById(R.id.rdbPalete2);
        rdbPalete3 = view.findViewById(R.id.rdbPalete3);
        rdbPalete4 = view.findViewById(R.id.rdbPalete4);
        rdbPalete5 = view.findViewById(R.id.rdbPalete5);
        rdbPalete6 = view.findViewById(R.id.rdbPalete6);
        rdbAutomatic = view.findViewById(R.id.rdbautomatic);
        rdbPalete = view.findViewById(R.id.rdbpalette);
        rdbChomatic = view.findViewById(R.id.rdbchromatic);
        rdbOctevalSetting = getActivity().findViewById(R.id.rdbOctaves);
        rdbFifthsSetting = getActivity().findViewById(R.id.rdbFifths);
        rdbFourthsSetting = getActivity().findViewById(R.id.rdbFourths);
        rdbMainPalete1 = getActivity().findViewById(R.id.rdbMainPalete1);
        rdbMainPalete2 = getActivity().findViewById(R.id.rdbMainPalete2);
        rdbMainPalete3 = getActivity().findViewById(R.id.rdbMainPalete3);
        rdbMainPalete4 = getActivity().findViewById(R.id.rdbMainPalete4);
        rdbMainPalete5 = getActivity().findViewById(R.id.rdbMainPalete5);
        rdbMainPalete6 = getActivity().findViewById(R.id.rdbMainPalete6);
        btnmainnext = getActivity().findViewById(R.id.btnnext);
        btnmainNextOctive = getActivity().findViewById(R.id.btnnextx2);
        btnmainPre = getActivity().findViewById(R.id.btnpre);
        btnmainPreOctive = getActivity().findViewById(R.id.btnprex2);
        txtTutorial = view.findViewById(R.id.txttutorial);
        txtBackApp = view.findViewById(R.id.txtbackapp);
        txtvolume = view.findViewById(R.id.txtvolume);
        imgbottomMain = getActivity().findViewById(R.id.imgbottom);
        txtPreset = view.findViewById(R.id.txtPreset);
        listPreset = new ArrayList<HashMap<String, Object>>();
        ListView lvPreset = getActivity().findViewById(R.id.lvPreset);
        segmentedGroupInterval = view.findViewById(R.id.segmentedInterval);
        segmentedGroupSelection = view.findViewById(R.id.segmentedSelection);
        segmentedGrouppalete = view.findViewById(R.id.segmentedPalete);
        txtNotePalete = view.findViewById(R.id.txtnotePalette);
        txtNoteSection = view.findViewById(R.id.txtnotesection);
        txtPitch = view.findViewById(R.id.txtpitch);
        txtNotePalettePreset = view.findViewById(R.id.txtNotePalettePreset);
        numberPicker = view.findViewById(R.id.numberpicker);
        swvolume = view.findViewById(R.id.tgvolum);
        txtPreset.setText(Bmpreset);
        init();
         conVertNote = new ConVertNote();

        // Set Invisible
        INVISIBLE();
        updateSetting();
        readSetttings();

        setTextColor(red, gren, blue);
        setNumberPicker();
        clickBackapp();
        clickSeggmentControlerSelecte();
        clickSeggmentControlerPalete();
        clickSementControlerInterval();
        loadGameSetting();
        setText();
        txtPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PresetActivity preset = new PresetActivity();
                fragmentTransaction.add(R.id.contentView, preset);
                fragmentTransaction.addToBackStack("preset");
                fragmentTransaction.commit();
            }
        });
        txtTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HelpHelix helpHelix = new HelpHelix();
                fragmentTransaction.add(R.id.contentView, helpHelix);
                fragmentTransaction.addToBackStack("helpHelix");
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    void playNote(int note) {
        AudioSoundPlayer audioSoundPlayer = new AudioSoundPlayer(getActivity());
        audioSoundPlayer.isNotePlaying(note);
        audioSoundPlayer.playNote(note);
        audioSoundPlayer.stopNote(note);


    }

    public void loadGameSetting() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("helix", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            int getcheckId = sharedPreferences.getInt("checked", R.id.rdbchromatic);
            int checkedInterval = sharedPreferences.getInt("checkedInterval", R.id.rdbOctaves);
            this.segmentedGroupSelection.check(getcheckId);
            this.segmentedGroupInterval.check(checkedInterval);

        } else {

            segmentedGroupSelection.check(R.id.rdbchromatic);
            // this.segmentedGroupInterval.check(R.id.rdbOctaves);
        }

    }
    void  setTextPalete1MainAndSetting(String text){
        rdbPalete1.setText(text);

        rdbMainPalete1.setText(text);

    }
    void  setTextPalete2MainAndSetting(String text){
        rdbPalete2.setText(text);

        rdbMainPalete2.setText(text);

    }
    void  setTextPalete3MainAndSetting(String text){
        rdbPalete3.setText(text);

        rdbMainPalete3.setText(text);

    }
    void  setTextPalete4MainAndSetting(String text){
        rdbPalete4.setText(text);

        rdbMainPalete4.setText(text);

    }
    void  setTextPalete5MainAndSetting(String text){
        rdbPalete5.setText(text);

        rdbMainPalete5.setText(text);

    }
    void  setTextPalete6MainAndSetting(String text){
        rdbPalete6.setText(text);

        rdbMainPalete6.setText(text);

    }


    private void setText() {

        String BMnoteSetting1 = listPreset.get(0).get(BMUS_Key_note1).toString();
        String BMnoteSetting2 = listPreset.get(0).get(BMUS_Key_note2).toString();
        String BMnoteSetting3 = listPreset.get(0).get(BMUS_Key_note3).toString();
        String BMnoteSetting4 = listPreset.get(0).get(BMUS_Key_note4).toString();
        String BMnoteSetting5 = listPreset.get(0).get(BMUS_Key_note5).toString();
        String BMnoteSetting6 = listPreset.get(0).get(BMUS_Key_note6).toString();
        String bm = listPreset.get(0).get(BMUS_Key_Preset).toString();
        rdbPalete1.setText(BMnoteSetting1);
        rdbPalete2.setText(BMnoteSetting2);
        rdbPalete3.setText(BMnoteSetting3);
        rdbPalete4.setText(BMnoteSetting4);
        rdbPalete5.setText(BMnoteSetting5);
        rdbPalete6.setText(BMnoteSetting6);
        rdbMainPalete1.setText(BMnoteSetting1);
        rdbMainPalete2.setText(BMnoteSetting2);
        rdbMainPalete3.setText(BMnoteSetting3);
        rdbMainPalete4.setText(BMnoteSetting4);
        rdbMainPalete5.setText(BMnoteSetting5);
        rdbMainPalete6.setText(BMnoteSetting6);

        txtPreset.setText(bm);


    }

    public void clickSeggmentControlerPalete() {

        segmentedGrouppalete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdbPalete1:
                        cliclRdbPalete1();
                        break;
                    case R.id.rdbPalete2:
                        cliclRdbPalete2();
                        break;
                    case R.id.rdbPalete3:
                        cliclRdbPalete3();
                        break;
                    case R.id.rdbPalete4:
                        cliclRdbPalete4();
                        break;
                    case R.id.rdbPalete5:
                        cliclRdbPalete5();
                        break;
                    case R.id.rdbPalete6:
                        cliclRdbPalete6();
                        break;
                }
            }
        });
    }


    public void clickSeggmentControlerSelecte() {
        final SegmentedGroup segmentedMainPalete = getActivity().findViewById(R.id.segmentedMainPalete);


        segmentedGroupSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdbautomatic:


                        btnmainnext.setVisibility(View.INVISIBLE);
                        btnmainNextOctive.setVisibility(View.INVISIBLE);
                        btnmainPre.setVisibility(View.INVISIBLE);
                        btnmainPreOctive.setVisibility(View.INVISIBLE);
                        txtNotePalete.setVisibility(View.INVISIBLE);
                        segmentedGrouppalete.setVisibility(View.INVISIBLE);
                        segmentedGroupInterval.setVisibility(View.INVISIBLE);
                        segmentedMainPalete.setVisibility(View.INVISIBLE);
                        numberPicker.setVisibility(View.INVISIBLE);
                        txtNotePalettePreset.setVisibility(View.INVISIBLE);
                        rdbAutomatic.setChecked(true);
                        rdbChomatic.setChecked(false);
                        rdbPalete.setChecked(false);
                        txtPreset.setVisibility(View.INVISIBLE);
                        imgbottomMain.setVisibility(View.VISIBLE);

                        break;
                    case R.id.rdbpalette:


                        txtNotePalete.setVisibility(View.VISIBLE);
                        txtNotePalete.setText("Note palette:");
                        btnmainnext.setVisibility(View.INVISIBLE);
                        btnmainNextOctive.setVisibility(View.INVISIBLE);
                        btnmainPre.setVisibility(View.INVISIBLE);
                        btnmainPreOctive.setVisibility(View.INVISIBLE);
                        imgbottomMain.setVisibility(View.INVISIBLE);
                        segmentedMainPalete.setVisibility(View.VISIBLE);
                        segmentedGrouppalete.setVisibility(View.VISIBLE);
                        txtNotePalettePreset.setVisibility(View.VISIBLE);
                        numberPicker.setVisibility(View.VISIBLE);
                        segmentedGroupInterval.setVisibility(View.INVISIBLE);
                        txtNotePalettePreset.setVisibility(View.VISIBLE);
                        txtPreset.setVisibility(View.VISIBLE);


                        break;
                    case R.id.rdbchromatic:

                        btnmainnext.setVisibility(View.VISIBLE);
                        btnmainNextOctive.setVisibility(View.VISIBLE);
                        btnmainPre.setVisibility(View.VISIBLE);
                        btnmainPreOctive.setVisibility(View.VISIBLE);

                        txtNotePalete.setVisibility(View.VISIBLE);
                        segmentedMainPalete.setVisibility(View.INVISIBLE);
                        segmentedGroupInterval.setVisibility(View.VISIBLE);
                        numberPicker.setVisibility(View.INVISIBLE);
                        txtNotePalettePreset.setVisibility(View.INVISIBLE);
                        segmentedGrouppalete.setVisibility(View.INVISIBLE);
                        segmentedMainPalete.setVisibility(View.INVISIBLE);
                        txtPreset.setVisibility(View.INVISIBLE);
                        imgbottomMain.setVisibility(View.VISIBLE);

                        txtNotePalete.setText("Interval shift for <<  >> and buttons:");
                        break;


                    // Nothing to do
                }


            }
        });

    }

    public void clickSementControlerInterval() {
        ImageView imgMainBottom = getActivity().findViewById(R.id.imgbottom);
        segmentedGroupInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdbOctaves:
                        chormatic = 12;

                        break;
                    case R.id.rdbFourths:
                        chormatic = 4;

                        break;
                    case R.id.rdbFifths:

                        chormatic = 7;

                        break;


                    // Nothing to do
                }
            }
        });


    }





    public void updateSetting() {
        HashMap<String, Object> helix1 = new HashMap<String, Object>();
        helix1.clear();
        helix1.put(BMUS_Key_Preset, "Brass Instruments");
        helix1.put(BMUS_Key_note1, "C2");
        helix1.put(BMUS_Key_note2, "Eb4");
        helix1.put(BMUS_Key_note3, "F4");
        helix1.put(BMUS_Key_note4, "Bb4");
        helix1.put(BMUS_Key_note5, "Eb5");
        helix1.put(BMUS_Key_note6, "F5");
        HashMap<String, Object> helix2 = new HashMap<String, Object>();
        helix2.put(BMUS_Key_Preset, "Guitar - Drop D");
        helix2.put(BMUS_Key_note1, "D2");
        helix2.put(BMUS_Key_note2, "A2");
        helix2.put(BMUS_Key_note3, "D3");
        helix2.put(BMUS_Key_note4, "G3");
        helix2.put(BMUS_Key_note5, "B4");
        helix2.put(BMUS_Key_note6, "E4");
        HashMap<String, Object> helix3 = new HashMap<String, Object>();
        helix3.put(BMUS_Key_Preset, "Guitar - Drop C");
        helix3.put(BMUS_Key_note1, "C2");
        helix3.put(BMUS_Key_note2, "G2");
        helix3.put(BMUS_Key_note3, "C3");
        helix3.put(BMUS_Key_note4, "F3");
        helix3.put(BMUS_Key_note5, "A3");
        helix3.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix4 = new HashMap<String, Object>();
        helix4.put(BMUS_Key_Preset, "Guitar - Drop B");
        helix4.put(BMUS_Key_note1, "B1");
        helix4.put(BMUS_Key_note2, "F#2");
        helix4.put(BMUS_Key_note3, "B2");
        helix4.put(BMUS_Key_note4, "E3");
        helix4.put(BMUS_Key_note5, "Ab3");
        helix4.put(BMUS_Key_note6, "Db4");
        HashMap<String, Object> helix5 = new HashMap<String, Object>();
        helix5.put(BMUS_Key_Preset, "Guitar - Double Drop D");
        helix5.put(BMUS_Key_note1, "D1");
        helix5.put(BMUS_Key_note2, "A2");
        helix5.put(BMUS_Key_note3, "D3");
        helix5.put(BMUS_Key_note4, "G3");
        helix5.put(BMUS_Key_note5, "B3");
        helix5.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix6 = new HashMap<String, Object>();
        helix6.put(BMUS_Key_Preset, "Guitar - Standard tuning");
        helix6.put(BMUS_Key_note1, "E2");
        helix6.put(BMUS_Key_note2, "A2");
        helix6.put(BMUS_Key_note3, "D3");
        helix6.put(BMUS_Key_note4, "G3");
        helix6.put(BMUS_Key_note5, "B3");
        helix6.put(BMUS_Key_note6, "E4");
        HashMap<String, Object> helix7 = new HashMap<String, Object>();
        helix7.put(BMUS_Key_Preset, "Guitar - Open A");
        helix7.put(BMUS_Key_note1, "E2");
        helix7.put(BMUS_Key_note2, "A2");
        helix7.put(BMUS_Key_note3, "Db3");
        helix7.put(BMUS_Key_note4, "E3");
        helix7.put(BMUS_Key_note5, "A3");
        helix7.put(BMUS_Key_note6, "E4");
        HashMap<String, Object> helix8 = new HashMap<String, Object>();
        helix8.put(BMUS_Key_Preset, "Guitar - Slide Open A");
        helix8.put(BMUS_Key_note1, "E2");
        helix8.put(BMUS_Key_note2, "A2");
        helix8.put(BMUS_Key_note3, "E3");
        helix8.put(BMUS_Key_note4, "A3");
        helix8.put(BMUS_Key_note5, "Db3");
        helix8.put(BMUS_Key_note6, "E4");
        HashMap<String, Object> helix9 = new HashMap<String, Object>();
        helix9.put(BMUS_Key_Preset, "Guitar - Open C");
        helix9.put(BMUS_Key_note1, "C2");
        helix9.put(BMUS_Key_note2, "G2");
        helix9.put(BMUS_Key_note3, "C3");
        helix9.put(BMUS_Key_note4, "G3");
        helix9.put(BMUS_Key_note5, "C3");
        helix9.put(BMUS_Key_note6, "E4");
        HashMap<String, Object> helix10 = new HashMap<String, Object>();
        helix10.put(BMUS_Key_Preset, "Guitar - Open D");
        helix10.put(BMUS_Key_note1, "D2");
        helix10.put(BMUS_Key_note2, "A2");
        helix10.put(BMUS_Key_note3, "D3");
        helix10.put(BMUS_Key_note4, "F#3");
        helix10.put(BMUS_Key_note5, "A3");
        helix10.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix11 = new HashMap<String, Object>();
        helix11.put(BMUS_Key_Preset, "Guitar - Open E");
        helix11.put(BMUS_Key_note1, "E2");
        helix11.put(BMUS_Key_note2, "B2");
        helix11.put(BMUS_Key_note3, "E3");
        helix11.put(BMUS_Key_note4, "Ab3");
        helix11.put(BMUS_Key_note5, "B3");
        helix11.put(BMUS_Key_note6, "E4");
        HashMap<String, Object> helix12 = new HashMap<String, Object>();
        helix12.put(BMUS_Key_Preset, "Guitar - Open C6");
        helix12.put(BMUS_Key_note1, "D2");
        helix12.put(BMUS_Key_note2, "G2");
        helix12.put(BMUS_Key_note3, "D3");
        helix12.put(BMUS_Key_note4, "G3");
        helix12.put(BMUS_Key_note5, "B4");
        helix12.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix13 = new HashMap<String, Object>();
        helix13.put(BMUS_Key_Preset, "Guitar - Open G");
        helix13.put(BMUS_Key_note1, "D2");
        helix13.put(BMUS_Key_note2, "G2");
        helix13.put(BMUS_Key_note3, "D3");
        helix13.put(BMUS_Key_note4, "G3");
        helix13.put(BMUS_Key_note5, "B3");
        helix13.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix14 = new HashMap<String, Object>();
        helix14.put(BMUS_Key_Preset, "Guitar - DAD-GAD");
        helix14.put(BMUS_Key_note1, "D2");
        helix14.put(BMUS_Key_note2, "A2");
        helix14.put(BMUS_Key_note3, "D3");
        helix14.put(BMUS_Key_note4, "G3");
        helix14.put(BMUS_Key_note5, "A3");
        helix14.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix15 = new HashMap<String, Object>();
        helix15.put(BMUS_Key_Preset, "Guitar - DAD-ADD");
        helix15.put(BMUS_Key_note1, "D2");
        helix15.put(BMUS_Key_note2, "A2");
        helix15.put(BMUS_Key_note3, "D3");
        helix15.put(BMUS_Key_note4, "A3");
        helix15.put(BMUS_Key_note5, "D3");
        helix15.put(BMUS_Key_note6, "D4");

        HashMap<String, Object> helix16 = new HashMap<String, Object>();
        helix16.put(BMUS_Key_Preset, "Guitar - Standard D");
        helix16.put(BMUS_Key_note1, "D2");
        helix16.put(BMUS_Key_note2, "G2");
        helix16.put(BMUS_Key_note3, "C3");
        helix16.put(BMUS_Key_note4, "F3");
        helix16.put(BMUS_Key_note5, "A3");
        helix16.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix17 = new HashMap<String, Object>();
        helix17.put(BMUS_Key_Preset, "Guitar - All Fourths");
        helix17.put(BMUS_Key_note1, "E2");
        helix17.put(BMUS_Key_note2, "A2");
        helix17.put(BMUS_Key_note3, "D3");
        helix17.put(BMUS_Key_note4, "G3");
        helix17.put(BMUS_Key_note5, "C4");
        helix17.put(BMUS_Key_note6, "F4");
        HashMap<String, Object> helix18 = new HashMap<String, Object>();
        helix18.put(BMUS_Key_Preset, "Guitar - All Fifths");
        helix18.put(BMUS_Key_note1, "C2");
        helix18.put(BMUS_Key_note2, "G2");
        helix18.put(BMUS_Key_note3, "D3");
        helix18.put(BMUS_Key_note4, "A3");
        helix18.put(BMUS_Key_note5, "E4");
        helix18.put(BMUS_Key_note6, "B4");
        HashMap<String, Object> helix19 = new HashMap<String, Object>();
        helix19.put(BMUS_Key_Preset, "Strings - Violin");
        helix19.put(BMUS_Key_note1, "G3");
        helix19.put(BMUS_Key_note2, "D4");
        helix19.put(BMUS_Key_note3, "A4");
        helix19.put(BMUS_Key_note4, "E5");
        helix19.put(BMUS_Key_note5, "DIS");
        helix19.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix20 = new HashMap<String, Object>();
        helix20.put(BMUS_Key_Preset, "Strings - Viola");
        helix20.put(BMUS_Key_note1, "C3");
        helix20.put(BMUS_Key_note2, "G4");
        helix20.put(BMUS_Key_note3, "D4");
        helix20.put(BMUS_Key_note4, "A4");
        helix20.put(BMUS_Key_note5, "DIS");
        helix20.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix21 = new HashMap<String, Object>();
        helix21.put(BMUS_Key_Preset, "Strings - Cello");
        helix21.put(BMUS_Key_note1, "C2");
        helix21.put(BMUS_Key_note2, "G2");
        helix21.put(BMUS_Key_note3, "D3");
        helix21.put(BMUS_Key_note4, "A3");
        helix21.put(BMUS_Key_note5, "DIS");
        helix21.put(BMUS_Key_note6, "DIS");


        HashMap<String, Object> helix22 = new HashMap<String, Object>();
        helix22.put(BMUS_Key_Preset, "Ukelele - Standard C");
        helix22.put(BMUS_Key_note1, "G4");
        helix22.put(BMUS_Key_note2, "C4");
        helix22.put(BMUS_Key_note3, "E4");
        helix22.put(BMUS_Key_note4, "A4");
        helix22.put(BMUS_Key_note5, "DIS");
        helix22.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix23 = new HashMap<String, Object>();
        helix23.put(BMUS_Key_Preset, "Ukelele - Standard D");
        helix23.put(BMUS_Key_note1, "A4");
        helix23.put(BMUS_Key_note2, "D4");
        helix23.put(BMUS_Key_note3, "F#4");
        helix23.put(BMUS_Key_note4, "B4");
        helix23.put(BMUS_Key_note5, "DIS");
        helix23.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix24 = new HashMap<String, Object>();
        helix24.put(BMUS_Key_Preset, "Ukelele - Tenor C");
        helix24.put(BMUS_Key_note1, "G3");
        helix24.put(BMUS_Key_note2, "C4");
        helix24.put(BMUS_Key_note3, "E4");
        helix24.put(BMUS_Key_note4, "A4");
        helix24.put(BMUS_Key_note5, "DIS");
        helix24.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix25 = new HashMap<String, Object>();
        helix25.put(BMUS_Key_Preset, "Ukelele - Baritone G");
        helix25.put(BMUS_Key_note1, "D3");
        helix25.put(BMUS_Key_note2, "G3");
        helix25.put(BMUS_Key_note3, "B3");
        helix25.put(BMUS_Key_note4, "E4");
        helix25.put(BMUS_Key_note5, "DIS");
        helix25.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix26 = new HashMap<String, Object>();
        helix26.put(BMUS_Key_Preset, "Ukelele - Tenor G");
        helix26.put(BMUS_Key_note1, "D4");
        helix26.put(BMUS_Key_note2, "G3");
        helix26.put(BMUS_Key_note3, "B3");
        helix26.put(BMUS_Key_note4, "E4");
        helix26.put(BMUS_Key_note5, "DIS");
        helix26.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix27 = new HashMap<String, Object>();
        helix27.put(BMUS_Key_Preset, "Ukelele - 5 String(Doubled 4th)");
        helix27.put(BMUS_Key_note1, "G4");
        helix27.put(BMUS_Key_note2, "G3");
        helix27.put(BMUS_Key_note3, "C4");
        helix27.put(BMUS_Key_note4, "E4");
        helix27.put(BMUS_Key_note5, "A4");
        helix27.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix28 = new HashMap<String, Object>();
        helix28.put(BMUS_Key_Preset, "Ukelele - 5 String(Doubled 3th)");
        helix28.put(BMUS_Key_note1, "G4");
        helix28.put(BMUS_Key_note2, "C4");
        helix28.put(BMUS_Key_note3, "C3");
        helix28.put(BMUS_Key_note4, "E4");
        helix28.put(BMUS_Key_note5, "A4");
        helix28.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix29 = new HashMap<String, Object>();
        helix29.put(BMUS_Key_Preset, "Ukelele - 5 String(Doubled 1st)");
        helix29.put(BMUS_Key_note1, "G4");
        helix29.put(BMUS_Key_note2, "C4");
        helix29.put(BMUS_Key_note3, "E4");
        helix29.put(BMUS_Key_note4, "A3");
        helix29.put(BMUS_Key_note5, "A4");
        helix29.put(BMUS_Key_note6, "DIS");
        HashMap<String, Object> helix30 = new HashMap<String, Object>();
        helix30.put(BMUS_Key_Preset, "Bass - BEADGC");
        helix30.put(BMUS_Key_note1, "B0");
        helix30.put(BMUS_Key_note2, "E1");
        helix30.put(BMUS_Key_note3, "A1");
        helix30.put(BMUS_Key_note4, "D2");
        helix30.put(BMUS_Key_note5, "G2");
        helix30.put(BMUS_Key_note6, "C3");
        HashMap<String, Object> helix31 = new HashMap<String, Object>();
        helix31.put(BMUS_Key_Preset, "Banjo - Open G");
        helix31.put(BMUS_Key_note1, "G4");
        helix31.put(BMUS_Key_note2, "D3");
        helix31.put(BMUS_Key_note3, "G3");
        helix31.put(BMUS_Key_note4, "B3");
        helix31.put(BMUS_Key_note5, "D4");
        helix31.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix32 = new HashMap<String, Object>();
        helix32.put(BMUS_Key_Preset, "Banjo - C");
        helix32.put(BMUS_Key_note1, "G4");
        helix32.put(BMUS_Key_note2, "c3");
        helix32.put(BMUS_Key_note3, "G3");
        helix32.put(BMUS_Key_note4, "B3");
        helix32.put(BMUS_Key_note5, "D4");
        helix32.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix33 = new HashMap<String, Object>();
        helix33.put(BMUS_Key_Preset, "Banjo - Standard Plectrum");
        helix33.put(BMUS_Key_note1, "C3");
        helix33.put(BMUS_Key_note2, "G3");
        helix33.put(BMUS_Key_note3, "B3");
        helix33.put(BMUS_Key_note4, "D4");
        helix33.put(BMUS_Key_note5, "none");
        helix33.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix34 = new HashMap<String, Object>();
        helix34.put(BMUS_Key_Preset, "Banjo - Standard Plectrum");
        helix34.put(BMUS_Key_note1, "C3");
        helix34.put(BMUS_Key_note2, "G3");
        helix34.put(BMUS_Key_note3, "B3");
        helix34.put(BMUS_Key_note4, "D4");
        helix34.put(BMUS_Key_note5, "none");
        helix34.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix35 = new HashMap<String, Object>();
        helix35.put(BMUS_Key_Preset, "Banjo - Standard Tenor Jazz");
        helix35.put(BMUS_Key_note1, "C3");
        helix35.put(BMUS_Key_note2, "G3");
        helix35.put(BMUS_Key_note3, "D4");
        helix35.put(BMUS_Key_note4, "A4");
        helix35.put(BMUS_Key_note5, "none");
        helix35.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix36 = new HashMap<String, Object>();
        helix36.put(BMUS_Key_Preset, "Banjo - Irish Tenor");
        helix36.put(BMUS_Key_note1, "G2");
        helix36.put(BMUS_Key_note2, "D3");
        helix36.put(BMUS_Key_note3, "A3");
        helix36.put(BMUS_Key_note4, "E4");
        helix36.put(BMUS_Key_note5, "none");
        helix36.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix37 = new HashMap<String, Object>();
        helix37.put(BMUS_Key_Preset, "Banjo - Open String");
        helix37.put(BMUS_Key_note1, "G4");
        helix37.put(BMUS_Key_note2, "B2");
        helix37.put(BMUS_Key_note3, "E3");
        helix37.put(BMUS_Key_note4, "Ab3");
        helix37.put(BMUS_Key_note5, "B3");
        helix37.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix38 = new HashMap<String, Object>();
        helix38.put(BMUS_Key_Preset, "Banjo - Bass");
        helix38.put(BMUS_Key_note1, "E1");
        helix38.put(BMUS_Key_note2, "A1");
        helix38.put(BMUS_Key_note3, "D2");
        helix38.put(BMUS_Key_note4, "G2");
        helix38.put(BMUS_Key_note5, "none");
        helix38.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix39 = new HashMap<String, Object>();
        helix39.put(BMUS_Key_Preset, "Banjo - Cello");
        helix39.put(BMUS_Key_note1, "C2");
        helix39.put(BMUS_Key_note2, "G2");
        helix39.put(BMUS_Key_note3, "D3");
        helix39.put(BMUS_Key_note4, "A3");
        helix39.put(BMUS_Key_note5, "none");
        helix39.put(BMUS_Key_note6, "none");


        listPreset.add(helix1);
        listPreset.add(helix2);
        listPreset.add(helix3);
        listPreset.add(helix4);
        listPreset.add(helix5);
        listPreset.add(helix6);
        listPreset.add(helix7);
        listPreset.add(helix8);
        listPreset.add(helix9);
        listPreset.add(helix10);
        listPreset.add(helix11);
        listPreset.add(helix12);
        listPreset.add(helix13);
        listPreset.add(helix14);
        listPreset.add(helix15);
        listPreset.add(helix16);
        listPreset.add(helix17);
        listPreset.add(helix18);


        listPreset.add(helix19);
        listPreset.add(helix20);
        listPreset.add(helix21);
        listPreset.add(helix22);
        listPreset.add(helix23);
        listPreset.add(helix24);
        listPreset.add(helix25);
        listPreset.add(helix26);
        listPreset.add(helix27);
        listPreset.add(helix28);
        listPreset.add(helix29);
        listPreset.add(helix30);
        listPreset.add(helix31);
        listPreset.add(helix32);
        listPreset.add(helix33);
        listPreset.add(helix33);
        listPreset.add(helix34);
        listPreset.add(helix35);
        listPreset.add(helix36);
        listPreset.add(helix37);
        listPreset.add(helix38);
        listPreset.add(helix39);


        writeSettings();


    }

    private void writeSettings() {

        File cacheDir = null;
        File appDirectory = null;

        if (android.os.Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = getActivity().getApplicationContext().getExternalCacheDir();
            appDirectory = new File(cacheDir + subFolder);

        } else {
            cacheDir = getActivity().getApplicationContext().getCacheDir();
            String BaseFolder = cacheDir.getAbsolutePath();
            appDirectory = new File(BaseFolder + subFolder);

        }

        if (appDirectory != null && !appDirectory.exists()) {
            appDirectory.mkdirs();
        }

        File fileName = new File(appDirectory, file);

        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            out.writeObject(listPreset);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.flush();
                fos.close();
                if (out != null)
                    out.flush();
                out.close();
            } catch (Exception e) {

            }
        }
    }

    public void readSetttings() {
        File cacheDir = null;
        File appDirectory = null;
        if (android.os.Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = getActivity().getApplicationContext().getExternalCacheDir();
            appDirectory = new File(cacheDir + subFolder);
        } else {
            cacheDir = getActivity().getApplicationContext().getCacheDir();
            String BaseFolder = cacheDir.getAbsolutePath();
            appDirectory = new File(BaseFolder + subFolder);
        }

        if (appDirectory != null && !appDirectory.exists()) return; // File does not exist

        File fileName = new File(appDirectory, file);

        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            ArrayList<HashMap<String, Object>> myHash = (ArrayList<HashMap<String, Object>>) in.readObject();
            // HashMap<String, Object> myHashMap = (HashMap<String, Object>) in.readObject();
            listPreset = myHash;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (fis != null) {
                    fis.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {

        typefont = Typeface.createFromAsset(getActivity().getAssets(), "font.ttf");
        txtPitch.setTypeface(typefont);
        txtNoteSection.setTypeface(typefont);
        txtNotePalete.setTypeface(typefont);
        txtvolume.setTypeface(typefont);
        txtNotePalettePreset.setTypeface(typefont);
        arrayPicker = new String[]{
                "C2", "C#/Db2", "D2", "D#/Eb2", "E2", "F2", "F#/Gb2", "G2", "G#2/Ab2", "A2", "A#Bb2", "B2",
                "C3", "C#/Db3", "D3", "D#/Eb3", "E3", "F3", "F#/Gb3", "G3", "G#3/Ab3", "A3", "A#Bb3", "B3",
                " C4", "C#/Db4", "D4", "D#/Eb4", "E4", "F4", "F#/Gb4", "G4", "G#4/Ab4", "A4", "A#Bb4", "B4",
                " C5", "C#/Db5", "D5", "D#/Eb5", "E5", "F5", "F#/Gb5", "G5", "G#5/Ab5", "A5", "A#Bb5", "B5",
                "C6", "C#/Db6", "D6", "D#/Eb6", "E6", "F6", "F#/Gb6", "G6", "G#6/Ab6", "A6", "A#Bb6", "B6",
                "C7", "C#/Db7", "D7", "D#/Eb7", "E7", "F7", "F#/Gb7", "G7", "G#7/Ab7", "A7", "A#Bb7", "B7",
                "C8", "C#/Db8", "D8", "D#/Eb8", "E8", "F8", "F#/Gb8", "G8"};
    }

    public void setNumberPicker() {
        numberPicker.setMaxValue(arrayPicker.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(arrayPicker);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference
        numberPicker.setWrapSelectorWheel(false);
    }

    @RequiresApi(api = 28)
    public int[] setTextColor(int red, int green, int blue) {
        int[] listColor = new int[3];
        listColor[0] = red;
        listColor[1] = green;
        listColor[2] = blue;
//swvolume.setlv
        swvolume.setRightBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        swvolume.setLeftBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtvolume.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        // lvPreset.setBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtPreset.setBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtNoteSection.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtBackApp.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtTutorial.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtNotePalete.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtPitch.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtNotePalettePreset.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        setNumberPickerTextColor(numberPicker, Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentedGroupInterval.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentedGroupSelection.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentedGrouppalete.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));

        return listColor;

    }

    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color) {

        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
        } catch (NoSuchFieldException e) {
            //   Log.w("setNumberPickerTextColor", e);
        } catch (IllegalAccessException e) {
            // Log.w("setNumberPickerTextColor", e);
        } catch (IllegalArgumentException e) {
            //Log.w("setNumberPickerTextColor", e);
        }

        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(color);
        }
        numberPicker.invalidate();
    }


    public void clickBackapp() {

        txtBackApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("helix", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int checkId = segmentedGroupSelection.getCheckedRadioButtonId();
                int checkidInterval = segmentedGroupInterval.getCheckedRadioButtonId();

                editor.putInt("checked", checkId);
                editor.putInt("checkedInterval", checkidInterval);
                editor.apply();
                getFragmentManager().popBackStack();


            }
        });
    }

    public void cliclRdbPalete1() {
        final ListView lvpreset = getActivity().findViewById(R.id.lvPreset);

        rdbPalete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n1 =  conVertNote.convertNameToNote(rdbPalete1.getText().toString());
                playNote(n1);


                numberPicker.setValue(n1);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                    @TargetApi(28)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //    number1 = newVal;

                        note1 = newVal;
                        SlectNumber1 = arrayPicker[newVal];
                        rdbPalete1.setText(SlectNumber1);
                        //SegmentedGroup segmentedGroupMainPlate = getActivity().
                        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
                        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.2f, 1.0f);

                        red1 = (int) (color.x * 255);
                        blue1 = (int) (color.z * 255);
                        gren1 = (int) (color.y * 255);
                        setTextColor(red1, gren1, blue1);
                        playNote(note1);
                        setTextPalete1MainAndSetting(SlectNumber1);
                        // lvpreset.setBackgroundColor(Color.rgb(red1,gren1,blue1));

                        Log.e("sl", SlectNumber1);


                    }

                });


            }
        });
    }


    public void cliclRdbPalete2() {
        rdbPalete2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int n2 = conVertNote.convertNameToNote(rdbPalete2.getText().toString());
                playNote(n2);
                numberPicker.setValue(n2);
                Log.e("n", String.valueOf(n2));
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @TargetApi(28)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //number2 = newVal;
                        rdbPalete2.setText(arrayPicker[newVal]);
                        note2 = newVal;
                        SlectNumber2 = arrayPicker[newVal];
                        playNote(note2);
                        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
                        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.2f, 1.0f);
                        red2 = (int) (color.x * 255);
                        blue2 = (int) (color.z * 255);
                        gren2 = (int) (color.y * 255);
                        setTextColor(red2, gren2, blue2);
                        setTextPalete2MainAndSetting(SlectNumber2);

                    }
                });

            }
        });
    }

    public void cliclRdbPalete3() {
        rdbPalete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n3 = conVertNote.convertNameToNote(rdbPalete3.getText().toString());
                playNote(n3);
                numberPicker.setValue(n3);
                Log.e("n", String.valueOf(n3));
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @TargetApi(28)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //  number3 = newVal;
                        rdbPalete3.setText(arrayPicker[newVal]);
                        note3 = newVal;
                        SlectNumber3 = arrayPicker[newVal];
                        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
                        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.2f, 1.0f);
                        red3 = (int) (color.x * 255);
                        blue3 = (int) (color.z * 255);
                        gren3 = (int) (color.y * 255);
                        setTextColor(red3, gren3, blue3);
                        playNote(note3);
                        setTextPalete3MainAndSetting(SlectNumber3);
                    }
                });

            }
        });
    }

    public void cliclRdbPalete4() {
        rdbPalete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n4 = conVertNote.convertNameToNote(rdbPalete4.getText().toString());
                playNote(n4);
                numberPicker.setValue(n4);
                Log.e("n", String.valueOf(n4));
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @TargetApi(28)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //number4 = newVal;
                        rdbPalete4.setText(arrayPicker[newVal]);
                        note4 = newVal;
                        SlectNumber4 = arrayPicker[newVal];
                        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
                        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.2f, 1.0f);
                        red4 = (int) (color.x * 255);
                        blue4 = (int) (color.z * 255);
                        gren4 = (int) (color.y * 255);
                        playNote(note4);
                        setTextColor(red4, gren4, blue4);
                        setTextPalete4MainAndSetting(SlectNumber4);

                    }
                });

            }
        });

    }

    public void cliclRdbPalete5() {
        rdbPalete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n5 = conVertNote.convertNameToNote(rdbPalete5.getText().toString());
                playNote(n5);
                numberPicker.setValue(n5);
                Log.e("n", String.valueOf(n5));
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @TargetApi(28)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        // number5 = newVal;

                        note5 = newVal;
                        SlectNumber5 = arrayPicker[newVal];
                        rdbPalete5.setText(arrayPicker[newVal]);
                        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
                        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.2f, 1.0f);
                        red5 = (int) (color.x * 255);
                        blue5 = (int) (color.z * 255);
                        gren5 = (int) (color.y * 255);
                        playNote(note5);
                        setTextColor(red5, gren5, blue5);
                        setTextPalete5MainAndSetting(SlectNumber5);
                    }
                });

            }
        });

    }

    public void cliclRdbPalete6() {
        rdbPalete6.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int n6 = conVertNote.convertNameToNote(rdbPalete6.getText().toString());
                playNote(n6);
                numberPicker.setValue(n6);
                Log.e("n", String.valueOf(n6));
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @TargetApi(28)
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //number6 = newVal;

                        note6 = newVal;
                        SlectNumber6 = arrayPicker[newVal];
                        rdbPalete6.setText(arrayPicker[newVal]);
                        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
                        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.2f, 1.0f);
                        red6 = (int) (color.x * 255);
                        blue6 = (int) (color.z * 255);
                        gren6 = (int) (color.y * 255);
                        setTextColor(red6, gren6, blue6);
                        playNote(note6);
                        setTextPalete6MainAndSetting(SlectNumber6);

                    }
                });

            }
        });
    }

    public void INVISIBLE() {
        txtNotePalete.setVisibility(View.INVISIBLE);
        txtNotePalete.setVisibility(View.INVISIBLE);
        segmentedGroupInterval.setVisibility(View.INVISIBLE);
        segmentedGrouppalete.setVisibility(View.INVISIBLE);
        txtNotePalettePreset.setVisibility(View.INVISIBLE);
        numberPicker.setVisibility(View.INVISIBLE);
        txtPreset.setVisibility(View.INVISIBLE);


    }

}
