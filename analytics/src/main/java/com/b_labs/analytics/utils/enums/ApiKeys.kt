package com.b_labs.analytics.utils.enums

/**
 * ApiKeys is an enum class which contains the list of
 * all the api keys supported by the library.
 * Api keys are stored in the gradle.properties file and
 * accessed using meta-data in the manifest file of the app module
 **/
enum class ApiKeys(val key: String) {
    AMPLITUDE_API_KEY("com.blabs.analytics.AmplitudeApiKey"),
    WEB_ENGAGE_KEY("com.blabs.analytics.WebEngageKey"),
}
