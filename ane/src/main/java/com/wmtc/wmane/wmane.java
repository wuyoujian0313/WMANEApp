package com.wmtc.wmane;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

import java.lang.reflect.Field;

/**
 * Created by wuyoujian on 17/3/15.
 */

public class wmane implements FREExtension {
    @Override
    public void initialize() {

    }

    @Override
    public FREContext createContext(String s) {
        WMContext Context = new WMContext();
        return Context;
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
    public void dispose() {

    }
}
