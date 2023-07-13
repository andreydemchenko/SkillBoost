#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "native-lib"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

jobject initializeWebView(JNIEnv* env, jobject context) {
    jclass webViewClass = env->FindClass("android/webkit/WebView");

    jmethodID webViewConstructor = env->GetMethodID(webViewClass, "<init>", "(Landroid/content/Context;)V");
    jobject webView = env->NewObject(webViewClass, webViewConstructor, context);

    return webView;
}

void configureWebViewSettings(JNIEnv* env, jobject webView) {
    jclass webViewClass = env->GetObjectClass(webView);
    jmethodID getSettingsMethod = env->GetMethodID(webViewClass, "getSettings", "()Landroid/webkit/WebSettings;");
    jobject webSettings = env->CallObjectMethod(webView, getSettingsMethod);

    jclass webSettingsClass = env->GetObjectClass(webSettings);
    jmethodID setJavaScriptEnabledMethod = env->GetMethodID(webSettingsClass, "setJavaScriptEnabled", "(Z)V");
    jmethodID setDomStorageEnabledMethod = env->GetMethodID(webSettingsClass, "setDomStorageEnabled", "(Z)V");
    jmethodID setDatabaseEnabledMethod = env->GetMethodID(webSettingsClass, "setDatabaseEnabled", "(Z)V");
    jmethodID setSafeBrowsingEnabledMethod = env->GetMethodID(webSettingsClass, "setSafeBrowsingEnabled", "(Z)V");
    jmethodID setCacheModeMethod = env->GetMethodID(webSettingsClass, "setCacheMode", "(I)V");

    env->CallVoidMethod(webSettings, setJavaScriptEnabledMethod, JNI_TRUE);
    env->CallVoidMethod(webSettings, setDomStorageEnabledMethod, JNI_TRUE);
    env->CallVoidMethod(webSettings, setDatabaseEnabledMethod, JNI_TRUE);
    env->CallVoidMethod(webSettings, setSafeBrowsingEnabledMethod, JNI_TRUE);
    jint loadDefaultConstant = -1; // For WebSettings.LOAD_DEFAULT
    env->CallVoidMethod(webSettings, setCacheModeMethod, loadDefaultConstant);
}

jobject initializeWebViewClient(JNIEnv* env) {
    jclass webViewClientClass = env->FindClass("com/dem/spoyersoccer/utils/CustomWebViewClient");

    jmethodID webViewClientConstructor = env->GetMethodID(webViewClientClass, "<init>", "()V");
    jobject webViewClient = env->NewObject(webViewClientClass, webViewClientConstructor);

    return webViewClient;
}

void makeWebViewGone(JNIEnv* env, jobject webView) {
    jclass viewClass = env->GetObjectClass(webView);
    jmethodID setVisibilityMethod = env->GetMethodID(viewClass, "setVisibility", "(I)V");
    jint invisibleConstant = 8; // For View.GONE
    env->CallVoidMethod(webView, setVisibilityMethod, invisibleConstant);
}

void makeWebViewVisible(JNIEnv* env, jobject webView) {
    jclass viewClass = env->GetObjectClass(webView);
    jmethodID setVisibilityMethod = env->GetMethodID(viewClass, "setVisibility", "(I)V");
    jint visibleConstant = 0; // For View.VISIBLE
    env->CallVoidMethod(webView, setVisibilityMethod, visibleConstant);
}

void assignWebViewClientToWebView(JNIEnv* env, jobject webView, jobject webViewClient) {
    jclass webViewClass = env->GetObjectClass(webView);
    jmethodID setWebViewClientMethod = env->GetMethodID(webViewClass, "setWebViewClient", "(Landroid/webkit/WebViewClient;)V");

    env->CallVoidMethod(webView, setWebViewClientMethod, webViewClient);
}

void loadUrl(JNIEnv* env, jobject webView, const char* urlStr) {
    jclass webViewClass = env->GetObjectClass(webView);
    jmethodID loadUrlMethod = env->GetMethodID(webViewClass, "loadUrl", "(Ljava/lang/String;)V");
    jstring urlJString = env->NewStringUTF(urlStr);
    env->CallVoidMethod(webView, loadUrlMethod, urlJString);
    env->DeleteLocalRef(urlJString);
}

