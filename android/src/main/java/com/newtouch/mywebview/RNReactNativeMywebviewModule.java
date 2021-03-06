
package com.newtouch.mywebview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.List;

public class RNReactNativeMywebviewModule extends ReactContextBaseJavaModule {

  public static ReactApplicationContext reactContext;
  private static final String ERROR_CODE = RNReactNativeMywebviewModule.class.getName();
  public RNReactNativeMywebviewModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNReactNativeMywebview";
  }


  @ReactMethod
  public void startAction(ReadableMap options) {
    String url = options.hasKey("url") ? options.getString("url") : "";
    String token = options.hasKey("token") ? options.getString("token") : "";
    String appid = options.hasKey("appid") ? options.getString("appid") : "";
    Intent intent = new Intent(getCurrentActivity(),WebViewActivity.class);
    intent.putExtra("url",url);
    intent.putExtra("token",token);
    intent.putExtra("appid",appid);
    getCurrentActivity().startActivity(intent);
  }

  @ReactMethod
  public void destroyByWebview(ReadableMap options) {
    try {
      if (WebViewActivity.webViewActivity != null) {
        WebViewActivity.webViewActivity.finish();
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}