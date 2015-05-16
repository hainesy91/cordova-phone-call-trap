package io.gvox.phonecalltrap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import java.lang.reflect.Method;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONArray;

interface ITelephony {      

boolean endCall();     

//void answerRingingCall();      

//void silenceRinger(); 

}


public class PhoneCallTrap extends CordovaPlugin {

    CallStateListener listener;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if( "onCall".equals(action))
            {
            prepareListener();

            listener.setCallbackContext(callbackContext);
            }
         else  if( "endCall".equals(action))
            {
                EndCall();
            }

        return true;
    }

    private void prepareListener() {
        if (listener == null) {
            listener = new CallStateListener();
            TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyMgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
       private void EndCall() {
           
           // TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
      
            try{
               Class clazz = Class.forName(TelephonyMgr.getClass().getName());
                           
               Method method = clazz.getDeclaredMethod("getITelephony");
               method.setAccessible(true);
            
              ITelephony telephonyService = (ITelephony) method.invoke(TelephonyMgr);
              telephonyService.endCall();
            } catch ( ClassNotFoundException e ) {
            
            } catch ( NoSuchMethodException e ) {
         
            } catch ( IllegalAccessException e ) {
                
                
            }catch ( InvocationTargetException e ) {}
            
          
    
    }
}

class CallStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

        String msg = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
            msg = "IDLE";
            break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
            msg = "OFFHOOK";
            break;

            case TelephonyManager.CALL_STATE_RINGING:
            msg = "RINGING";
            break;
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, msg+":"+incomingNumber);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
