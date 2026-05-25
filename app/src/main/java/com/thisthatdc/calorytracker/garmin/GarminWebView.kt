package com.thisthatdc.calorytracker.garmin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val login =
    "https://sso.garmin.com/portal/sso/en-US/sign-in?clientId=GarminConnect&service=https%3A%2F%2Fconnect.garmin.com%2Fapp"

@SuppressLint("SetJavaScriptEnabled")
fun garminWebViewScreen(
    context: Context,
    d: Date = Date(),
    callback: (String) -> Unit,
    onLogin: () -> Unit
): WebView {
    val webView = WebView(context)
    val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(d)
    var cookie: String = ""
    var uuid: String = ""
    var token: String = ""

    webView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            Log.d("WebView", "Overrider loading: ${request.url}")
            return false
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            Log.d("WebView", "Error: $error")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            if (url?.endsWith("/app") == true || url?.endsWith("/app/home") == true) {
                cookie = CookieManager.getInstance().getCookie(url)
                view?.evaluateJavascript("(function() { return document.body.innerHTML.match(/\"displayName\":\"([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\"/)[1]; })();") { result ->
                    uuid = strip(result)
                    Log.d("WebViewJS", "UUID Result: -$uuid-")
                }
                view?.evaluateJavascript("(function() { return document.head.innerHTML.match(/<meta name=\"csrf-token\" content=\"([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\">/)[1]; })();") { result ->
                    token = strip(result)
                    Log.d("WebViewJS", "TOKEN Result: -$token-")
                    connect(cookie, uuid, token, day, callback)
                }
            } else {
                Log.d("WebView", "Need to login")
                onLogin()
            }
            Log.d("WebView", "UrL: $url")
        }
    }
    webView.settings.javaScriptEnabled = true;
    webView.settings.domStorageEnabled = true;
    webView.settings.loadWithOverviewMode = true;
    webView.loadUrl(login)
    return webView
}


fun strip(v: String): String {
    return v.substring(1, v.length - 1)
}

@OptIn(DelicateCoroutinesApi::class)
fun connect(cookie: String, uuid: String, token: String, day: String, callback: (String) -> Unit) {
    val client = OkHttpClient()
    val url =
        "https://connect.garmin.com/gc-api/usersummary-service/usersummary/daily/${uuid}?calendarDate=${day}"
    Log.d("WebView", "URL: $url")

    val request = Request.Builder()
        .url(url)
        .header("accept", "*/*")
        .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8,it;q=0.7")
        .header("connect-csrf-token", token) //"87df0fb7-3bfe-4528-b344-053cc22f1669")
        .header("dnt", "1")
        .header("priority", "u=1, i")
        .header(
            "sec-ch-ua",
            "\"Chromium\";v=\"148\", \"Google Chrome\";v=\"148\", \"Not/A)Brand\";v=\"99\""
        )
        .header("sec-ch-ua-mobile", "?0")
        .header("sec-ch-ua-platform", "\"Windows\"")
        .header("sec-fetch-dest", "empty")
        .header("sec-fetch-mode", "cors")
        .header("sec-fetch-site", "same-origin")
        .header(
            "user-agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36"
        )
        .header("cookie", cookie)
        .build()
    GlobalScope.launch {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.d("WebView", "Error in http")
                throw Error("Error in http")
            }
            val result = response.body.string()
            Log.d("WebView", "Result: $result")
            callback(result)
        }
    }

}