package com.b_labs.analytics.manager

import android.content.Context
import com.b_labs.analytics.interfaces.AnalyticsManager
import com.b_labs.analytics.interfaces.AnalyticsTracker
import com.b_labs.analytics.utils.enums.AnalyticsTool

/**
 * AnalyticsManagerImpl is the implementation of AnalyticsManager interface.
 * It is responsible for initializing all the analytics trackers
 * and delegating the tracking calls to them.
 **/
class AnalyticsManagerImpl constructor(
    private val analyticsTrackers: Set<AnalyticsTracker>,
    private val appContext: Context,
) : AnalyticsManager {

    init {
        analyticsTrackers.forEach { tracker ->
            tracker.initialize(appContext)
        }
    }

    /**
     * Track event to all the analytics trackers
     * @param eventName name of the event
     * @param parameters parameters of the event
     * @param tools analytics tools to which the event should be tracked
     * If no tools are passed, the event will be tracked to all the analytics tools
     * If tools are passed, the event will be tracked to only those analytics tools
     * @see AnalyticsTool
     * @see AnalyticsTracker
     * @see AnalyticsManager
     */
    override fun trackEvent(
        eventName: String,
        parameters: Map<String, Any>?,
        vararg tools: AnalyticsTool,
    ) {
        analyticsTrackers.forEach { tracker ->
            if (tools.isEmpty() || tools.contains(tracker.getAnalyticsTool())) {
                tracker.trackEvent(eventName, parameters)
            }
        }
    }

    /**
     * Track screen to all the analytics trackers
     *  @param eventName name of the screen or screen_view event
     *  @param parameters parameters of the screen
     *  @param tools analytics tools to which the screen should be tracked
     * If no tools are passed, the screen will be tracked to all the analytics tools
     * If tools are passed, the screen will be tracked to only those analytics tools
     * @see AnalyticsTool
     * @see AnalyticsTracker
     * @see AnalyticsManager
     */
    override fun trackScreen(
        eventName: String,
        parameters: Map<String, Any>?,
        vararg tools: AnalyticsTool,
    ) {
        analyticsTrackers.forEach { tracker ->
            if (tools.isEmpty() || tools.contains(tracker.getAnalyticsTool())) {
                tracker.trackScreen(eventName, parameters)
            }
        }
    }
}
