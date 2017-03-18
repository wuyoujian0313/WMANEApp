package com.wmtc.wmane.ANEFunction;

import android.widget.Toast;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.wmtc.wmane.ANETypeConversion;
import com.wmtc.wmane.SharedManager;

/**
 * Created by wuyoujian on 17/3/15.
 */

public class SendLinkFunction implements FREFunction {
    @Override
    public FREObject call(FREContext freContext, FREObject[] freObjects) {

        String value = ANETypeConversion.FREObject2String(freObjects[2]);
        if (value != null) {

            SharedManager.SharedDataModel model = SharedManager.getSingleton().new SharedDataModel();

            model.dataType = SharedManager.E_SharedDataType.SharedDataTypeURL;
            model.url = value;

            String stringTitle = ANETypeConversion.FREObject2String(freObjects[0]);
            if (stringTitle != null) {
                model.title = stringTitle;
            }

            String stringText = ANETypeConversion.FREObject2String(freObjects[1]);
            if (stringText != null) {
                model.content = stringText;
            }

            SharedManager.getSingleton().sharedData(model, new SharedManager.SharedFinishCallback() {
                @Override
                public void finishSharedCallback(int statusCode, Object resp) {
                    //
                }
            });
        }

        return null;
    }
}
