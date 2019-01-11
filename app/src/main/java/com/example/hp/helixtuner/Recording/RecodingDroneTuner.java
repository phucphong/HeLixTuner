package com.example.hp.helixtuner.Recording;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import static com.example.hp.helixtuner.ValidatePublic.*;

public class RecodingDroneTuner {
    boolean mShouldContinue; // Indicates if recording / playback should stop

    public void recordAudio() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mShouldContinue = true;
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
                // buffer size in bytes
//                int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
//                        AudioFormat.CHANNEL_IN_MONO,
//                        AudioFormat.ENCODING_PCM_16BIT);
                if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                    bufferSize = SAMPLE_RATE * 2;
                }
                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

                if (record.getState() != AudioRecord.STATE_INITIALIZED) {
                    Log.e("", "Audio Record can't initialize!");
                    return;
                }
                record.startRecording();

                Log.v(LOG_TAG, "Start recording");

                long shortsRead = 0;
                while (mShouldContinue) {
                    int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);

                    for (int i = 0; i < audioBuffer.length; i++) {
                        //ep kieu short - float
                        circleBuffers[i] = audioBuffer[i];
                        // lay gia tri min max doi sang -1,1
                        circleBuffers[i] /= Short.MIN_VALUE;


                    }
                    float firstSlot = circleBuffers[0];
                    float secondSlot = circleBuffers[1];
                    shortsRead += numberOfShort;

                    //   Log.e("int so thu 1", String.valueOf(firstSlot));
                    // Log.e("int so thu 2", String.valueOf(secondSlot));

                }


                record.stop();
                record.release();


                Log.v(LOG_TAG, String.format("Recording stopped. Samples read: %d", shortsRead));
            }
        }).start();


    }


}
