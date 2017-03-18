package com.wmtc.wmane;

/**
 * Created by wuyoujian on 17/3/14.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.*;
import com.tencent.mm.opensdk.openapi.*;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SharedManager implements ActionSheet.IActionSheetListener {

    private ActionSheet actionSheet;
    private Activity activity;
    private List<AISharedPlatformSDKInfo> sdkInfos;
    private List<AISharedPlatformScene> scenes;

    private SharedFinishCallback callback;
    private SharedDataModel data;

    // 微信
    private static final  String WX_APP_ID = "wx7a296d05150143e5";
    private static final  String WX_APP_SECRET = "dce5699086e990df3104052ce298f573";
    private static final  String WX_APP_REDIRECTURI = "";
    private IWXAPI wx_api;

    private static final  String QQ_APP_ID = "1105282903";
    private static final  String QQ_APP_SECRET = "HDTXtnSO0WkAPIgc";
    private static final  String QQ_APP_REDIRECTURI = "";


    public static final SharedManager getSingleton() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final SharedManager INSTANCE = new SharedManager();
    }

    public boolean isInstallSharedApp () {
        return wx_api.isWXAppInstalled();
    }

    public void regiterSharedSDK(Activity activity) {

        this.activity = activity;
        this.sdkInfos = new ArrayList<>();
        this.scenes = new ArrayList<>();

        this.actionSheet = new ActionSheet(this.activity);
        this.actionSheet.setCancelable(false);
        this.actionSheet.setCanceledOnTouchOutside(true);

        AISharedPlatformSDKInfo sdk1 = new AISharedPlatformSDKInfo(E_AIPlatfrom.AIPlatfromWechat,
                WX_APP_ID,WX_APP_SECRET,WX_APP_REDIRECTURI);
        this.sdkInfos.add(sdk1);

        registerSharedPlatform();
    }


    public void sharedData(SharedDataModel data,SharedFinishCallback callback)  {
        this.data = data;
        this.callback = callback;
        this.actionSheet.show();
    }

    @Override
    public void onActionSheetItemClick(ActionSheet actionSheet, int itemPosition, ActionSheet.ItemModel itemModel) {
        AISharedPlatformScene scene = this.scenes.get(itemPosition);
        if (scene.platfrom == E_AIPlatfrom.AIPlatfromWechat) {
            sharedToWeixin(scene);
        } else if (scene.platfrom == E_AIPlatfrom.AIPlatfromQQ) {
            sharedToQQ(scene);
        } else if (scene.platfrom == E_AIPlatfrom.AIPlatfromWeibo ) {
            sharedToWeibo(scene);
        }
    }

    private void registerSharedPlatform() {

        for (AISharedPlatformSDKInfo item:this.sdkInfos) {

            E_AIPlatfrom platform = item.platfrom;
            if (platform == E_AIPlatfrom.AIPlatfromWechat) {
                wx_api = WXAPIFactory.createWXAPI(this.activity,WX_APP_ID,true);
                wx_api.registerApp(WX_APP_ID);

                this.scenes.add(new AISharedPlatformScene(platform,E_AIPlatformScene.AIPlatformSceneSession,"分享到微信好友"));
                this.scenes.add(new AISharedPlatformScene(platform,E_AIPlatformScene.AIPlatformSceneTimeline,"分享到微信朋友圈"));
                this.scenes.add(new AISharedPlatformScene(platform,E_AIPlatformScene.AIPlatformSceneFavorite,"分享到微信收藏"));
            } else if(platform == E_AIPlatfrom.AIPlatfromQQ) {

            } else if (platform == E_AIPlatfrom.AIPlatfromWeibo) {

            }
            addActionSheetMenu();
        }
    }

    private void addActionSheetMenu() {
        List<String> menus = new ArrayList<>();
        for (AISharedPlatformScene scene:this.scenes ) {
            menus.add(scene.sceneName);
        }

        this.actionSheet.setOtherButtonTitlesSimple(menus);
    }

    private void sharedToWeixin(AISharedPlatformScene scene) {

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = scene.scene.ordinal();

        if (this.data.dataType == E_SharedDataType.SharedDataTypeText) {

            WXTextObject textObj = new WXTextObject();
            textObj.text = this.data.content;

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = textObj;

            req.transaction = APIHelper.buildTransaction("text");
            req.message = msg;
        } else if (this.data.dataType == E_SharedDataType.SharedDataTypeImage) {
            //req.
            if (this.data.image != null) {
                WXImageObject imgObj = new WXImageObject(this.data.image);
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;

                Bitmap thumbBmp = Bitmap.createScaledBitmap(this.data.image, APIHelper.THUMB_SIZE, APIHelper.THUMB_SIZE, true);
                msg.thumbData = APIHelper.bmpToByteArray(thumbBmp,true);

                req.transaction = APIHelper.buildTransaction("image");
                req.message = msg;
            }
        } else if (this.data.dataType == E_SharedDataType.SharedDataTypeImageURL) {

            WXImageObject imageObject = new WXImageObject();

            WXMediaMessage msg = new WXMediaMessage();
            try {
                Bitmap bmp = BitmapFactory.decodeStream(new URL(this.data.url).openStream());
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, APIHelper.THUMB_SIZE, APIHelper.THUMB_SIZE, true);

                msg.thumbData = APIHelper.bmpToByteArray(thumbBmp, true);
                imageObject.imageData = APIHelper.bmpToByteArray(bmp,true);
                msg.mediaObject = imageObject;

            } catch (Exception e) {
                e.printStackTrace();;
                return;
            }

            req.transaction = APIHelper.buildTransaction("imageURL");
            req.message = msg;
        } else if (this.data.dataType == E_SharedDataType.SharedDataTypeURL) {

            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = this.data.url;

            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = this.data.title;
            msg.description = this.data.content;

            req.transaction = APIHelper.buildTransaction("webpage");
            req.message = msg;
        }

        wx_api.sendReq(req);
    }


    private void sharedToQQ(AISharedPlatformScene scene) {

    }

    private void sharedToWeibo(AISharedPlatformScene scene) {

    }


    public interface SharedFinishCallback {
        void finishSharedCallback(int statusCode, Object resp);
    }

    private static class APIHelper {

        private static final int THUMB_SIZE = 100;
        public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
            if (needRecycle) {
                bmp.recycle();
            }

            byte[] result = output.toByteArray();
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        public static String buildTransaction(final String type) {
            return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
        }
    }


    private static enum E_AIPlatfrom {
        AIPlatfromWechat,
        AIPlatfromQQ,
        AIPlatfromWeibo
    }

    private static enum  E_AIPlatformScene {
        AIPlatformSceneSession,
        AIPlatformSceneTimeline,
        AIPlatformSceneFavorite
    }

    private class AISharedPlatformSDKInfo {

        public E_AIPlatfrom platfrom;
        public String appId;
        public String appSecret;
        public String redirectURI;


        AISharedPlatformSDKInfo (E_AIPlatfrom platfrom,String appId,String appSecret,String redirectURI) {
            this.platfrom = platfrom;
            this.appId = appId;
            this.appSecret = appSecret;
            this.redirectURI = redirectURI;
        }
    }

    private class AISharedPlatformScene {
        public  E_AIPlatfrom platfrom;
        public  E_AIPlatformScene scene;
        public  String sceneName;
        AISharedPlatformScene(E_AIPlatfrom platfrom, E_AIPlatformScene scene,String sceneName) {
            this.platfrom = platfrom;
            this.scene = scene;
            this.sceneName = sceneName;
        }
    }

    public static enum E_SharedDataType {
        SharedDataTypeText,     // 文字分享
        SharedDataTypeImage,    // 图片分享
        SharedDataTypeImageURL, // 远程图片分享
        SharedDataTypeURL,      // 网页分享
        SharedDataTypeMusic,    // 音乐分享
        SharedDataTypeVideo,    // 视频分享
    }

    /* ！！！！！！！分享支持的类型说明
    1、微信和新浪微博都支持SharedDataType定义的类型
    2、QQ目前为了QQ聊天和QQ空间统一，就都只是支持：
    SharedDataTypeText、SharedDataTypeImage、SharedDataTypeURL这4个类型的分享
    */

    // ！！！！！！ 请注意上面支持的类型说明
    public class SharedDataModel {

        public E_SharedDataType dataType;
        public String title;
        public String content;
        public String url;
        public String lowBandUrl;
        public Bitmap image;
    }
}

