package com.newtouch.mywebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SafeWebViewBridge.InjectedChromeClient;
import cn.pedant.SafeWebViewBridge.JsCallback;

public class MainActivity extends AppCompatActivity implements BaseTitleBar.onLeftBackClickListener, BaseTitleBar.onLeftFinishClickListener, BaseTitleBar.onRightImageViewClickListener, BaseTitleBar.onRightTextViewClickListener {

    private CoreWebView coreWebView;
    private String url;
    private String token;
    private String appId;
    private WeChatShareUtil instance;
    private Bitmap bitmap;
    private Dialog dialog;
    private JSONObject RightMsg;
    private JSONObject leftMsg;
    private BaseTitleBar titleBar;
    public static MainActivity webViewActivity;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            setTranslucentStatus(true, this);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.trans_black);//通知栏所需颜色
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(this.getResources().getColor(R.color.color_));

        }
        setContentView(R.layout.activity_web_view);
        webViewActivity = this;
        url = getIntent().getStringExtra("url");
        token = getIntent().getStringExtra("token");
        appId = getIntent().getStringExtra("appId");

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, token);
        CookieSyncManager.getInstance().sync();
        titleBar = findViewById(R.id.titleBar);
        titleBar.setOnLeftBackClickListener(this);
        titleBar.setOnLeftFinishClickListener(this);
        titleBar.setOnRightImageViewClickListener(this);
        titleBar.setOnRightTextViewClickListener(this);

        coreWebView = findViewById(R.id.coreWebView);
        coreWebView.setWebViewClient(new WebViewClient());
        coreWebView.setUserAgentString("ruitaiAppAndroid/" + getVersionName(this));
        coreWebView.setWebChromeClient(
                new InjectedChromeClient("ruitaiApp", WebViewActivity.AndroidJS.class)
        );

        coreWebView.loadUrl(url);
    }


    @Override
    public void onTextViewClickListener(View v) {
        try {
            if (RightMsg != null && RightMsg.getString("url") != null && RightMsg.getString("url").length() > 0) {
                Intent intent = new Intent(webViewActivity, MainActivity.class);
                intent.putExtra("url", RightMsg.getString("url"));
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageViewClickListener(View v) {
        initShareDialog();
    }

    @Override
    public void onFinishClickListener(View v) {
        finish();
    }

    @Override
    public void onBackClickListener(View v) {
        try {
            if(leftMsg != null && !TextUtils.isEmpty(leftMsg.getString("method"))) {
                coreWebView.loadUrl("javascript:"+leftMsg.getString("method")+"()");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class AndroidJS {

        public static void setRightTitle(WebView webView, JSONObject msg) {

            try {
                MainActivity.webViewActivity.RightMsg = msg;
                if (msg != null && msg.getString("showRightTitle") != null && msg.getString("showRightTitle").length() > 0) {
                    String showRightTitle = msg.getString("showRightTitle");
                    if ("0".equals(showRightTitle)) {
                        MainActivity.webViewActivity.titleBar.setRightView("", 0);
                    } else if ("1".equals(showRightTitle)) {
                        if(msg.getString("rightTitle") != null){
                            MainActivity.webViewActivity.titleBar.setRightView(msg.getString("rightTitle"), 1);
                        }
                    } else if ("2".equals(showRightTitle)) {
                        MainActivity.webViewActivity.titleBar.setRightView(showRightTitle, 2);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public static void setLeftTitle(WebView webView, JSONObject msg) {

            try {
                MainActivity.webViewActivity.leftMsg = msg;
                if (msg != null && msg.getString("showLeftTitle") != null) {
                    String showRightTitle = msg.getString("showLeftTitle");
                    if ("0".equals(showRightTitle)) {
                        MainActivity.webViewActivity.titleBar.setLeftView(0);
                    } else if ("1".equals(showRightTitle)) {
                        MainActivity.webViewActivity.titleBar.setLeftView(1);
                    } else if ("2".equals(showRightTitle)) {
                        MainActivity.webViewActivity.titleBar.setLeftView(2);
                    } else if ("3".equals(showRightTitle)) {
                        MainActivity.webViewActivity.titleBar.setLeftView(3);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * @param webView
         * @param msg     设置 header 标题
         */
        public static void setTitle(WebView webView, String msg) {
//            try {
//                if (msg != null && msg.getString("showTitle") != null && msg.getString("showTitle").length() > 0) {
//                    if ("0".equals(msg.getString("showTitle")) && msg.getString("title") != null) {
//                        WebViewActivity.webViewActivity.titleBar.setTitle(msg.getString("title"), 0);
//                    } else if ("1".equals(msg.getString("showTitle"))) {
//                        WebViewActivity.webViewActivity.titleBar.setTitle("", 1);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            MainActivity.webViewActivity.titleBar.setTitle(msg, 0);

        }

        /**
         * 跳转登录页面
         */
        public static void jumpLogin(WebView webView, JsCallback jsCallback) {
            WritableMap params = Arguments.createMap();
            sendEvent(RNReactNativeMywebviewModule.reactContext, "jumpLogin", params);
            try {
                jsCallback.apply("sdfsdf");
            } catch (JsCallback.JsCallbackException e) {
                e.printStackTrace();
            }
        }

        /**
         * 跳转rn页面
         */
        public static void jumpToRn(WebView webView, JSONObject msg) {
            WritableMap params = Arguments.createMap();
            sendEvent(RNReactNativeMywebviewModule.reactContext, "jumpToRn", msg.toString());
            //TaskExecutor.scheduleTaskOnUiThread(2000, new Runnable() {
            //    @Override
            //    public void run() {
            //        WebViewActivity.webViewActivity.finish();

            //    }
            //});
        }

        public static void jumpShare(WebView webView, JSONObject msg){
            if (msg != null) {
                WritableMap params = Arguments.createMap();
                try {
                    params.putString("pdfUrl",msg.getString("pdfUrl"));
                    params.putString("returnUrl",msg.getString("returnUrl"));
                    params.putString("pdfId",msg.getString("pdfId"));
                    params.putString("mailTitle",msg.getString("mailTitle"));
                    params.putString("mailDes",msg.getString("mailDes"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendEvent(RNReactNativeMywebviewModule.reactContext, "jumpShare", params);
                TaskExecutor.scheduleTaskOnUiThread(2000, new Runnable() {
                    @Override
                    public void run() {
                        WebViewActivity.webViewActivity.finish();

                    }
                });
            }

        }

        public static void sendEvent(ReactContext reactContext, String eventName, WritableMap params) {
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
        }

        public static void sendEvent(ReactContext reactContext, String eventName, String params) {
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
        }

    }

    /**
     * 初始化分享弹窗
     */
    private void initShareDialog() {
        instance = WeChatShareUtil.getInstance(this, appId);
        View dialogBinding = LayoutInflater.from(this).inflate(R.layout.dialog_share, null, false);
        dialogBinding.findViewById(R.id.ll_share_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showWeChat(SendMessageToWX.Req.WXSceneSession);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        dialogBinding.findViewById(R.id.ll_share_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showWeChat(SendMessageToWX.Req.WXSceneTimeline);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog = new Dialog(this, R.style.dialog_base);
        dialog.setContentView(dialogBinding);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        dialog.setCanceledOnTouchOutside(false);
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

    }

    /**
     * 调起微信分享
     * SendMessageToWX.Req.WXSceneSession 分享给好友
     * SendMessageToWX.Req.WXSceneTimeline 分享到朋友圈
     *
     * @param scene int
     */
    private void showWeChat(final int scene) throws JSONException {
        if (instance.isWeiXinAppInstall(appId)) {
            if (RightMsg != null && RightMsg.getString("imgUrl") != null) {
                Glide.with(this)
                        .load(RightMsg.getString("imgUrl"))
                        .asBitmap()
                        .centerCrop()//拉伸截取中间部分显示
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                bitmap = resource;
                                Message message = new Message();
                                message.what = 500;
                                Bundle bundle = new Bundle();
                                bundle.putInt("scene", scene);
                                message.setData(bundle);
                                mHandler.sendMessage(message);
                            }
                        });
            }


        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg1) {
            if (msg1.what == 500) {
                dialog.cancel();
                try {
                    if (RightMsg != null && RightMsg.getString("title") != null && RightMsg.getString("desc") != null && RightMsg.getString("link") != null) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, 120, 150, true);
                        instance.shareUrl(RightMsg.getString("link"), RightMsg.getString("title"), bitmap, RightMsg.getString("desc"), msg1.getData().getInt("scene"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    /**
     * 获得版本号信息
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String result = "";
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String verson = info.versionName;
            if (verson.length() > 0 && verson.contains("(")) {
                result = verson.substring(0, verson.indexOf("("));
            } else {
                result = verson;
            }
        } catch (PackageManager.NameNotFoundException e) {
            result = info.versionName;
        }
        return result;
    }

    private  void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
