package com.example.hp.helixtuner;

import android.annotation.TargetApi;
import android.content.Context;

import android.content.SharedPreferences;

import android.media.AudioManager;

import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.hp.helixtuner.OpenGl.MyGLSurfaceView;
import com.example.hp.helixtuner.Recording.RecodingDroneTuner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.renderscript.Float3;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import info.hoang8f.android.segmented.SegmentedGroup;

import static com.example.hp.helixtuner.BHSVtoRGB.NoteToHue;
import static com.example.hp.helixtuner.ValidatePublic.*;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    SharedPreferences.Editor editor;
    ConVertNote conVertNote;
    RadioButton rdbMainPalette1, rdbMainPalette2, rdbMainPalette3, rdbMainPalette4, rdbMainPalette5, rdbMainPalette6;
    SegmentedGroup segmentedMainGroup, segmentedGroupSelection;
    SharedPreferences sharedPreferences;
    RadioButton rdbAutomatic, rdbPalette, rdbChromatic;
    // RecyclerView rcvIntrusment;
    ImageView imgBottom, imgtop;
    int[] listImageTop = new int[12];
    static AudioManager amg;
    Button btnmainPre, btnmainPreOctive, btnmainnext, btnmainNextOctive;
    TextView txtNotePalettePreset;
    TextView txtvolume;
    RadioButton rdbMainPalete1, rdbMainPalete2, rdbMainPalete3, rdbMainPalete4, rdbMainPalete5, rdbMainPalete6, rdbpreset;


    private GLSurfaceView mGLView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConstraintLayout constraintLayout = findViewById(R.id.ctranslayout);
        mGLView = new MyGLSurfaceView(this);
        constraintLayout.addView(mGLView, 0);
        conVertNote = new ConVertNote();
        sharedPreferences = getSharedPreferences("helix", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        init();
        String a = conVertNote.getImgNameByNote(startnote);
        newnote = conVertNote.getNoteByNameColorTop(a);
        Log.e("newnote", String.valueOf(newnote));


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void floatColor(int note) {
        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(note) + 2.0f / 12.0f), 0.45f, 1.0f);

        red = (int) (color.x * 255);
        green = (int) (color.y * 255);
        blue = (int) (color.z * 255);
    }

    void setimageTopBottom(int keynote) {
        imgBottom.setImageResource(arrGuitar.get(keynote).getImagebottom());
        imgtop.setImageResource(arrGuitar.get(keynote).getImageTop());
    }

    void playBtnNext() {
        btnmainnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startnote = startnote + 1;


                changePosition(startnote);
                setimageTopBottom(startnote);
                // convet note snag FullString
                String a = conVertNote.getImgNameByNote(startnote);
                newnote = conVertNote.getNoteByNameColorTop(a);
                Log.e("newnote", String.valueOf(newnote));
                imgtop.setImageResource(listImageTop[newnote]);

            }


        });
    }

    void playBtnPre() {
        btnmainPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startnote = startnote - 1;
                changePosition(startnote);
                String a = conVertNote.getImgNameByNote(startnote);

                changePosition(startnote);
                newnote = conVertNote.getNoteByNameColorTop(a);
                Log.e("newnotePre", String.valueOf(newnote));
                imgtop.setImageResource(listImageTop[newnote]);

            }
        });
    }

    void playBtnPreOctive() {
        btnmainPreOctive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startnote = startnote - 12;
                changePosition(startnote);
                String a = conVertNote.getImgNameByNote(startnote);
                newnote = conVertNote.getNoteByNameColorTop(a);
                Log.e("chomaticnext", String.valueOf(chormatic));
                imgtop.setImageResource(listImageTop[newnote]);

            }
        });

    }

    void playBtnNectOctive() {
        btnmainNextOctive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startnote = startnote + 12;
                String a = conVertNote.getImgNameByNote(startnote);
                changePosition(startnote);
                Log.e("notenextOctive", String.valueOf(startnote));
                changePosition(startnote);
                newnote = conVertNote.getNoteByNameColorTop(a);
                Log.e("chomaticPre", String.valueOf(chormatic));
                imgtop.setImageResource(listImageTop[newnote]);


            }
        });
    }


    void clickimagetop() {

        imgtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SettingActivity settingActivity = new SettingActivity();
                fragmentTransaction.add(R.id.contentView, settingActivity, "settingActivity");

                fragmentTransaction.addToBackStack("settingActivity");

                fragmentTransaction.commit();


            }
        });
    }

    void playNote(int note) {
        AudioSoundPlayer audioSoundPlayer = new AudioSoundPlayer(getApplicationContext());
        audioSoundPlayer.isNotePlaying(note);
        audioSoundPlayer.playNote(note);
        audioSoundPlayer.stopNote(note);


    }

    void setimage(int note) {
        imgBottom.setImageResource(arrGuitar.get(note).getImagebottom());

    }

    void changePosition(int p) {

        boolean OutOfRange = p < 0 || p >= arrGuitar.size();
        if (!OutOfRange) {
            startnote = p;
        }
        playNote(startnote);
        setimage(startnote);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void setTintcolorPalette(float note) {
        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(note) + 2.0f / 12.0f), 0.45f, 1.0f);

        red = (int) (color.x * 255);
        green = (int) (color.y * 255);
        blue = (int) (color.z * 255);
        segmentedMainGroup.setTintColor(Color.rgb(red, green, blue));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void floatTitleColorPalette(float note) {
        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(note) + 2.0f / 12.0f), 0.2f, 1.0f);
        redTitle = (int) (color.x * 255);
        greenTitle = (int) (color.y * 255);
        blueTitle = (int) (color.z * 255);
    }

    void setColorTop(int a) {
        imgtop.setImageResource(listImageTop[a]);

    }


    public void ClickPaleteMain() {

        rdbMainPalette1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = rdbMainPalette1.getText().toString();
                final int n1 = conVertNote.getNoteByName(text);
                notePalete = conVertNote.getNoteByNameColorTop(text);

                if (n1 == -1) {


                } else {
                    playNote(n1 + 12);
                    setColorTop(notePalete);
                    setTintcolorPalette(notePalete);
                    floatTitleColorPalette(notePalete);
                }
            }
        });
        rdbMainPalette2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = rdbMainPalette2.getText().toString();
                final int n2 = conVertNote.getNoteByName(text);
                notePalete = conVertNote.getNoteByNameColorTop(text);

                if (n2 == -1) {
                } else {
                    playNote(n2 + 12);
                    setColorTop(notePalete);
                    setTintcolorPalette(notePalete);
                    floatTitleColorPalette(notePalete);

                }
            }
        });
        rdbMainPalette3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = rdbMainPalette3.getText().toString();
                final int n3 = conVertNote.getNoteByName(text);
                notePalete = conVertNote.getNoteByNameColorTop(text);

                if (n3 == -1) {
                } else {
                    playNote(n3 + 12);
                    setColorTop(notePalete);
                    setTintcolorPalette(notePalete);
                    floatTitleColorPalette(notePalete);
                }
            }
        });
        rdbMainPalette4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = rdbMainPalette4.getText().toString();
                final int n4 = conVertNote.getNoteByName(text);
                notePalete = conVertNote.getNoteByNameColorTop(text);

                if (n4 == -1) {
                } else {
                    playNote(n4 + 12);
                    setColorTop(notePalete);
                    setTintcolorPalette(notePalete);
                    floatTitleColorPalette(notePalete);
                }
            }
        });
        rdbMainPalette5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String text = rdbMainPalette5.getText().toString();
                final int n5 = conVertNote.getNoteByName(text);
                notePalete = conVertNote.getNoteByNameColorTop(text);

                if (n5 == -1) {
                } else {
                    playNote(n5 + 12);
                    setColorTop(notePalete);
                    setTintcolorPalette(notePalete);
                    floatTitleColorPalette(notePalete);
                }


            }
        });
        rdbMainPalette6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String text = rdbMainPalette6.getText().toString();
                final int n6 = conVertNote.getNoteByName(text);
                notePalete = conVertNote.getNoteByNameColorTop(text);

                if (n6 == -1) {
                } else {
                    playNote(n6 + 12);
                    setColorTop(notePalete);
                    setTintcolorPalette(notePalete);
                    floatTitleColorPalette(notePalete);

                }


            }
        });

    }

    private long nativeContextPtr = 0;


    private void requestRecordAudioPermission() {
        //check API version, do nothing if API version < 23!
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Activity", "Granted!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Activity", "Denied!");
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onStart() {
        super.onStart();
        setimageTopBottom(startnote);
        conVertNote = new ConVertNote();

        clickimagetop();

        floatTitleColorPalette(newnote);
        setTintcolorPalette(newnote);
        playBtnNext();
        playBtnNectOctive();
        playBtnPre();
        playBtnPreOctive();
        ClickPaleteMain();
        requestRecordAudioPermission();
        RecodingDroneTuner droneTuner = new RecodingDroneTuner();
        droneTuner.recordAudio();
        segmentedMainGroup.setVisibility(sharedPreferences.getInt("invisible", View.INVISIBLE));
        // segmentedMainGroup.setTintColor(Color.rgb(red, green, blue));


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mGLView.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();

        mGLView.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);  //this line is api 19+
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }
    }

    public void init() {
        listImageTop[0] = R.mipmap.top_c;
        listImageTop[1] = R.mipmap.top_c_;
        listImageTop[2] = R.mipmap.top_d;
        listImageTop[3] = R.mipmap.top_d_;
        listImageTop[4] = R.mipmap.top_e;
        listImageTop[5] = R.mipmap.top_f;
        listImageTop[6] = R.mipmap.top_f_;
        listImageTop[7] = R.mipmap.top_g;
        listImageTop[8] = R.mipmap.top_g_;
        listImageTop[9] = R.mipmap.top_a;
        listImageTop[10] = R.mipmap.top_a_;
        listImageTop[11] = R.mipmap.top_b;
        rdbAutomatic = findViewById(R.id.rdbautomatic);
        rdbPalette = findViewById(R.id.rdbpalette);
        rdbChromatic = findViewById(R.id.rdbchromatic);

        btnmainnext = findViewById(R.id.btnnext);
        btnmainNextOctive = findViewById(R.id.btnnextx2);
        btnmainPre = findViewById(R.id.btnpre);
        btnmainPreOctive = findViewById(R.id.btnprex2);

        imgBottom = findViewById(R.id.imgbottom);
        imgtop = findViewById(R.id.imgtop);
        rdbpreset = findViewById(R.id.rdbPreset);
        txtNotePalettePreset = findViewById(R.id.txtNotePalettePreset);
        segmentedMainGroup = findViewById(R.id.segmentedMainPalete);

        rdbMainPalette1 = findViewById(R.id.rdbMainPalete1);
        rdbMainPalette2 = findViewById(R.id.rdbMainPalete2);
        rdbMainPalette3 = findViewById(R.id.rdbMainPalete3);
        rdbMainPalette4 = findViewById(R.id.rdbMainPalete4);
        rdbMainPalette5 = findViewById(R.id.rdbMainPalete5);
        rdbMainPalette6 = findViewById(R.id.rdbMainPalete6);

        rdbMainPalette1.setText(SlectNumber1);
        rdbMainPalette2.setText(SlectNumber2);
        rdbMainPalette3.setText(SlectNumber3);
        rdbMainPalette4.setText(SlectNumber4);
        rdbMainPalette5.setText(SlectNumber5);
        rdbMainPalette6.setText(SlectNumber6);
        segmentedMainGroup.setVisibility(View.VISIBLE);


        arrGuitar.add(new Guitar("a1_2x", R.mipmap.a1_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_2_2x", R.mipmap.a_1_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b1_2x", R.mipmap.b1_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c2_2x", R.mipmap.c2_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_2_2x", R.mipmap.c_2_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d2_2x", R.mipmap.d2_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_2_2x", R.mipmap.d_2_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e2_2x", R.mipmap.e2_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f2_2x", R.mipmap.f2_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_2_2x", R.mipmap.f_2_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("f2_2x", R.mipmap.g2_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("f_2_2x", R.mipmap.g_2_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("a2_2x", R.mipmap.a2_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_2_2x", R.mipmap.a_2_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b2_2x", R.mipmap.b2_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c3_2x", R.mipmap.c3_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_3_2x", R.mipmap.c_3_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d3_2x", R.mipmap.d3_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_3_2x", R.mipmap.d_3_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e3_2x", R.mipmap.e3_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f3_2x", R.mipmap.f3_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_3_2x", R.mipmap.f_3_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("f3_2x", R.mipmap.g3_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("f_3_2x", R.mipmap.g_3_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("a3_2x", R.mipmap.a3_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_3_2x", R.mipmap.a_3_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b3_2x", R.mipmap.b3_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c4_2x", R.mipmap.c4_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_4_2x", R.mipmap.c_4_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d4_2x", R.mipmap.d4_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_4_2x", R.mipmap.d_4_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e4_2x", R.mipmap.e4_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f4_2x", R.mipmap.f4_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_4_2x", R.mipmap.f_4_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("f4_2x", R.mipmap.g4_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("f_4_2x", R.mipmap.g_4_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("a4_2x", R.mipmap.a4_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_4_2x", R.mipmap.a_4_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b4_2x", R.mipmap.b4_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c5_2x", R.mipmap.c5_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_5_2x", R.mipmap.c_5_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d5_2x", R.mipmap.d5_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_5_2x", R.mipmap.d_5_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e5_2x", R.mipmap.e5_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f5_2x", R.mipmap.f5_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_5_2x", R.mipmap.f_5_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("f5_2x", R.mipmap.g5_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("f_5_2x", R.mipmap.g_5_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("a5_2x", R.mipmap.a5_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_5_2x", R.mipmap.a_5_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b5_2x", R.mipmap.b5_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c6_2x", R.mipmap.c6_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_6_2x", R.mipmap.c_6_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d6_2x", R.mipmap.d6_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_6_2x", R.mipmap.d_6_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e6_2x", R.mipmap.e6_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f6_2x", R.mipmap.f6_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_6_2x", R.mipmap.f_6_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("g6_2x", R.mipmap.g6_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("g_6_2x", R.mipmap.g_6_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("a6_2x", R.mipmap.a6_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_6_2x", R.mipmap.a_6_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b6_2x", R.mipmap.b6_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c7_2x", R.mipmap.c7_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_7_2x", R.mipmap.c_7_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d7_2x", R.mipmap.d7_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_7_2x", R.mipmap.d_7_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e7_2x", R.mipmap.e7_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f7_2x", R.mipmap.f7_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_7_2x", R.mipmap.f_7_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("g7_2x", R.mipmap.g7_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("g7_2x", R.mipmap.g_7_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("a7_2x", R.mipmap.a7_2x, R.mipmap.top_a));
        arrGuitar.add(new Guitar("a_7_2x", R.mipmap.a_7_2x, R.mipmap.top_a_));
        arrGuitar.add(new Guitar("b7_2x", R.mipmap.b7_2x, R.mipmap.top_b));
        arrGuitar.add(new Guitar("c8_2x", R.mipmap.c8_2x, R.mipmap.top_c));
        arrGuitar.add(new Guitar("c_8_2x", R.mipmap.c_8_2x, R.mipmap.top_c_));
        arrGuitar.add(new Guitar("d8_2x", R.mipmap.d8_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_8_2x", R.mipmap.d_8_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e8_2x", R.mipmap.e8_2x, R.mipmap.top_e));
        arrGuitar.add(new Guitar("f8_2x", R.mipmap.f8_2x, R.mipmap.top_f));
        arrGuitar.add(new Guitar("f_8_2x", R.mipmap.f_8_2x, R.mipmap.top_f_));
        arrGuitar.add(new Guitar("c8_2x", R.mipmap.c8_2x, R.mipmap.top_g));
        arrGuitar.add(new Guitar("c_8_2x", R.mipmap.c_8_2x, R.mipmap.top_g_));
        arrGuitar.add(new Guitar("d8_2x", R.mipmap.d8_2x, R.mipmap.top_d));
        arrGuitar.add(new Guitar("d_8_2x", R.mipmap.d_8_2x, R.mipmap.top_d_));
        arrGuitar.add(new Guitar("e8_2x", R.mipmap.e8_2x, R.mipmap.top_e));

    }
}




