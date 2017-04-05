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

    @Override
    public void dispose() {

    }
}
