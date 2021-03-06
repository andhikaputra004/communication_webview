package co.i.communication_webview

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        my_web_view.settings.javaScriptEnabled = true
        my_web_view.addJavascriptInterface(JavaScriptInterface(), JAVASCRIPT_OBJ)
        my_web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (url == BASE_URL) {
                    injectJavaScriptFunction()
                }
            }
        }
        my_web_view.loadUrl(BASE_URL)

        btn_send_to_web.setOnClickListener {
            my_web_view.evaluateJavascript("javascript: " +
                    "updateFromAndroid(\"" + edit_text_to_web.text + "\")", null)
        }
    }

    override fun onDestroy() {
        my_web_view.removeJavascriptInterface(JAVASCRIPT_OBJ)
        super.onDestroy()
    }

    private fun injectJavaScriptFunction() {
        my_web_view.loadUrl("javascript: " +
                "window.androidObj.textToAndroid = function(message) { " +
                JAVASCRIPT_OBJ + ".textFromWeb(message) }")
    }


    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun textFromWeb(fromWeb: String) {
            txt_from_web.text = fromWeb
        }
    }

    companion object {
        private val JAVASCRIPT_OBJ = "javascript_obj"
        private val BASE_URL = "file:///android_asset/webview.html"
    }
}
