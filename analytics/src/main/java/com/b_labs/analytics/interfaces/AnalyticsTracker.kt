package com.b_labs.analytics.interfaces

import android.content.Context
import com.b_labs.analytics.utils.enums.AnalyticsTool

/**
 * AnalyticsTracker is an interface which contains the list of
 * all the functions that an analytics tracker should implement.
 */
interface AnalyticsTracker {

    /**
     * Initialize the analytics tracker
     * @param context application context
     */
    fun initialize(context: Context)

    /**
     * Track event to the analytics tracker
     * @param eventName name of the event
     * @param parameters parameters of the event
     */
    fun trackEvent(eventName: String, parameters: Map<String, Any>?)

    /**
     * Track screen to the analytics tracker
     * @param eventName name of the screen or screen_view event
     * @param parameters parameters of the screen
     */
    fun trackScreen(eventName: String, parameters: Map<String, Any>?)

    /**
     * Get the analytics tool
     * return analytics tool
     * @see AnalyticsTool
     */
    fun getAnalyticsTool(): AnalyticsTool
}
