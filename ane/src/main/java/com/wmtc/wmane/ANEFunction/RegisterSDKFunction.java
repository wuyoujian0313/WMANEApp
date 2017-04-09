package com.wmtc.wmane.ANEFunction;

import android.app.Activity;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.wmtc.wmane.SharedSDK.SharedManager;
import com.wmtc.wmane.WMANEShare;

/**
 * Created by wuyoujian on 17/3/15.
 */

public class RegisterSDKFunction implements FREFunction {
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {
        try {
            Activity activity = freContext.getActivity();
            WMANEShare.getSingleton().setFreContext(freContext);

            SharedManager.getSingleton().regiterSharedSDK(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
}
