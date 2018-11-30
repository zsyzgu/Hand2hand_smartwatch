package pcg.hand2hand_smartwatch;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class MainActivity extends WearableActivity {
    // ui
    TextView uText0;
    TextView uText1;
    TextView uText2;
    Button uButtonLog;

    // system
    long startTime;
    File fileDirectory;
    PrintWriter logger;
    boolean isLogging;

    // microphone
    MicrophoneThread microphoneThread;

    // inertial
    SensorManager sensorManager;
    InertialSensor inertialSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uText0 = findViewById(R.id.text0);
        uText1 = findViewById(R.id.text1);
        uText2 = findViewById(R.id.text2);
        uButtonLog = findViewById(R.id.button_log);

        uButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLogStatus();
            }
        });

        // Enables Always-on
        setAmbientEnabled();

        onCreateLogger();
        onCreateMicrophone();
        onCreateInertial();

        startTime = System.currentTimeMillis();
    }

    void onCreateLogger() {
        fileDirectory = this.getApplicationContext().getExternalFilesDir(null);
        try {
            String fileName = "log_" + new SimpleDateFormat("yy-MM-dd_HH-mm-ss").format(new Date()) + ".txt";
            logger = new PrintWriter(new FileOutputStream(fileDirectory + "/" + fileName));
            isLogging = false;
        } catch (Exception e) {
            Log.d("file", e.toString());
        }
    }

    void onCreateMicrophone() {
        microphoneThread = new MicrophoneThread(this);
        microphoneThread.start();
    }

    void onCreateInertial() {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        inertialSensor = new InertialSensor(this);
    }

    void logToFile(String tag, Object... param) {
        if (!isLogging) return;
        logger.write(tag);
        switch (tag) {
            case "linearaccelerometer":
            case "gyroscope":
            case "megneticfield":
            case "gravity":
                float[] values0 = (float[]) param[0];
                for (float value : values0) {
                    logger.write(" " + value);
                }
                break;
            case "microphone":
                byte[] values1 = (byte[]) param[0];
                for (byte value : values1) {
                    logger.write(" "  + value);
                }
                break;
            case "time":
                logger.write(" " + System.currentTimeMillis());
                break;
        }
        logger.write("\n");
    }

    void showFrequency() {
        double runTime = (System.currentTimeMillis() - startTime) / 1000.0;
        uText0.setText(String.format("fIne: %.3f Hz", inertialSensor.counter / runTime));
        uText1.setText(String.format("fMic: %.3f Hz", microphoneThread.counter / runTime));
    }

    void changeLogStatus() {
        if (isLogging == true) {
            isLogging = false;
            uButtonLog.setText("LOG/OFF");
        } else {
            isLogging = true;
            uButtonLog.setText("LOG/ON");
        }
    }
}
