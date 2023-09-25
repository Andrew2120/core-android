package com.b_labs.analytics.analytics_tools.web_engage

import android.app.Application
import android.content.Context
import com.b_labs.analytics.interfaces.AnalyticsTracker
import com.b_labs.analytics.utils.enums.AnalyticsTool
import com.b_labs.analytics.utils.enums.ApiKeys
import com.b_labs.analytics.utils.getValueFromMetaData
import com.google.firebase.BuildConfig
import com.webengage.sdk.android.Analytics
import com.webengage.sdk.android.LocationTrackingStrategy
import com.webengage.sdk.android.WebEngage
import com.webengage.sdk.android.WebEngageActivityLifeCycleCallbacks
import com.webengage.sdk.android.WebEngageConfig

class WebEngageAnalyticsTracker : AnalyticsTracker {

    private var webEngageAnalytics: Analytics? = null
    override fun initialize(context: Context) {
        context.getValueFromMetaData(ApiKeys.WEB_ENGAGE_KEY)?.let {
            val webEngage =
                WebEngageConfig.Builder().setWebEngageKey(it).setDebugMode(BuildConfig.DEBUG)
                    .setLocationTrackingStrategy(LocationTrackingStrategy.DISABLED).build()
            (context as Application).registerActivityLifecycleCallbacks(
                WebEngageActivityLifeCycleCallbacks(context, webEngage),
            )
            webEngageAnalytics = WebEngage.get().analytics()
        } ?: run {
            throw Exception("Web Engage API key not found in manifest file")
        }
    }

    override fun trackEvent(eventName: String, parameters: Map<String, Any>?) {
        val map: MutableMap<String, Any> = HashMap()
        parameters?.forEach { (key, value) ->
            map[key] = value
        }
        webEngageAnalytics?.track(eventName, map)
    }

    override fun trackScreen(eventName: String, parameters: Map<String, Any>?) {
        val map: MutableMap<String, Any> = HashMap()
        parameters?.forEach { (key, value) ->
            map[key] = value
        }
        webEngageAnalytics?.screenNavigated(eventName, map)
    }

    override fun getAnalyticsTool() = AnalyticsTool.WEB_ENGAGE
}
