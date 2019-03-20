package com.example.hp.helixtuner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

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
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;


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
    SharedPreferences.Editor editor;
    String[] arrayPicker;
    SharedPreferences sharedPreferences;
    private String subFolder = "/userdata";
    private String file = "helix.txt";

    int checkId;
    int SC_DisableNote = -1;
    int checkidInterval;
    ConVertNote conVertNote;
    RadioButton rdbFifthsSetting, rdbFourthsSetting, rdbOctevalSetting, rdbpreset;
    TextView txtNotePalettePreset;
    ImageView imgbottomMain;
    SegmentedGroup segmentedGroupSelection, segmentedGrouppalete, segmentedGroupInterval;
    NumberPicker numberPicker;
    TextView txtNotePalete, txtNoteSection, txtPitch, txtTutorial, txtBackApp, txtvolume;
    Typeface typefont;
    JellyToggleButton swvolume;
    String txtPitchVolume;
    RadioButton rdbPalete1, rdbPalete2, rdbPalete3, rdbPalete4, rdbPalete5, rdbPalete6, rdbPalete, rdbAutomatic, rdbChomatic, rdbPreset;
    RadioButton rdbMainPalete1, rdbMainPalete2, rdbMainPalete3, rdbMainPalete4, rdbMainPalete5, rdbMainPalete6;
    SegmentedGroup segmentPreset;
    Button btnmainPre, btnmainPreOctive, btnmainnext, btnmainNextOctive;
    int redSetting, greenSetting, blueSetting, redTitleSetting, greenTitleSetting, blueTitleSetting;

    @TargetApi(28)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seting_activity, container, false);
        sharedPreferences = getActivity().getSharedPreferences("helix", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        init(view);
        conVertNote = new ConVertNote();
        // Set Invisible

        INVISIBLE();
        updateSetting();
        readSetttings();
        floatColorSetting(newnote);
        floatTitleSetting(newnote);

        setTextColor(redSetting, greenSetting, blueSetting);

        setTittlecolor(redTitleSetting, greenTitleSetting, blueTitleSetting);
        // Log.e("notesetting", String.valueOf(startnote));
        setNumberPicker();
        clickBackapp();
        clickSeggmentControlerSelecte();
        clickSeggmentControlerPalete();
        clickSementControlerInterval();
        loadGameSetting();
        setText();
        rdbpreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PresetActivity preset = new PresetActivity();


                fragmentTransaction.add(R.id.contentView, preset, "presetactivity");
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
        checkId = segmentedGroupSelection.getCheckedRadioButtonId();
        checkidInterval = segmentedGroupInterval.getCheckedRadioButtonId();


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void floatColorSetting(int note) {
        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(note) + 2.0f / 12.0f), 0.45f, 1.0f);

        redSetting = (int) (color.x * 255);
        greenSetting = (int) (color.y * 255);
        blueSetting = (int) (color.z * 255);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void floatTitleSetting(int note) {
        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(note) + 2.0f / 12.0f), 0.2f, 1.0f);

        redTitleSetting = (int) (color.x * 255);
        greenTitleSetting = (int) (color.y * 255);
        blueTitleSetting = (int) (color.z * 255);
    }


    void playNote(int note) {
        AudioSoundPlayer audioSoundPlayer = new AudioSoundPlayer(getActivity());
        audioSoundPlayer.isNotePlaying(note);
        audioSoundPlayer.playNote(note);
        audioSoundPlayer.stopNote(note);


    }

    public void loadGameSetting() {

        if (sharedPreferences != null) {
            int getcheckId = sharedPreferences.getInt("checked", R.id.rdbchromatic);
            int checkedInterval = sharedPreferences.getInt("checkedInterval", R.id.rdbOctaves);
            this.segmentedGroupSelection.check(getcheckId);
            this.segmentedGroupInterval.check(checkedInterval);

        } else {

            segmentedGroupSelection.check(R.id.rdbchromatic);
            segmentedGroupInterval.check(R.id.rdbOctaves);
        }
    }

    void setTextPalete1MainAndSetting(String text) {
        rdbPalete1.setText(text);

        rdbMainPalete1.setText(text);

    }

    void setTextPalete2MainAndSetting(String text) {
        rdbPalete2.setText(text);

        rdbMainPalete2.setText(text);

    }

    void setTextPalete3MainAndSetting(String text) {
        rdbPalete3.setText(text);

        rdbMainPalete3.setText(text);

    }

    void setTextPalete4MainAndSetting(String text) {
        rdbPalete4.setText(text);

        rdbMainPalete4.setText(text);

    }

    void setTextPalete5MainAndSetting(String text) {
        rdbPalete5.setText(text);

        rdbMainPalete5.setText(text);

    }

    void setTextPalete6MainAndSetting(String text) {
        rdbPalete6.setText(text);

        rdbMainPalete6.setText(text);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    private void setText() {

        BMpreset = listPreset.get(5).get(BMUS_Key_Preset).toString();
        BMNote1 = listPreset.get(5).get(BMUS_Key_note1).toString();
        BMNote2 = listPreset.get(5).get(BMUS_Key_note2).toString();
        BMNote3 = listPreset.get(5).get(BMUS_Key_note3).toString();
        BMNote4 = listPreset.get(5).get(BMUS_Key_note4).toString();
        BMNote5 = listPreset.get(5).get(BMUS_Key_note5).toString();
        BMNote6 = listPreset.get(5).get(BMUS_Key_note6).toString();


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


        rdbpreset.setText(BMpreset);


    }

    public void clickSeggmentControlerPalete() {

        segmentedGrouppalete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
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

                editor.putInt("visible", View.VISIBLE);
                editor.putInt("invisible", View.INVISIBLE);

                editor.commit();

                switch (checkedId) {


                    case R.id.rdbautomatic:

                        imgbottomMain.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        btnmainnext.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        btnmainNextOctive.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        btnmainPre.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        btnmainPreOctive.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        txtNotePalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentedGrouppalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentedGroupInterval.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentedMainPalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        numberPicker.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        txtNotePalettePreset.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentPreset.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));


                        break;
                    case R.id.rdbpalette:


                        txtNotePalettePreset.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        segmentPreset.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        txtNotePalete.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        segmentedGrouppalete.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        txtNotePalettePreset.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        numberPicker.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        segmentedMainPalete.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        txtNotePalete.setText("Note palette:");
                        btnmainnext.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        btnmainNextOctive.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        btnmainPre.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        btnmainPreOctive.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        imgbottomMain.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));

                        segmentedGroupInterval.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));


                        break;
                    case R.id.rdbchromatic:

                        btnmainnext.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        btnmainNextOctive.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        btnmainPre.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        btnmainPreOctive.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        imgbottomMain.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        txtNotePalete.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        segmentedMainPalete.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        segmentedGroupInterval.setVisibility(sharedPreferences.getInt("visible", View.VISIBLE));
                        numberPicker.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        txtNotePalettePreset.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentedGrouppalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentedMainPalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
                        segmentPreset.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));


                        txtNotePalete.setText("Interval shift for << and >>  buttons:");
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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (checkedId) {
                    case R.id.rdbOctaves:

                        chormatic = 12;

                        editor.putInt("chomatic", chormatic);
                        editor.putInt("checkedInterval", checkidInterval);
                        editor.commit();


                        break;
                    case R.id.rdbFourths:
                        chormatic = 4;

                        editor.putInt("chomatic", chormatic);
                        editor.putInt("checkedInterval", checkidInterval);
                        editor.commit();

                        break;
                    case R.id.rdbFifths:

                        chormatic = 7;
                        editor.putInt("chomatic", chormatic);
                        editor.putInt("checkedInterval", checkidInterval);
                        editor.commit();


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
        helix1.put(BMUS_Key_note1, "A#/B♭3");
        helix1.put(BMUS_Key_note2, "D#/E♭4");
        helix1.put(BMUS_Key_note3, "F4");
        helix1.put(BMUS_Key_note4, "D#/E♭4");
        helix1.put(BMUS_Key_note5, "D#/E♭5");
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
        helix4.put(BMUS_Key_note2, "F#/G♭2");
        helix4.put(BMUS_Key_note3, "B2");
        helix4.put(BMUS_Key_note4, "E3");
        helix4.put(BMUS_Key_note5, "G#/A♭3");
        helix4.put(BMUS_Key_note6, "C#/D♭4");
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
        helix6.put(BMUS_Key_note1, "E4");
        helix6.put(BMUS_Key_note2, "A4");
        helix6.put(BMUS_Key_note3, "D5");
        helix6.put(BMUS_Key_note4, "G5");
        helix6.put(BMUS_Key_note5, "B5");
        helix6.put(BMUS_Key_note6, "E6");
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
        helix8.put(BMUS_Key_note5, "C#/D♭3");
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
        helix10.put(BMUS_Key_note4, "F#/G♭3");
        helix10.put(BMUS_Key_note5, "A3");
        helix10.put(BMUS_Key_note6, "D4");
        HashMap<String, Object> helix11 = new HashMap<String, Object>();
        helix11.put(BMUS_Key_Preset, "Guitar - Open E");
        helix11.put(BMUS_Key_note1, "E2");
        helix11.put(BMUS_Key_note2, "B2");
        helix11.put(BMUS_Key_note3, "E3");
        helix11.put(BMUS_Key_note4, "G#/A♭3");
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
        helix19.put(BMUS_Key_note5, "none");
        helix19.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix20 = new HashMap<String, Object>();
        helix20.put(BMUS_Key_Preset, "Strings - Viola");
        helix20.put(BMUS_Key_note1, "C3");
        helix20.put(BMUS_Key_note2, "G4");
        helix20.put(BMUS_Key_note3, "D4");
        helix20.put(BMUS_Key_note4, "A4");
        helix20.put(BMUS_Key_note5, "none");
        helix20.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix21 = new HashMap<String, Object>();
        helix21.put(BMUS_Key_Preset, "Strings - Cello");
        helix21.put(BMUS_Key_note1, "C2");
        helix21.put(BMUS_Key_note2, "G2");
        helix21.put(BMUS_Key_note3, "D3");
        helix21.put(BMUS_Key_note4, "A3");
        helix21.put(BMUS_Key_note5, "none");
        helix21.put(BMUS_Key_note6, "none");


        HashMap<String, Object> helix22 = new HashMap<String, Object>();
        helix22.put(BMUS_Key_Preset, "Ukelele - Standard C");
        helix22.put(BMUS_Key_note1, "G4");
        helix22.put(BMUS_Key_note2, "C4");
        helix22.put(BMUS_Key_note3, "E4");
        helix22.put(BMUS_Key_note4, "A4");
        helix22.put(BMUS_Key_note5, "none");
        helix22.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix23 = new HashMap<String, Object>();
        helix23.put(BMUS_Key_Preset, "Ukelele - Standard D");
        helix23.put(BMUS_Key_note1, "A4");
        helix23.put(BMUS_Key_note2, "D4");
        helix23.put(BMUS_Key_note3, "F#/G♭4");
        helix23.put(BMUS_Key_note4, "B4");
        helix23.put(BMUS_Key_note5, "none");
        helix23.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix24 = new HashMap<String, Object>();
        helix24.put(BMUS_Key_Preset, "Ukelele - Tenor C");
        helix24.put(BMUS_Key_note1, "G3");
        helix24.put(BMUS_Key_note2, "C4");
        helix24.put(BMUS_Key_note3, "E4");
        helix24.put(BMUS_Key_note4, "A4");
        helix24.put(BMUS_Key_note5, "none");
        helix24.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix25 = new HashMap<String, Object>();
        helix25.put(BMUS_Key_Preset, "Ukelele - Baritone G");
        helix25.put(BMUS_Key_note1, "D3");
        helix25.put(BMUS_Key_note2, "G3");
        helix25.put(BMUS_Key_note3, "B3");
        helix25.put(BMUS_Key_note4, "E4");
        helix25.put(BMUS_Key_note5, "none");
        helix25.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix26 = new HashMap<String, Object>();
        helix26.put(BMUS_Key_Preset, "Ukelele - Tenor G");
        helix26.put(BMUS_Key_note1, "D4");
        helix26.put(BMUS_Key_note2, "G3");
        helix26.put(BMUS_Key_note3, "B3");
        helix26.put(BMUS_Key_note4, "E4");
        helix26.put(BMUS_Key_note5, "none");
        helix26.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix27 = new HashMap<String, Object>();
        helix27.put(BMUS_Key_Preset, "Ukelele - 5 String(Doubled 4th)");
        helix27.put(BMUS_Key_note1, "G4");
        helix27.put(BMUS_Key_note2, "G3");
        helix27.put(BMUS_Key_note3, "C4");
        helix27.put(BMUS_Key_note4, "E4");
        helix27.put(BMUS_Key_note5, "A4");
        helix27.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix28 = new HashMap<String, Object>();
        helix28.put(BMUS_Key_Preset, "Ukelele - 5 String(Doubled 3th)");
        helix28.put(BMUS_Key_note1, "G4");
        helix28.put(BMUS_Key_note2, "C4");
        helix28.put(BMUS_Key_note3, "C3");
        helix28.put(BMUS_Key_note4, "E4");
        helix28.put(BMUS_Key_note5, "A4");
        helix28.put(BMUS_Key_note6, "none");
        HashMap<String, Object> helix29 = new HashMap<String, Object>();
        helix29.put(BMUS_Key_Preset, "Ukelele - 5 String(Doubled 1st)");
        helix29.put(BMUS_Key_note1, "G4");
        helix29.put(BMUS_Key_note2, "C4");
        helix29.put(BMUS_Key_note3, "E4");
        helix29.put(BMUS_Key_note4, "A3");
        helix29.put(BMUS_Key_note5, "A4");
        helix29.put(BMUS_Key_note6, "none");
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
        helix37.put(BMUS_Key_note4, "G#/A♭3");
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

    public void init(View view) {
        rdbPreset = view.findViewById(R.id.rdbPreset);
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
        rdbpreset = view.findViewById(R.id.rdbPreset);
        listPreset = new ArrayList<HashMap<String, Object>>();
        ListView lvPreset = getActivity().findViewById(R.id.lvPreset);
        segmentedGroupInterval = view.findViewById(R.id.segmentedInterval);
        segmentedGroupSelection = view.findViewById(R.id.segmentedSelection);
        segmentedGrouppalete = view.findViewById(R.id.segmentedPalete);
        segmentPreset = view.findViewById(R.id.segmentedPreset);
        txtNotePalete = view.findViewById(R.id.txtnotePalette);
        txtNoteSection = view.findViewById(R.id.txtnotesection);

        txtNotePalettePreset = view.findViewById(R.id.txtNotePalettePreset);
        numberPicker = view.findViewById(R.id.numberpicker);
        swvolume = view.findViewById(R.id.tgvolum);

        typefont = Typeface.createFromAsset(getActivity().getAssets(), "font.ttf");

        txtNoteSection.setTypeface(typefont);
        txtNotePalete.setTypeface(typefont);
        txtvolume.setTypeface(typefont);
        txtNotePalettePreset.setTypeface(typefont);

        arrayPicker = new String[]{
                "C2", "C#/D♭2", "D2", "D#/E♭2", "E2", "F2", "F#/G♭2", "G2", "G#/A♭2", "A2", "A#/B♭2", "B2",
                "C3", "C#/D♭3", "D3", "D#/E♭3", "E3", "F3", "F#/G♭3", "G3", "G#/A♭3", "A3", "A#/B♭3", "B3",
                "C4", "C#/D♭4", "D4", "D#/E♭4", "E4", "F4", "F#/G♭4", "G4", "G#/A♭4", "A4", "A#/B♭4", "B4",
                "C5", "C#/D♭5", "D5", "D#/E♭5", "E5", "F5", "F#/G♭5", "G5", "G#/A♭5", "A5", "A#/B♭5", "B5",
                "C6", "C#/D♭6", "D6", "D#/E♭6", "E6", "F6", "F#/G♭6", "G6", "G#/A♭6", "A6", "A#/B♭6", "B6",
                "C7", "C#/D♭7", "D7", "D#/E♭7", "E7", "F7", "F#/G♭7", "G7", "G#/A♭7", "A7", "A#/B♭7", "B7",
                "C8", "C#/D♭8", "D8", "D#/E♭8", "E8", "F8", "F#/G♭8", "G8"};
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

    int[] setTittlecolor(int red, int green, int blue) {
        int[] listColor = new int[3];
        listColor[0] = red;
        listColor[1] = green;
        listColor[2] = blue;
        txtvolume.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtNoteSection.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtNotePalettePreset.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtNotePalete.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));

        return listColor;

    }

    @RequiresApi(api = 28)
    public int[] setTextColor(int red, int green, int blue) {
        int[] listColor = new int[3];
        listColor[0] = red;
        listColor[1] = green;
        listColor[2] = blue;
        rdbpreset.setBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        swvolume.setRightBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        swvolume.setLeftBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentPreset.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtBackApp.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        txtTutorial.setTextColor(Color.rgb(listColor[0], listColor[1], listColor[2]));

        setNumberPickerTextColor(numberPicker, Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentedGroupInterval.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentedGroupSelection.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        segmentedGrouppalete.setTintColor(Color.rgb(listColor[0], listColor[1], listColor[2]));
        //rdbPreset.setBackgroundColor(Color.rgb(listColor[0], listColor[1], listColor[2]));

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


                getFragmentManager().popBackStack();


            }
        });
    }

    public void cliclRdbPalete1() {


        rdbPalete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rdbPalete1.getText().toString();
                final int n1 = conVertNote.getNoteByName(text);
                if (n1 == -1) {

                } else {
                    playNote(n1);
                    numberPicker.setValue(n1);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @TargetApi(28)
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            //    number1 = newVal;

                            SlectNumber1 = arrayPicker[newVal];
                            rdbPalete1.setText(SlectNumber1);
                            playNote(newVal);
                            setTextPalete1MainAndSetting(SlectNumber1);
                            Log.e("sl1", SlectNumber1);


                        }

                    });
                }


            }
        });
    }


    public void cliclRdbPalete2() {

        rdbPalete2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = rdbPalete2.getText().toString();
                final int n2 = conVertNote.getNoteByName(text);
                if (n2 == -1) {

                } else {
                    playNote(n2);
                    numberPicker.setValue(n2);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @TargetApi(28)
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            //    number1 = newVal;
                            SlectNumber2 = arrayPicker[newVal];
                            rdbPalete2.setText(SlectNumber2);

                            playNote(newVal);
                            setTextPalete2MainAndSetting(SlectNumber2);
                            Log.e("sl2", SlectNumber2);


                        }

                    });
                }


            }
        });
    }

    public void cliclRdbPalete3() {
        rdbPalete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rdbPalete3.getText().toString();
                int n3 = conVertNote.getNoteByName(text);
                if (n3 == -1) {

                } else {
                    playNote(n3);
                    numberPicker.setValue(n3);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @TargetApi(28)
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            //    number1 = newVal;
                            SlectNumber3 = arrayPicker[newVal];
                            rdbPalete3.setText(SlectNumber3);

                            playNote(newVal);
                            setTextPalete3MainAndSetting(SlectNumber3);
                            Log.e("sl3", SlectNumber3);


                        }

                    });
                }


            }
        });
    }

    @TargetApi(28)
    @RequiresApi(api = Build.VERSION_CODES.O)


    public void cliclRdbPalete4() {
        rdbPalete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rdbPalete4.getText().toString();
                final int n4 = conVertNote.getNoteByName(text);
                if (n4 == -1) {

                } else {
                    playNote(n4);
                    numberPicker.setValue(n4);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @TargetApi(28)
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                            SlectNumber4 = arrayPicker[newVal];
                            rdbPalete4.setText(SlectNumber4);

                            playNote(newVal);
                            setTextPalete4MainAndSetting(SlectNumber4);
                            Log.e("sl4", SlectNumber4);


                        }

                    });
                }


            }
        });

    }

    public void cliclRdbPalete5() {
        rdbPalete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rdbPalete5.getText().toString();
                final int n5 = conVertNote.getNoteByName(text);
                if (n5 == -1) {

                } else {
                    playNote(n5);
                    numberPicker.setValue(n5);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @TargetApi(28)
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                            SlectNumber5 = arrayPicker[newVal];
                            rdbPalete5.setText(SlectNumber5);

                            playNote(newVal);
                            setTextPalete5MainAndSetting(SlectNumber5);
                            Log.e("sl5", SlectNumber5);


                        }

                    });
                }


            }
        });

    }

    public void cliclRdbPalete6() {
        rdbPalete6.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String text = rdbPalete6.getText().toString();
                final int n6 = conVertNote.getNoteByName(text);
                if (n6 == -1) {


                } else {
                    playNote(n6);
                    numberPicker.setValue(n6);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                        @TargetApi(28)
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            SlectNumber6 = arrayPicker[newVal];
                            rdbPalete6.setText(SlectNumber6);

                            playNote(newVal);

                            setTextPalete6MainAndSetting(SlectNumber6);
                            Log.e("sl6", SlectNumber6);


                        }

                    });
                }


            }
        });
    }

//    @SuppressLint("NewApi")
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    void floatColor(int newVal, String keyred, String keygreen, String keyblue) {
//        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
//        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(newVal) + 2.0f / 12.0f), 0.45f, 1.0f);
//        int red = (int) (color.x * 255);
//        int blue = (int) (color.z * 255);
//        int green = (int) (color.y * 255);
//        setTextColor(red, green, blue);
//
//    }

    public void INVISIBLE() {
        txtNotePalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        txtNotePalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        segmentedGroupInterval.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        segmentedGrouppalete.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        txtNotePalettePreset.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        numberPicker.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        segmentPreset.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));


    }

}
