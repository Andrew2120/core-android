package com.b_labs.analytics.analytics_tools.firebase_analytics

import android.content.Context
import android.os.Bundle
import com.b_labs.analytics.interfaces.AnalyticsTracker
import com.b_labs.analytics.utils.enums.AnalyticsTool
import com.b_labs.analytics.utils.set
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsTracker(private val firebaseApp: FirebaseAnalytics) : AnalyticsTracker {

    private var firebaseAnalytics: FirebaseAnalytics? = null
    override fun initialize(context: Context) {
        firebaseAnalytics = firebaseApp
    }

    override fun trackEvent(eventName: String, parameters: Map<String, Any>?) {
        val bundle = Bundle()
        parameters?.forEach { (key, value) ->
            bundle[key] = value
        }
        firebaseAnalytics?.logEvent(eventName, bundle)
    }

    override fun trackScreen(eventName: String, parameters: Map<String, Any>?) {
        val bundle = Bundle()
        parameters?.forEach { (key, value) ->
            bundle[key] = value
        }
        firebaseAnalytics?.logEvent(eventName, bundle)
    }

    override fun getAnalyticsTool() = AnalyticsTool.FIREBASE_ANALYTICS
}
