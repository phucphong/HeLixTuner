package com.example.hp.helixtuner;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;


import com.example.hp.helixtuner.OpenGl.MyGLSurfaceView;
import com.example.hp.helixtuner.Recording.RecodingDroneTuner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;


import android.Manifest;


import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Build;
import android.renderscript.Float3;

import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.view.View;
import android.widget.Toast;

import static com.example.hp.helixtuner.BHSVtoRGB.NoteToHue;
import static com.example.hp.helixtuner.ValidatePublic.*;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    ConVertNote conVertNote;
    RadioButton rdbMainPalette1, rdbMainPalette2, rdbMainPalette3, rdbMainPalette4, rdbMainPalette5, rdbMainPalette6;
    Button BtnPre, BtnPreX2, BtnNext, BtnNexX2;
    SegmentedGroup segmentedMainGroup;
    private boolean loaded = false;
    PrefManager prefManager;
    RadioButton rdbAutomatic, rdbPalette, rdbChromatic;
    // RecyclerView rcvIntrusment;
    ImageView imgBottom, imgtop;
    static AudioManager amg;
    int note = 0;
    public static final int MAX_VOLUME = 100, CURRENT_VOLUME = 90;

    float sampleRate;

    private GLSurfaceView mGLView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLView = new MyGLSurfaceView(this);
       // final ConstraintLayout linerLayout = findViewById(R.id.ctranslayout);
       // linerLayout.addView(mGLView, 0);
        init();
        requestRecordAudioPermission();
        RecodingDroneTuner droneTuner = new RecodingDroneTuner();
        droneTuner.recordAudio();

        setTextRbgPalette();
        ClickRadio();
        prefManager = new PrefManager(this);
        conVertNote = new ConVertNote();

        amg = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        imgBottom.setImageResource(arrGuitar.get(position).getImagebottom());
        imgtop.setImageResource(arrGuitar.get(position).getImageTop());
        final AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        String samplerate = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        sampleRate = Float.parseFloat(samplerate);
        Log.e("posttiotn", String.valueOf(position));
        BHSVtoRGB bhsVtoRGB = new BHSVtoRGB();
        Float3 color = bhsVtoRGB.bHSVToRGB((NoteToHue(position) + 2.0f / 12.0f), 0.2f, 1.0f);

        red = (int) (color.x * 255);
        gren = (int) (color.y * 255);
        blue = (int) (color.z * 255);
        rdbAutomatic = findViewById(R.id.rdbautomatic);
        rdbPalette = findViewById(R.id.rdbpalette);
        rdbChromatic = findViewById(R.id.rdbchromatic);

        segmentedMainGroup.setVisibility(View.INVISIBLE);
        segmentedMainGroup.setTintColor(Color.rgb(red, gren, blue));


        imgtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SettingActivity settingActivity = new SettingActivity();
                fragmentTransaction.add(R.id.contentView, settingActivity);
                fragmentTransaction.addToBackStack("settingActivity");
                fragmentTransaction.commit();


            }
        });

        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePosition(note + 1);

                Log.e("note", String.valueOf(note));


            }


        });
        BtnNexX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = note + chormatic;
                Log.e("note", String.valueOf(note));
                changePosition(p);
                Log.e("p", String.valueOf(p));
            }
        });
        BtnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePosition(note - 1);
            }
        });

        BtnPreX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = note - chormatic;
                changePosition(p);

            }
        });

    }


    void playNote(int note) {
        AudioSoundPlayer audioSoundPlayer = new AudioSoundPlayer(getApplicationContext());
        audioSoundPlayer.isNotePlaying(note);
        audioSoundPlayer.playNote(note);
        audioSoundPlayer.stopNote(note);
        imgBottom.setImageResource(arrGuitar.get(note).getImagebottom());
        imgtop.setImageResource(arrGuitar.get(note).getImageTop());


    }

    void changePosition(int p) {

        boolean OutOfRange = p < 0 || p >= arrGuitar.size();
        if (!OutOfRange) {
            note = p;
        }
        playNote(note);
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


    @Override
    protected void onPause() {
        super.onPause();
        //  mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  mGLView.onResume();


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


    public void setTextRbgPalette() {
        rdbMainPalette1.setText(BMNote1);
        rdbMainPalette2.setText(BMNote2);
        rdbMainPalette3.setText(BMNote3);
        rdbMainPalette4.setText(BMNote4);
        rdbMainPalette5.setText(BMNote5);
        rdbMainPalette6.setText(BMNote6);


    }


    public void init() {

        BtnPre = findViewById(R.id.btnpre);
        BtnPreX2 = findViewById(R.id.btnprex2);
        BtnNext = findViewById(R.id.btnnext);
        BtnNexX2 = findViewById(R.id.btnnextx2);
        imgBottom = findViewById(R.id.imgbottom);
        imgtop = findViewById(R.id.imgtop);


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


    public void ClickRadio() {

        rdbMainPalette1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n1 = conVertNote.convertNameToNote(rdbMainPalette1.getText().toString());
                playNote(n1);
                Log.e("n", String.valueOf(n1));
                //  segmentedMainGroup.setTintColor(Color.rgb(red1, gren1, blue1));


            }
        });
        rdbMainPalette2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int n2 = conVertNote.convertNameToNote(rdbMainPalette2.getText().toString());
                playNote(n2);

                Log.e("n", String.valueOf(n2));
            }
        });
        rdbMainPalette3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int n3 = conVertNote.convertNameToNote(rdbMainPalette3.getText().toString());
                playNote(n3);

                Log.e("n", String.valueOf(n3));

            }
        });
        rdbMainPalette4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int n4 = conVertNote.convertNameToNote(rdbMainPalette4.getText().toString());
                playNote(n4);

                Log.e("n", String.valueOf(n4));

            }
        });
        rdbMainPalette5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int n5 = conVertNote.convertNameToNote(rdbMainPalette5.getText().toString());
                playNote(n5);

                Log.e("n", String.valueOf(n5));

            }
        });
        rdbMainPalette6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int n6 = conVertNote.convertNameToNote(rdbMainPalette6.getText().toString());
                playNote(n6);

                Log.e("n", String.valueOf(n6));


            }
        });

    }
}




