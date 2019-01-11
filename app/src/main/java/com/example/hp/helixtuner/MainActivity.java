package com.example.hp.helixtuner;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;


import com.example.hp.helixtuner.OpenGl.MyGLSurfaceView;
import com.example.hp.helixtuner.Recording.RecodingDroneTuner;

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

import static com.example.hp.helixtuner.BHSVtoRGB.NoteToHue;
import static com.example.hp.helixtuner.ValidatePublic.*;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    private List<String> files = new ArrayList<>();
    RadioButton rdbMainPalette1, rdbMainPalette2, rdbMainPalette3, rdbMainPalette4, rdbMainPalette5, rdbMainPalette6;
    Button BtnPre, BtnPreX2, BtnNext, BtnNexX2;
    SegmentedGroup segmentedMainGroup;
    private boolean loaded = false;
    PrefManager prefManager;
    RadioButton rdbAutomatic, rdbPalette, rdbChromatic;
    // RecyclerView rcvIntrusment;
    ImageView imgBottom, imgtop;
    static SoundPool soundPool;
    static int[] sm;
    static AudioManager amg;
    int note = 24;


    String KeymBundle = "KeymBundle";


    String keyNote = "intentNote";
    String keyRed = "red";
    String keyBlu = "blu";
    String keyGreen = "Green";
    String keyIntArrayAudio = "audio";

    float sampleRate;

    private GLSurfaceView mGLView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGLView = new MyGLSurfaceView(this);

        final ConstraintLayout linerLayout = findViewById(R.id.ctranslayout);
        linerLayout.addView(mGLView, 0);
        requestRecordAudioPermission();
        RecodingDroneTuner droneTuner = new RecodingDroneTuner();
        droneTuner.recordAudio();
        init();
        setTextRbgPalette();
        ClickRadio();
        prefManager = new PrefManager(this);


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
                changePosition(position + 1);

            }


        });
        BtnNexX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePosition(position + chormatic);
            }
        });
        BtnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePosition(position - 1);
            }
        });

        BtnPreX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePosition(position - chormatic);
            }
        });

    }


    double convertNoteToFrequency(float note, float octave) {
        return 440 * Math.pow(2, (note - 69 - octave * 12) / 12.);
    }


    void changeNote() {


         soundPool.play(sm[position], 1, 1, 1, 0, 1f);
            imgBottom.setImageResource(arrGuitar.get(position).getImagebottom());
            imgtop.setImageResource(arrGuitar.get(position).getImageTop());


    }

    void changePosition(int p) {
        stopNote();
        boolean OutOfRange = p < 0 || p >= sm.length;
        if (!OutOfRange) {
            position = p;
        }
        changeNote();
    }

    void stopNote()
    {
       soundPool.stop(sm[position]);

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
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();


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

    public native String stringFromJNI();

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


        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        sm = new int[88];




        sm[0] = soundPool.load(getApplication(), R.raw.phong, 1);
        sm[1] = soundPool.load(getApplication(), R.raw.phong1, 1);
        sm[2] = soundPool.load(getApplication(), R.raw.phong2, 1);
        sm[3] = soundPool.load(getApplication(), R.raw.phong3, 1);
        sm[4] = soundPool.load(getApplication(), R.raw.phong4, 1);
        sm[5] = soundPool.load(getApplication(), R.raw.phong5, 1);
        sm[6] = soundPool.load(getApplication(), R.raw.phong6, 1);
        sm[7] = soundPool.load(getApplication(), R.raw.phong7, 1);
        sm[8] = soundPool.load(getApplication(), R.raw.phong8, 1);
        sm[9] = soundPool.load(getApplication(), R.raw.phong9, 1);
        sm[10] = soundPool.load(getApplication(), R.raw.phong10, 1);
        sm[11] = soundPool.load(getApplication(), R.raw.phong11, 1);
        sm[12] = soundPool.load(getApplication(), R.raw.phong12, 1);
        sm[13] = soundPool.load(getApplication(), R.raw.phong13, 1);
        sm[14] = soundPool.load(getApplication(), R.raw.phong14, 1);
        sm[15] = soundPool.load(getApplication(), R.raw.phong15, 1);
        sm[16] = soundPool.load(getApplication(), R.raw.phong16, 1);
        sm[17] = soundPool.load(getApplication(), R.raw.phong18, 1);
        sm[18] = soundPool.load(getApplication(), R.raw.phong1, 1);
        sm[19] = soundPool.load(getApplication(), R.raw.phong2, 1);
        sm[20] = soundPool.load(getApplication(), R.raw.phong3, 1);
        sm[21] = soundPool.load(getApplication(), R.raw.phong4, 1);
        sm[22] = soundPool.load(getApplication(), R.raw.phong5, 1);
        sm[23] = soundPool.load(getApplication(), R.raw.phong5, 1);
        sm[24] = soundPool.load(getApplication(), R.raw.phong7, 1);
        sm[25] = soundPool.load(getApplication(), R.raw.phong8, 1);
        sm[26] = soundPool.load(getApplication(), R.raw.phong9, 1);
        sm[27] = soundPool.load(getApplication(), R.raw.phong10, 1);
        sm[28] = soundPool.load(getApplication(), R.raw.phong11, 1);
        sm[29] = soundPool.load(getApplication(), R.raw.phong12, 1);
        sm[30] = soundPool.load(getApplication(), R.raw.phong13, 1);
        sm[31] = soundPool.load(getApplication(), R.raw.phong14, 1);
        sm[32] = soundPool.load(getApplication(), R.raw.phong15, 1);
        sm[33] = soundPool.load(getApplication(), R.raw.phong16, 1);
        sm[34] = soundPool.load(getApplication(), R.raw.phong18, 1);
        sm[35] = soundPool.load(getApplication(), R.raw.phong, 1);
        sm[36] = soundPool.load(getApplication(), R.raw.phong1, 1);
        sm[37] = soundPool.load(getApplication(), R.raw.phong2, 1);
        sm[38] = soundPool.load(getApplication(), R.raw.phong3, 1);
        sm[39] = soundPool.load(getApplication(), R.raw.phong4, 1);
        sm[40] = soundPool.load(getApplication(), R.raw.phong5, 1);
        sm[41] = soundPool.load(getApplication(), R.raw.phong6, 1);
        sm[42] = soundPool.load(getApplication(), R.raw.phong7, 1);
        sm[43] = soundPool.load(getApplication(), R.raw.phong18, 1);
        sm[44] = soundPool.load(getApplication(), R.raw.phong16, 1);
        sm[45] = soundPool.load(getApplication(), R.raw.phong15, 1);
        sm[46] = soundPool.load(getApplication(), R.raw.phong14, 1);
        sm[47] = soundPool.load(getApplication(), R.raw.phong13, 1);
        sm[48] = soundPool.load(getApplication(), R.raw.phong12, 1);
        sm[49] = soundPool.load(getApplication(), R.raw.phong11, 1);
        sm[50] = soundPool.load(getApplication(), R.raw.phong10, 1);
        sm[51] = soundPool.load(getApplication(), R.raw.phong9, 1);
        sm[52] = soundPool.load(getApplication(), R.raw.phong8, 1);
        sm[53] = soundPool.load(getApplication(), R.raw.phong7, 1);
        sm[54] = soundPool.load(getApplication(), R.raw.phong6, 1);
        sm[55] = soundPool.load(getApplication(), R.raw.phong5, 1);
        sm[56] = soundPool.load(getApplication(), R.raw.phong4, 1);
        sm[57] = soundPool.load(getApplication(), R.raw.phong3, 1);
        sm[58] = soundPool.load(getApplication(), R.raw.phong2, 1);
        sm[59] = soundPool.load(getApplication(), R.raw.phong1, 1);
        sm[60] = soundPool.load(getApplication(), R.raw.phong, 1);
        sm[61] = soundPool.load(getApplication(), R.raw.phong18, 1);
        sm[62] = soundPool.load(getApplication(), R.raw.phong16, 1);
        sm[63] = soundPool.load(getApplication(), R.raw.phong15, 1);
        sm[64] = soundPool.load(getApplication(), R.raw.phong14, 1);
        sm[65] = soundPool.load(getApplication(), R.raw.phong13, 1);
        sm[66] = soundPool.load(getApplication(), R.raw.phong12, 1);
        sm[67] = soundPool.load(getApplication(), R.raw.phong11, 1);
        sm[68] = soundPool.load(getApplication(), R.raw.phong10, 1);
        sm[69] = soundPool.load(getApplication(), R.raw.phong9, 1);
        sm[70] = soundPool.load(getApplication(), R.raw.phong8, 1);
        sm[71] = soundPool.load(getApplication(), R.raw.phong7, 1);
        sm[72] = soundPool.load(getApplication(), R.raw.phong6, 1);
        sm[73] = soundPool.load(getApplication(), R.raw.phong5, 1);
        sm[74] = soundPool.load(getApplication(), R.raw.phong4, 1);
        sm[75] = soundPool.load(getApplication(), R.raw.phong3, 1);
        sm[76] = soundPool.load(getApplication(), R.raw.phong2, 1);
        sm[77] = soundPool.load(getApplication(), R.raw.phong1, 1);
        sm[78] = soundPool.load(getApplication(), R.raw.phong, 1);
        sm[79] = soundPool.load(getApplication(), R.raw.phon17, 1);
        sm[80] = soundPool.load(getApplication(), R.raw.phong10, 1);
        sm[81] = soundPool.load(getApplication(), R.raw.phong11, 1);
        sm[82] = soundPool.load(getApplication(), R.raw.phong15, 1);
        sm[83] = soundPool.load(getApplication(), R.raw.phong13, 1);
        sm[84] = soundPool.load(getApplication(), R.raw.phong16, 1);
        sm[85] = soundPool.load(getApplication(), R.raw.phong15, 1);
        sm[86] = soundPool.load(getApplication(), R.raw.phong, 1);
        sm[87] = soundPool.load(getApplication(), R.raw.phong2, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });


    }


    public void ClickRadio() {

        rdbMainPalette1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sm[post1], 1, 1, 1, 0, 1f);
                segmentedMainGroup.setTintColor(Color.rgb(red1, gren1, blue1));


            }
        });
        rdbMainPalette2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sm[post2], 1, 1, 1, 0, 1f);
                // rdbMainPalette2.setText(SlectNumber2);
                segmentedMainGroup.setTintColor(Color.rgb(red2, gren2, blue2));

            }
        });
        rdbMainPalette3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sm[post3], 1, 1, 1, 0, 1f);
                //rdbMainPalette3.setText(SlectNumber3);
                segmentedMainGroup.setTintColor(Color.rgb(red3, gren3, blue3));

            }
        });
        rdbMainPalette4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sm[post4], 1, 1, 1, 0, 1f);
                //rdbMainPalette4.setText(SlectNumber4);
                segmentedMainGroup.setTintColor(Color.rgb(red4, gren4, blue4));

            }
        });
        rdbMainPalette5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sm[post5], 1, 1, 1, 0, 1f);
                // rdbMainPalette5.setText(SlectNumber5);
                segmentedMainGroup.setTintColor(Color.rgb(red5, gren5, blue5));

            }
        });
        rdbMainPalette6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sm[post6], 1, 1, 1, 0, 1f);
                // rdbMainPalette6.setText(SlectNumber6);
                segmentedMainGroup.setTintColor(Color.rgb(red6, gren6, blue6));


            }
        });

    }
}




