package pcg.hand2hand_smartwatch;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class MicrophoneThread extends Thread {
    final static int SAMPLE_RATE_IN_HZ = 44100;
    final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    MainActivity father;
    int counter;

    AudioRecord audioRecord;
    byte[] buffer;
    int bufferSize;
    boolean isRun;
    //AudioManager audioManager;

    public MicrophoneThread(MainActivity father) {
        super();
        this.father = father;
        counter = 0;

        // audio record
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        audioRecord = new AudioRecord(MediaRecorder.getAudioSourceMax(), SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
        buffer = new byte[bufferSize];
        isRun = false;

        // audio manager
        //audioManager = (AudioManager) father.getSystemService(Context.AUDIO_SERVICE);
        //audioManager.setMode(AudioManager.STREAM_MUSIC);
        //audioManager.setMicrophoneMute(false);
        //audioManager.setSpeakerphoneOn(false);
    }

    public void run() {
        try {
            audioRecord.startRecording();
            isRun = true;
            while (isRun) {
                counter++;
                int readSize = audioRecord.read(buffer, 0, bufferSize);
                Log.d("xxx", readSize + "");
                if (readSize != bufferSize) {
                    Log.d("microphone", "buff: " + bufferSize + ", read: " + readSize);
                } else {
                    father.logToFile("time");
                    father.logToFile("microphone", buffer);
                }
            }
            audioRecord.stop();
        } catch (Exception e) {
            Log.d("microphone", e.toString());
        } finally {
            audioRecord.release();
            audioRecord = null;
        }
    }

    public void pause() {
        isRun = !isRun;
    }
}
