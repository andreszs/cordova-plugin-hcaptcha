package com.andreszs.hcaptcha;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hcaptcha.sdk.*;
import com.hcaptcha.sdk.tasks.*;

import android.util.Log;

/**
 * Cordova plugin to verify hCaptcha on android.
 */
public class HCaptchaPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("verify")) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject arg_object = args.getJSONObject(0);
                        String siteKey = arg_object.getString("siteKey");
                        String locale = arg_object.has("locale") ? arg_object.getString("locale") : "AUTO";
                        boolean loading = arg_object.has("loading") && arg_object.getBoolean("loading");
                        int tokenExpiration = arg_object.has("tokenExpiration") ? arg_object.getInt("tokenExpiration") : 120;

                        verify(siteKey, locale, loading, tokenExpiration, callbackContext);
                    } catch (JSONException e) {
                        callbackContext.error("Verify called without providing siteKey");
                    }
                }
            });
            return true;
        }
        return false;
    }

    private void verify(String siteKey, String locale, boolean loading, int tokenExpiration, CallbackContext callbackContext) {
        final CallbackContext finalCallbackContext = callbackContext;

        HCaptchaSize hCaptchaSize;
        hCaptchaSize = HCaptchaSize.INVISIBLE;

        HCaptchaOrientation hCaptchaOrientation;
        hCaptchaOrientation = HCaptchaOrientation.PORTRAIT;
        
        HCaptchaTheme hCaptchaTheme;
        hCaptchaTheme = HCaptchaTheme.LIGHT;

        final HCaptcha hCaptcha = HCaptcha.getClient(cordova.getActivity());
        final HCaptchaConfig hCaptchaConfig = HCaptchaConfig.builder()
                .siteKey(siteKey)
                .size(hCaptchaSize)
                .orientation(hCaptchaOrientation)
                .theme(hCaptchaTheme)
                .locale(locale)
                .loading(loading)
                .tokenExpiration(tokenExpiration)
                .build();

        hCaptcha
                .addOnSuccessListener(new OnSuccessListener<HCaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(HCaptchaTokenResponse response) {
                        String userResponseToken = response.getTokenResult();
                        if (!userResponseToken.isEmpty()) {
                            finalCallbackContext.success(userResponseToken);
                        } else {
                            finalCallbackContext.error("Response token was empty");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(HCaptchaException e) {
                        finalCallbackContext.error("hCaptcha failed: " + e.getMessage() + "(" + e.getStatusCode() + ")");
                    }
                })
                .addOnOpenListener(new OnOpenListener() {
                    @Override
                    public void onOpen() {
                        Log.d("hCaptcha", "hCaptcha is now visible.");
                    }
                });

        hCaptcha.verifyWithHCaptcha(hCaptchaConfig);
    }
}
