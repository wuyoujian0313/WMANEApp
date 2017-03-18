package com.wmtc.wmane.ANEFunction;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.wmtc.wmane.SharedManager;

/**
 * Created by wuyoujian on 17/3/15.
 */

public class RegisterSDKFunction implements FREFunction {
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        try {
            Activity activity = freContext.getActivity();

            SharedManager.getSingleton().regiterSharedSDK(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
