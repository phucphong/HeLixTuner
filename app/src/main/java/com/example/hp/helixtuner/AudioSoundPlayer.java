package com.example.hp.helixtuner;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.SparseArray;

import java.io.InputStream;

/**
 * Created by ssaurel on 15/03/2018.
 */
public class AudioSoundPlayer {

    private SparseArray<PlayThread> threadMap = null;
    private Context context;
    private static final SparseArray<String> SOUND_MAP = new SparseArray<>();
    public static final int MAX_VOLUME = 100, CURRENT_VOLUME = 90;

    static {
        // white keys sounds
        SOUND_MAP.put(0, "21_Guitar");
        SOUND_MAP.put(1, "22_Guitar");
        SOUND_MAP.put(2, "23_Guitar");
        SOUND_MAP.put(3, "24_Guitar");
        SOUND_MAP.put(4, "25_Guitar");
        SOUND_MAP.put(5, "26_Guitar");
        SOUND_MAP.put(6, "27_Guitar");
        SOUND_MAP.put(7, "28_Guitar");
        SOUND_MAP.put(8, "29_Guitar");
        SOUND_MAP.put(9, "30_Guitar");
        SOUND_MAP.put(10, "31_Guitar");
        SOUND_MAP.put(11, "32_Guitar");
        SOUND_MAP.put(12, "33_Guitar");
        SOUND_MAP.put(13, "34_Guitar");
        SOUND_MAP.put(14, "35_Guitar");
        SOUND_MAP.put(15, "36_Guitar");
        SOUND_MAP.put(16, "37_Guitar");
        SOUND_MAP.put(17, "38_Guitar");
        SOUND_MAP.put(18, "39_Guitar");
        SOUND_MAP.put(19, "40_Guitar");
        SOUND_MAP.put(20, "41_Guitar");
        SOUND_MAP.put(21, "42_Guitar");
        SOUND_MAP.put(22, "43_Guitar");
        SOUND_MAP.put(23, "44_Guitar");
        SOUND_MAP.put(24, "45_Guitar");
        SOUND_MAP.put(25, "46_Guitar");
        SOUND_MAP.put(26, "47_Guitar");
        SOUND_MAP.put(27, "48_Guitar");
        SOUND_MAP.put(28, "49_Guitar");
        SOUND_MAP.put(29, "50_Guitar");
        SOUND_MAP.put(30, "51_Guitar");
        SOUND_MAP.put(31, "52_Guitar");
//
        SOUND_MAP.put(32, "53_Guitar");
        SOUND_MAP.put(33, "54_Guitar");
        SOUND_MAP.put(34, "55_Guitar");
        SOUND_MAP.put(35, "56_Guitar");
        SOUND_MAP.put(36, "57_Guitar");
        SOUND_MAP.put(37, "58_Guitar");
        SOUND_MAP.put(38, "59_Guitar");
        SOUND_MAP.put(39, "60_Guitar");
        SOUND_MAP.put(40, "61_Guitar");
        SOUND_MAP.put(41, "62_Guitar");
        SOUND_MAP.put(42, "63_Guitar");
        SOUND_MAP.put(43, "64_Guitar");
        SOUND_MAP.put(44, "65_Guitar");
        SOUND_MAP.put(45, "66_Guitar");
        SOUND_MAP.put(46, "67_Guitar");
        SOUND_MAP.put(47, "68_Guitar");
        SOUND_MAP.put(48, "69_Guitar");
        SOUND_MAP.put(49, "70_Guitar");
        SOUND_MAP.put(50, "71_Guitar");
        SOUND_MAP.put(51, "72_Guitar");
        SOUND_MAP.put(52, "73_Guitar");
        SOUND_MAP.put(53, "74_Guitar");
        SOUND_MAP.put(54, "75_Guitar");
        SOUND_MAP.put(55, "76_Guitar");
        SOUND_MAP.put(56, "77_Guitar");
        SOUND_MAP.put(57, "78_Guitar");
        SOUND_MAP.put(58, "79_Guitar");
        SOUND_MAP.put(59, "80_Guitar");
        SOUND_MAP.put(60, "81_Guitar");
        SOUND_MAP.put(61, "82_Guitar");
        SOUND_MAP.put(62, "83_Guitar");
        SOUND_MAP.put(63, "84_Guitar");

        SOUND_MAP.put(64, "85_Guitar");
        SOUND_MAP.put(65, "86_Guitar");
        SOUND_MAP.put(66, "87_Guitar");
        SOUND_MAP.put(67, "88_Guitar");
        SOUND_MAP.put(68, "89_Guitar");
        SOUND_MAP.put(69, "90_Guitar");
        SOUND_MAP.put(70, "91_Guitar");
        SOUND_MAP.put(71, "92_Guitar");
        SOUND_MAP.put(72, "93_Guitar");
        SOUND_MAP.put(73, "94_Guitar");
        SOUND_MAP.put(74, "95_Guitar");
        SOUND_MAP.put(75, "96_Guitar");
        SOUND_MAP.put(76, "97_Guitar");
        SOUND_MAP.put(77, "98_Guitar");
        SOUND_MAP.put(78, "99_Guitar");
        SOUND_MAP.put(79, "100_Guitar");
        SOUND_MAP.put(80, "101_Guitar");
        SOUND_MAP.put(81, "102_Guitar");
        SOUND_MAP.put(82, "103_Guitar");
        SOUND_MAP.put(83, "104_Guitar");
        SOUND_MAP.put(84, "105_Guitar");
        SOUND_MAP.put(85, "106_Guitar");
        SOUND_MAP.put(86, "107_Guitar");
        SOUND_MAP.put(87, "108_Guitar");

    }

    public AudioSoundPlayer(Context context) {
        this.context = context;
        threadMap = new SparseArray<>();
    }

    public void playNote(int note) {
        if (!isNotePlaying(note)) {
            PlayThread thread = new PlayThread(note);
            thread.start();
            threadMap.put(note, thread);
        }
    }

    public void stopNote(int note) {
        PlayThread thread = threadMap.get(note);

        if (thread != null) {
            threadMap.remove(note);
        }
    }

    public boolean isNotePlaying(int note) {
        return threadMap.get(note) != null;
    }

    private class PlayThread extends Thread {
        int note;
        AudioTrack audioTrack;

        public PlayThread(int note) {
            this.note = note;
        }

        @Override
        public void run() {
            try {
                String path = SOUND_MAP.get(note) + ".wav";
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor ad = assetManager.openFd(path);
                long fileSize = ad.getLength();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];

                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

                float logVolume = (float) (1 - (Math.log(MAX_VOLUME - CURRENT_VOLUME) / Math.log(MAX_VOLUME)));
                audioTrack.setStereoVolume(logVolume, logVolume);

                audioTrack.play();
                InputStream audioStream = null;
                int headerOffset = 0x2C; long bytesWritten = 0; int bytesRead = 0;

                audioStream = assetManager.open(path);
                audioStream.read(buffer, 0, headerOffset);

                while (bytesWritten < fileSize - headerOffset) {
                    bytesRead = audioStream.read(buffer, 0, bufferSize);
                    bytesWritten += audioTrack.write(buffer, 0, bytesRead);
                }

                audioTrack.stop();
                audioTrack.release();

            } catch (Exception e) {
            } finally {
                if (audioTrack != null) {
                    audioTrack.release();
                }
            }
        }
    }

}