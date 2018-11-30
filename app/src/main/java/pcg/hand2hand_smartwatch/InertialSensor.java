package pcg.hand2hand_smartwatch;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;

public class InertialSensor implements SensorEventListener  {
    MainActivity father;
    int counter;

    Sensor sensorLinearAccelerometer;
    Sensor sensorGyroscope;
    Sensor sensorMegneticField;
    Sensor sensorGravity;

    float[] dataLinearAccelerometer;

    public InertialSensor(MainActivity father) {
        this.father = father;
        counter = 0;

        sensorLinearAccelerometer = father.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        father.sensorManager.registerListener(this, sensorLinearAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        sensorGyroscope = father.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        father.sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        /*sensorMegneticField = father.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        father.sensorManager.registerListener(this, sensorMegneticField, SensorManager.SENSOR_DELAY_FASTEST);

        sensorGravity = father.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        father.sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_FASTEST);*/
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        father.showFrequency();
        if (event.sensor == sensorLinearAccelerometer) {
            counter++;
            father.logToFile("linearaccelerometer", event.values);
            dataLinearAccelerometer = event.values.clone();
        }
        if (event.sensor == sensorGyroscope) {
            father.logToFile("gyroscope", event.values);
        }
        /*if (event.sensor == sensorMegneticField) {
            father.logToFile("megneticfield", event.values);
        }
        if (event.sensor == sensorGravity) {
            father.logToFile("gravity", event.values);
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
