package com.b_labs.datapersistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.b_labs.datapersistence.PreferencesConstants.BYTES_TO_STRING_SEPARATOR
import com.b_labs.datapersistence.PreferencesConstants.SECURITY_KEY_ALIES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * LocalPreferencesImpl is an implementation of the [LocalPreferences] interface that uses Android's DataStore
 * to store and retrieve key-value pairs in a local storage with added security features.
 *
 * @param [name] The name of the DataStore, typically used to create a unique namespace for preferences.
 * @param [context] The Android application context.
 *
 * @property security Initialize a SecurityUtil instance for data encryption/decryption.
 * @property json Initialize a JSON serializer for encoding/decoding data.
 * @property dataStore Store the DataStore instance in the class property.
 */
class LocalPreferencesImpl(
    name: String,
    context: Context,
) : LocalPreferences {
    private val security: SecurityUtil by lazy { SecurityUtil() }
    private val json = Json { encodeDefaults = true }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name)
    private val dataStore = context.dataStore

    /**
     * Retrieves data of type [T] associated with the given [key] from the DataStore as a Flow.
     *
     * @param key The key used to retrieve the data.
     * @return A Flow emitting the data associated with the key.
     */
    override fun <T> getData(
        key: PreferencesKey,
    ): Flow<T?> {
        val item = dataStore.data
            .map { preferences ->
                preferences[key.createPreferencesKey<T>(key.name)]
            }
        return item
    }

    /**
     * Sets data of type [T] associated with the given [key] in the DataStore.
     *
     * @param value The value to be stored.
     * @param key The key used to store the data.
     */
    override suspend fun <T> setData(
        value: T,
        key: PreferencesKey,
    ) {
        dataStore.edit {
            it[key.createPreferencesKey<T>(key.name)] = value
        }
    }

    /**
     * Retrieves encrypted data of type [String] associated with the given [key] from the DataStore as a Flow.
     *
     * @param key The key used to retrieve the encrypted data.
     * @return A Flow emitting the encrypted data associated with the key.
     */
    override fun getEncryptedData(
        key: Preferences.Key<String>,

    ) = dataStore.data
        .secureMap<String> { preferences ->
            preferences[key].orEmpty()
        }

    /**
     * Sets encrypted data of type [String] associated with the given [key] in the DataStore.
     *
     * @param value The encrypted value to be stored.
     * @param key The key used to store the encrypted data.
     */
    override suspend fun setEncryptedData(
        value: String,
        key: Preferences.Key<String>,
    ) {
        dataStore.secureEdit(value) { prefs, encryptedValue ->
            prefs[key] = encryptedValue
        }
    }

    /**
     * Clears all data in the DataStore.
     */
    override suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * Removes a preference associated with the given [key] from the DataStore.
     *
     * @param key The key used to remove the preference.
     */
    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    // Private helper function to create a Preferences.Key<T> based on the type of T.
    private fun <T> PreferencesKey.createPreferencesKey(key: String): Preferences.Key<T> {
        return when (this.type) {
            Types.String -> stringPreferencesKey(key) as Preferences.Key<T>
            Types.Int -> intPreferencesKey(key) as Preferences.Key<T>
            Types.Float -> doublePreferencesKey(key) as Preferences.Key<T>
            Types.Long -> booleanPreferencesKey(key) as Preferences.Key<T>
            Types.Double -> floatPreferencesKey(key) as Preferences.Key<T>
            Types.Boolean -> longPreferencesKey(key) as Preferences.Key<T>
        }
    }

    // Private inline function to securely map data in the DataStore using encryption.
    private inline fun <reified T> Flow<Preferences>.secureMap(crossinline fetchValue: (value: Preferences) -> String): Flow<T> {
        return map { preferences ->
            val decryptedValue = security.decryptData(
                SECURITY_KEY_ALIES,
                fetchValue(preferences).split(BYTES_TO_STRING_SEPARATOR).map { it.toByte() }
                    .toByteArray(),
            )
            json.decodeFromString(decryptedValue)
        }
    }

    // Private suspend inline function to securely edit data in the DataStore using encryption.
    private suspend inline fun <reified T> DataStore<Preferences>.secureEdit(
        value: T,
        crossinline editStore: (MutablePreferences, String) -> Unit,
    ) {
        edit {
            val encryptedValue =
                security.encryptData(SECURITY_KEY_ALIES, json.encodeToString(value))
            editStore.invoke(it, encryptedValue.joinToString(BYTES_TO_STRING_SEPARATOR))
        }
    }
}
