package com.pianomusicdrumpad.pianokeyboard.exit;


import org.json.JSONObject;

/***
 * This Interface is used for the Callback listner for the API call
 */
public class OnTaskCompleted {


    public interface CallBackListener {
        void onTaskCompleted(JSONObject result);

        void onTaskCompleted(JSONObject result, String Method);

        void onTaskCompleted(Boolean result);

    }

    CallBackListener listener;

    public OnTaskCompleted(CallBackListener listener) {
        this.listener = listener;
    }

    public void onTaskCompleted(JSONObject result) {
        listener.onTaskCompleted(result);
    }

    public void onTaskCompleted(JSONObject result, String Method) {
        listener.onTaskCompleted(result, Method);
    }

    public void onTaskCompleted(Boolean result) {
        listener.onTaskCompleted(result);
    }

}
