package com.b_labs.datapersistence

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/**
 * This interface responsible for representing a local preferences storage.
 */
interface LocalPreferences {
    /**
     * Retrieves data of type [T] associated with the given [key] as a Flow.
     *
     * @param key The unique identifier for the preference.
     * @return A Flow emitting the preference value or null if not found.
     */
    fun <T> getData(
        key:
        PreferencesKey,
    ): Flow<T?>

    /**
     * Sets the value of the preference associated with the given [key].
     *
     * @param value The value to be stored in the preference.
     * @param key The unique identifier for the preference.
     */
    suspend fun <T> setData(
        value: T,
        key: PreferencesKey,
    )

    /**
     * Retrieves encrypted data of type [String] associated with the given [key] as a Flow.
     *
     * @param key The unique identifier for the encrypted preference.
     * @return A Flow emitting the encrypted preference value or null if not found.
     */
    fun getEncryptedData(
        key: Preferences.Key<String>,
    ): Flow<String?>

    /**
     * Sets the value of the encrypted preference associated with the given [key].
     *
     * @param value The encrypted value to be stored in the preference.
     * @param key The unique identifier for the encrypted preference.
     */
    suspend fun setEncryptedData(
        value: String,
        key: Preferences.Key<String>,
    )

    /**
     * Clears all data stored in the preferences.
     */
    suspend fun clearDataStore()

    /**
     * Removes the preference associated with the given [key].
     *
     * @param key The unique identifier for the preference to be removed.
     */
    suspend fun <T> removePreference(
        key: Preferences.Key<T>,

    )
}
