package com.wmtc.wmane;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.wmtc.wmane.ANEFunction.IsinstallFunction;
import com.wmtc.wmane.ANEFunction.RegisterSDKFunction;
import com.wmtc.wmane.ANEFunction.SendImageFunction;
import com.wmtc.wmane.ANEFunction.SendImageURLFunction;
import com.wmtc.wmane.ANEFunction.SendLinkFunction;
import com.wmtc.wmane.ANEFunction.SendTextFunction;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuyoujian on 17/3/15.
 */

public class WMContext extends FREContext {

//    ANE_FUNCTION(sharing_function_register);
//    ANE_FUNCTION(sharing_function_text);
//    ANE_FUNCTION(sharing_function_link);
//    ANE_FUNCTION(sharing_function_image);
//    ANE_FUNCTION(sharing_function_image_url);
//    ANE_FUNCTION(sharing_function_is_installed);

    private static final String SHARING_FUNCTION_REGISTER = "sharing_function_register";
    private static final String SHARING_FUNCTION_TEXT = "sharing_function_text";
    private static final String SHARING_FUNCTION_LINK = "sharing_function_link";
    private static final String SHARING_FUNCTION_IMAGE = "sharing_function_image";
    private static final String SHARING_FUNCTION_IMAGE_URL = "sharing_function_image_url";
    private static final String SHARING_FUNCTION_IS_INSTALLED = "sharing_function_is_installed";

    @Override
    public void dispose() {

    }


    // com.wmtc.wmane
    public static void setResourctID(String rName,FREContext context) throws Exception{
        Class<?> R = Class.forName(rName);
        Class<?>[] clss = R.getDeclaredClasses();
        for (int i = 0; i < clss.length; i++) {
            Class<?> cls = clss[i];
            Field[] flds = cls.getDeclaredFields();
            for (int j = 0; j < flds.length; j++) {
                Field fld = flds[j];
                fld.setAccessible(true);
                Object obj = cls.newInstance();
                String name = cls.getSimpleName()+"."+fld.getName();
                int id = context.getResourceId(name);
                fld.set(obj, id);
            }
        }
    }



    @Override
    public Map<String, FREFunction> getFunctions() {
        Map<String, FREFunction> map = new HashMap<String, FREFunction>();
        //映射
        map.put(SHARING_FUNCTION_REGISTER, new RegisterSDKFunction());
        map.put(SHARING_FUNCTION_TEXT, new SendTextFunction());
        map.put(SHARING_FUNCTION_LINK, new SendLinkFunction());
        map.put(SHARING_FUNCTION_IMAGE, new SendImageFunction());
        map.put(SHARING_FUNCTION_IMAGE_URL, new SendImageURLFunction());
        map.put(SHARING_FUNCTION_IS_INSTALLED, new IsinstallFunction());
        return map;
    }

}
