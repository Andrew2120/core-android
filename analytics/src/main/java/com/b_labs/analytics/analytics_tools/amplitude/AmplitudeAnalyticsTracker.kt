package com.b_labs.analytics.analytics_tools.amplitude

import android.app.Application
import android.content.Context
import com.amplitude.api.Amplitude
import com.amplitude.api.AmplitudeClient
import com.b_labs.analytics.interfaces.AnalyticsTracker
import com.b_labs.analytics.utils.enums.AnalyticsTool
import com.b_labs.analytics.utils.enums.ApiKeys
import com.b_labs.analytics.utils.getValueFromMetaData
import org.json.JSONObject

class AmplitudeAnalyticsTracker : AnalyticsTracker {

    private var amplitudeClient: AmplitudeClient? = null
    override fun initialize(context: Context) {
        context.getValueFromMetaData(ApiKeys.AMPLITUDE_API_KEY)?.let {
            amplitudeClient = Amplitude.getInstance().initialize(context, it)
                .enableForegroundTracking(context as Application)
        } ?: run {
            throw Exception("Amplitude API key not found in manifest file")
        }
    }

    override fun trackEvent(eventName: String, parameters: Map<String, Any>?) {
        val eventProperties = JSONObject()
        parameters?.forEach { (key, value) ->
            eventProperties.put(key, value)
        }
        amplitudeClient?.logEvent(eventName, eventProperties)
    }

    override fun trackScreen(eventName: String, parameters: Map<String, Any>?) {
        val eventProperties = JSONObject()
        parameters?.forEach { (key, value) ->
            eventProperties.put(key, value)
        }
        amplitudeClient?.logEvent(eventName, eventProperties)
    }

    override fun getAnalyticsTool() = AnalyticsTool.AMPLITUDE
}