jobject getRootView(JNIEnv* env, jobject instance) {
    jclass clazz = env->GetObjectClass(instance);
    jmethodID getRootViewMethod = env->GetMethodID(clazz, "getRootView", "()Landroid/view/View;");
    jobject rootView = env->CallObjectMethod(instance, getRootViewMethod);
    return rootView;
}

void addWebViewToViewGroup(JNIEnv* env, jobject rootView, jobject webView) {
    jclass viewGroupClass = env->GetObjectClass(rootView);
    jmethodID addViewMethod = env->GetMethodID(viewGroupClass, "addView", "(Landroid/view/View;)V");
    env->CallVoidMethod(rootView, addViewMethod, webView);
}

void setAcceptThirdPartyCookies(JNIEnv* env, jobject webView) {
    jclass cookieManagerClass = env->FindClass("android/webkit/CookieManager");
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }
    jmethodID getInstanceMethod = env->GetStaticMethodID(cookieManagerClass, "getInstance", "()Landroid/webkit/CookieManager;");
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }
    jobject cookieManager = env->CallStaticObjectMethod(cookieManagerClass, getInstanceMethod);
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }
    jmethodID setAcceptThirdPartyCookiesMethod = env->GetMethodID(cookieManagerClass, "setAcceptThirdPartyCookies", "(Landroid/webkit/WebView;Z)V");
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return;
    }
    env->CallVoidMethod(cookieManager, setAcceptThirdPartyCookiesMethod, webView, JNI_TRUE);
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
    }
}

std::string g_globalUrl;

bool compareUrls(const char* url1, const char* url2) {
    std::string cleanUrl1(url1);
    std::string cleanUrl2(url2);

    if (cleanUrl1.back() != '/') {
        cleanUrl1.push_back('/');
    }

    size_t wwwPos = cleanUrl1.find("www.");
    if (wwwPos != std::string::npos) {
        cleanUrl1.erase(wwwPos, 4);
    }

    wwwPos = cleanUrl2.find("www.");
    if (wwwPos != std::string::npos) {
        cleanUrl2.erase(wwwPos, 4);
    }

    int result = strcmp(cleanUrl1.c_str(), cleanUrl2.c_str());
    return (result == 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dem_spoyersoccer_MainActivity_passUrlToNative(JNIEnv *env, jobject context, jstring url) {
    const char *urlStr = env->GetStringUTFChars(url, nullptr);
    LOGD("Received URL: %s", urlStr);

    g_globalUrl = urlStr;

    LOGD("Initializing WebView");
    jobject webView = initializeWebView(env, context);
    if (webView == nullptr) {
        LOGE("Failed to initialize WebView");
        return;
    }

    configureWebViewSettings(env, webView);
    setAcceptThirdPartyCookies(env, webView);

    makeWebViewGone(env, webView);

    LOGD("Initializing WebViewClient");
    jobject webViewClient = initializeWebViewClient(env);
    if (webViewClient == nullptr) {
        LOGE("Failed to initialize WebViewClient");
        return;
    }

    LOGD("Assigning WebViewClient to WebView");
    assignWebViewClientToWebView(env, webView, webViewClient);

    LOGD("Loading URL");
    loadUrl(env, webView, urlStr);

    LOGD("Getting the root view of the activity");
    jobject rootView = getRootView(env, context);

    LOGD("Adding the WebView to the root view");
    addWebViewToViewGroup(env, rootView, webView);


    LOGD("Releasing local references");
    env->ReleaseStringUTFChars(url, urlStr);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_dem_spoyersoccer_utils_CustomWebViewClient_onPageFinishedNative(JNIEnv *env, jobject instance, jobject view, jstring url) {
    const char *urlStr = env->GetStringUTFChars(url, nullptr);

    bool urlsEqual = compareUrls(g_globalUrl.c_str(), urlStr);

    if (!urlsEqual) {
        makeWebViewVisible(env, view);
    }
    setAcceptThirdPartyCookies(env, view);

    env->ReleaseStringUTFChars(url, urlStr);
}
