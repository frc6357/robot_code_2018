//package org.usfirst.frc6357.robotcode.tools.estimation;
//
//import org.ejml.simple.*;
//
//// NOTE: The math in this implementation is for:
//// Time in seconds
//// Distance in Meters
//// Velocity in Meters Per Second
//// Acceleration in Meters Per Second Per Second
//
//public class KalmanFilter1D {
//
//    private SimpleMatrix F;     // The state transition matrix, 2x2
//    private SimpleMatrix B;     // The input matrix
//    private SimpleMatrix state; // The state matrix, 2x1
//    private SimpleMatrix P;     // The posteriori covariance matrix, 2x2
//    private SimpleMatrix Q;     // The process noise covariance, 2x2
//    private double S;           // The Innovation Covariance, scalar
//    private SimpleMatrix K;     // The Kalman Gain, 2x1
//    private SimpleMatrix H;     // The observation matrix, 1x2
//    private double R;           // The observation noise covariance, scalar
//    private SimpleMatrix I;     // The Identity Matrix, 2x2
//
//    public KalmanFilter1D(){
//        Initialize(0);
//    }
//
//    /**
//     * This function initializes (or re-initializes) the KF
//     *
//     * @param initialDistance Distance in meters
//     */
//    public void Initialize(double initialDistance){
//        state = new SimpleMatrix(2, 1);
//        F = new SimpleMatrix(2,2);
//        B = new SimpleMatrix(2,1);
//        P = new SimpleMatrix(2,2);
//        Q = new SimpleMatrix(2,2);
//        H = new SimpleMatrix(1, 2);
//        I = new SimpleMatrix(2,2);
//
//        // state is the state
//        // state is a column vector of position and velocity
//        // The initial position is recorded here.
//        // We assume initial velocity is 0
//        state.set(0,0, initialDistance);
//
//        // F: The state transition matrix
//        // For each state transition, we integrate
//        // So the new pos is old pos plus velocity times delta T
//        // The new velocity is the old velocity plus acc times delta T
//        // This means the major axis of F is 1, and the top right element is delta T
//        // We set the delta T in the predict function
//        F.set(0,0,1);
//        F.set(1,1,1);
//
//        // H is the observation matrix
//        // It basically picks which state element we want to look at
//        // For position, this is a 2 element row vector [1, 0]
//        H.set(0,0,1);
//
//        // I is a pre-allocated identity matrix for use later
//        I.set(0,0,1);
//        I.set(1,1,1);
//
//        // Q is the process noise covariance, based on the sensor
//        // For ADIS16448, this is 5.1 mg RMS plus 0.883 mg in quantization noise
//        // So, total sensor noise (stdev) is the RMS sum, or 5.176 mg (0.005176)
//        // The sensor variance is the square of this
//        // Each integration cuts the variance by a factor of two
//        // The cross-covariance delta T times the sensor noise. We assume deltaT=0.05 for this
//        // All of these are multiplied by 9.8 to give m/s/s instead of g
//        // The data here are for m/s/s
//        Q.set(0,0,0.0001312757824);
//        Q.set(0,1,0.000013127541);
//        Q.set(1,0,0.000013127541);
//        Q.set(1,1,0.00026255082);
//
//        // P is the process estimate covariance, this is the initial estimate, and is equal to Q
//        P.set(Q);
//
//        // R is the measurement covariance, set by the sensitivity of the LiDAR
//        // This is 1cm, since that's the resolution in the message
//        R = 0.01;
//    }
//
//    /**
//     * This function returns the position result of the prediction
//     * Try to call this at a very regular interval
//     * This can be called while robot is disabled, so long as accel measurement is available
//     *
//     * @param acceleration The new acceleration in m/s/s
//     * @param deltaT The time since the last prediction _or_ update
//     * @return The resultant prediction of position
//     */
//    public double predict(double acceleration, double deltaT){
//        // Update the state transition matrix with the delta T since last prediction
//        F.set(0,1,deltaT);
//        // Update the input matrix with the accel measured, scaled by delta T
//        B.set(1,0, acceleration*deltaT);
//
//        // Update the state based on acceleration
//        state = F.mult(state);
//        state = state.plus(B);
//
//        // Update the covariance estimate
//        P = F.mult(P).mult(F.transpose()).plus(Q);
//        return H.mult(state).get(0);
//    }
//
//    /**
//     * This function returns the position result of the update
//     *
//     * @param observation The measurement from the LiDAR in meters
//     * @return The updated estimate of position
//     */
//    public double update(double observation){
//        double innovation;
//        // Calculate the innovation/measurement residual
//        innovation = observation - H.mult(state).get(0);
//        // Calculate the residutal covariance
//        S = R + (H.mult(P).mult(H.transpose())).get(0);
//        // Calculate the Kalman gain
//        K = P.mult(H.transpose()).divide(S);
//        // Update the state based on the kalman gain and innovation
//        state = state.plus(K.scale(innovation));
//        // Update the covariance estimate
//        SimpleMatrix temp = I.minus(K.mult(H));
//        P = temp.mult(P).mult(temp.transpose());
//        P = P.plus(K.mult(K.transpose())).scale(R);
//
//        return H.mult(state).get(0);
//    }
//
//}
