package com.example.wewantour9;

import android.util.Log;

public class StepDetector {

    private static final int SIZE_RING = 50;
    private static final int ACCEL_RING = 10;

    // The sensitivity is determined by the threshold. 25f very sensitive!
    private static final float THRESHOLD = 30f;  //SENSITIVITY

    private static final int STEP_DELAY_NS = 250000000;

    private int counterAccel = 0;
    private float[] accelRingX = new float[SIZE_RING];
    private float[] accelRingY = new float[SIZE_RING];
    private float[] accelRingZ = new float[SIZE_RING];
    private int counterVel = 0;
    private float[] velRing = new float[ACCEL_RING];
    private long lastStepTimeNs = 0;
    private float oldAccelerationEstimate = 0;

    private StepListener listener;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

/*L'idea è quella di calcolare il passo solamente quando il campionamento cambia direzione
  velocemente (simulando un passo reale), ossia quando nei 50 campionamenti dell Accelerazione (SIZE RING) si nota un
  cambio di direzione.
 */
    public void updateAcceleration(long timeNs, float x, float y, float z) {
        float[] currentAccel = new float[3];
        currentAccel[0] = x;
        currentAccel[1] = y;
        currentAccel[2] = z;

        // WHERE THE GLOBAL VECTOR IS?
        counterAccel++;
        accelRingX[counterAccel % SIZE_RING] = currentAccel[0];   //accelRingCounter % ACCEL_RING_SIZE
        accelRingY[counterAccel % SIZE_RING] = currentAccel[1];
        accelRingZ[counterAccel % SIZE_RING] = currentAccel[2];

        float[] worldZ = new float[3];
        //MEDIA DEL CAMPIONAMENTO DI 50 VALORI DELL ACCELERAZIONE --
        worldZ[0] = SensorFilter.sum(accelRingX) / Math.min(counterAccel, SIZE_RING);
        worldZ[1] = SensorFilter.sum(accelRingY) / Math.min(counterAccel, SIZE_RING);
        worldZ[2] = SensorFilter.sum(accelRingZ) / Math.min(counterAccel, SIZE_RING);

        //NORMALIZZAZIONE DEL VETTORE RISULTANTE DELLA MEDIA -- NORMALIZE OF THIS VECTOR
        float normalization_factor = SensorFilter.norm(worldZ);
        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;
        //VETTORE NORMALIZZATO CHE MI DA SOLO LA DIREZIONE QUINDI

        //VETTORE Z PROVENIENTE DAL PRODOTTO SCALARE DEL VETTORE ATTUALE E IL VETTORE MEDIA
        /*Geometricamente vuol dire che quando i due vettori analizzati sono ortogonali il risultato sarà 0.
        Mentre se i valori hanno la stessa direzione con verso uguale o diverso avranno il valore massimo.
        Il normalization factor è la norma del vettore della media che è uguale alla velocità di gravità (9.81) quando
        il telefono è fermo */
        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
        counterVel++;
        //ARRAY DI ULTIME ACCELERAZIONI Z
        velRing[counterVel % ACCEL_RING] = currentZ;

        //LOG DI DEBUGGING
        //Log.e("CurrentZ= SensorFilter.dot(worldZ, currentAccel)", currentZ+"= SensorFilter.dot(|"+worldZ[0]+"|"+worldZ[1]+"|"+worldZ[2]+"|"+", |"+currentAccel[0]+"|"+currentAccel[1]+"|"+currentAccel[2]+"|"+")");

        float accelerationEstimate = SensorFilter.sum(velRing);

        /*Quando cambiamo direzione (simulando il passo) accelerationEstimate è positivo mentre oldAccelerationEstimate
          è negativo*/
        if (accelerationEstimate > THRESHOLD && oldAccelerationEstimate <= THRESHOLD
                && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }
        oldAccelerationEstimate = accelerationEstimate;
    }
}
