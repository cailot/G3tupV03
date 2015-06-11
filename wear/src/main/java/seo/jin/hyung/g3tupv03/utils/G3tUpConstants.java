package seo.jin.hyung.g3tupv03.utils;

/**
 * Created by jinseo on 2015. 6. 9..
 */
public interface G3tUpConstants
{
    public static final String TAG = "### JINHYUNG ###";

    public static final int DEFAULT_VIBRATION_DURATION_MS = 200; // in millis

    public static final String PREF_KEY_COUNTER = "counter";



    /** an up-down movement that takes more than this will not be registered as such **/
    public static final long TIME_THRESHOLD_NS = 2000000000; // in nanoseconds (= 2sec)

    /**
     * Earth gravity is around 9.8 m/s^2 but user may not completely direct his/her hand vertical
     * during the exercise so we leave some room. Basically if the x-component of gravity, as
     *
     * measured by the Gravity sensor, changes with a variation (delta) > GRAVITY_THRESHOLD,
     * we consider that a successful count.
     */
    public static final float GRAVITY_THRESHOLD = 7.0f;






    public static final long SECOND = 1000; // in milliseconds



// staus of fragment
    public static final int COUNTER_STATE = 0;
    public static final int EXERCISE_STATE = 1;
    public static final int DISPLAY_STATE = 2;



}
