package com.example.hp.helixtuner;

import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidatePublic {
    public static final int SAMPLE_RATE = 44100; // The sampling rate
    public static final String LOG_TAG = "test";


    public static int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT);





    public static short[] audioBuffer = new short[bufferSize / 2];
    public static float[] circleBuffers = new float[bufferSize / 2];
public  static String BMpreset,BMNote1, BMNote2, BMNote3, BMNote4, BMNote5, BMNote6;

    public static int chormatic = 12 ;

    public static ArrayList<HashMap<String, Object>> listPreset;
    public static ArrayList<Guitar> arrGuitar = new ArrayList<>();

    public static String SlectNumber1, SlectNumber2, SlectNumber3, SlectNumber4, SlectNumber5, SlectNumber6;
    public static String BMUS_Key_Preset = "preset";
    public static String BMUS_Key_note1 = "note1";
    public static String BMUS_Key_note2 = "note2";
    public static String BMUS_Key_note3 = "note3";
    public static String BMUS_Key_note4 = "note4";
    public static String BMUS_Key_note5 = "note5";
    public static String BMUS_Key_note6 = "note6";

    public static int red, green, blue;
    public static int redTitle, greenTitle, blueTitle;



}
