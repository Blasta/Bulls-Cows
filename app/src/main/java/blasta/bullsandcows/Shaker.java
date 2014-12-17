package blasta.bullsandcows;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by 1 on 17.12.2014.
 */
public class Shaker {

    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private float threshold = 2.5f;
    private int shakeCountNeed = 4;
    private int shakeCount = 4;

    private SensorManager sensorManager;
    private Callback cb = null;

    public Shaker(Context context, float threshold, int shakeCount, Callback cb) {
        this.threshold = threshold;
        this.shakeCount = shakeCount;
        this.cb = cb;

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        start();
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                if (isShakeEnough(event.values[0], event.values[1], event.values[2]))
                    cb.shakingEvent();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void start() {
        sensorManager.registerListener(
                listener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI
        );
    }

    public void stop(){
        sensorManager.unregisterListener(listener);
    }

    private boolean isShakeEnough(float x, float y, float z){
        double force = 0.0d;
        force += Math.pow((x - lastX) / SensorManager.GRAVITY_EARTH, 2.0);
        force += Math.pow((y - lastY) / SensorManager.GRAVITY_EARTH, 2.0);
        force += Math.pow((z - lastZ) / SensorManager.GRAVITY_EARTH, 2.0);
        force = Math.sqrt(force);

        lastX = x;
        lastY = y;
        lastZ = z;

        if (force > threshold){
            shakeCount++;
            if (shakeCount > shakeCountNeed){
                shakeCount = 0;
                lastX = 0;
                lastY = 0;
                lastZ = 0;
                return true;
            }
        }
        return false;
    }
}
