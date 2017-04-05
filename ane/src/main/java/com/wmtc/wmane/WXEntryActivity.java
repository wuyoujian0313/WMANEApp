package com.wmtc.wmane;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private  WXAccessTokenInfo accessTokenInfo;
    private  WXUserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {}

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) resp).code;

                String appId = SharedManager.WX_APP_ID;
                String appSecret = SharedManager.WX_APP_SECRET;

                final String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                        appId,appSecret,code);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();

                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();

                            Gson gson = new Gson();
                            accessTokenInfo = gson.fromJson(responseData,WXAccessTokenInfo.class);

                            final String infoUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                                    accessTokenInfo.getAccess_token(),accessTokenInfo.getOpenid());

                            Request requestInfo = new Request.Builder()
                                    .url(infoUrl)
                                    .build();

                            Response responseInfo = client.newCall(requestInfo).execute();
                            String infoString = responseInfo.body().string();
                            userInfo = gson.fromJson(infoString,WXUserInfo.class);

                            Toast.makeText(SharedManager.mActivity,"User Nickname is " + userInfo.getNickname(),Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
        this.finish();
    }


    private class WXAccessTokenInfo {
        private String access_token;
        private String expires_in;
        private String refresh_token;
        private String openid;
        private String scope;

        public String getAccess_token(){
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getExpires_in(){
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }
    private class WXUserInfo {
        private String openid;
        private String nickname;
        private String sex;
        private String province;
        private String city;
        private String country;
        private String headimagurl;
        private String unionid;

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getOpenid() {
            return openid;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getHeadimagurl() {
            return headimagurl;
        }

        public String getNickname() {
            return nickname;
        }

        public String getProvince() {
            return province;
        }

        public String getSex() {
            return sex;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setHeadimagurl(String headimagurl) {
            this.headimagurl = headimagurl;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
    }
}
