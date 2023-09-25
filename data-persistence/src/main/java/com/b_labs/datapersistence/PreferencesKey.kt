package com.b_labs.datapersistence

data class PreferencesKey(
    val type: Types,
    val name: String,
)

enum class Types {
    String, Int, Float, Long, Double, Boolean
}
