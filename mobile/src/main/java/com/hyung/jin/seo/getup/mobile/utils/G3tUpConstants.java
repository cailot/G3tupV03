package com.hyung.jin.seo.getup.mobile.utils;

/**
 * Created by jinseo on 2015. 6. 9..
 */
public interface G3tUpConstants
{
    public static final String TAG = "### JINHYUNG ###";





    public static final long SECOND = 1000; // in milliseconds



// staus of fragment
    public static final int START_STATE = 0;

    public static final int COUNTER_STATE = START_STATE;

    public static final int EXERCISE_STATE = START_STATE + 1;

    public static final int DISPLAY_STATE = START_STATE + 2;


    public static final String COMMUNICATIO_PATH = "/wearable/hooes";

    public static final String ALARM_START = "alarm_start";

    public static final String ALARM_STOP = "alarm_stop";

    public static final int ALARM_START_STATE = START_STATE + 3;

    public static final int ALARM_STOP_STATE = START_STATE + 4;


    public static final String FLAG_FROM_CLIENT = "flag_from_client";


    public static final String ACTION = "action";

    public static final String SOUND = "sound";

    public static final String VIBRATION = "vibration";

    public static final String RELEASE = "release";



}
