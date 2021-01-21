package com.example.wewantour9;

public class StepDetector {

    private static final int SIZE_RING = 50;
    private static final int VEL_RING = 10;

    // The sensitivity is determined by the threshold. 25f very sensitive!
    private static final float THRESHOLD = 30f;  //SENSITIVITY

    private static final int STEP_DELAY_NS = 250000000;

    private int counterAccel = 0;
    private float[] accelRingX = new float[SIZE_RING];
    private float[] accelRingY = new float[SIZE_RING];
    private float[] accelRingZ = new float[SIZE_RING];
    private int counterVeloc = 0;
    private float[] velRing = new float[VEL_RING];
    private long lastStepTimeNs = 0;
    private float oldVelocityEstimate = 0;

    private StepListener listener;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }


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
        //STESSO VETTORE DIVENTA UN VETTORE UNITARIO

        //VETTORE Z PROVENIENTE DAL PRODOTTO SCALARE DEL VETTORE ATTUALE E IL VETTORE MEDIA
        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
        counterVeloc++;
        //ARRAY DI ULTIME ACCELERAZIONI Z
        velRing[counterVeloc % VEL_RING] = currentZ;

        float velocityEstimate = SensorFilter.sum(velRing);

        if (velocityEstimate > THRESHOLD && oldVelocityEstimate <= THRESHOLD
                && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
            listener.step(timeNs);
            lastStepTimeNs = timeNs;
        }
        oldVelocityEstimate = velocityEstimate;
    }
}
