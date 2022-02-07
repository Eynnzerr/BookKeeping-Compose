package com.eynnzerr.cpbookkeeping_compose.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.eynnzerr.cpbookkeeping_compose.base.CPApplication.Companion.context
import com.eynnzerr.cpbookkeeping_compose.model.HomeData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "home_data")

val MONTHLY_EXPENSE = floatPreferencesKey("monthly_expense")
val MONTHLY_REVENUE = floatPreferencesKey("monthly_revenue")
val BUDGET = floatPreferencesKey("budget")
val DAILY_EXPENSE = floatPreferencesKey("daily_expense")
val DAILY_REVENUE = floatPreferencesKey("daily_revenue")
val DAY_RECORD = intPreferencesKey("day_number")
val MONTH_RECORD = intPreferencesKey("month_number")

suspend fun updateIntData(new_value: Int, key: Preferences.Key<Int>) {
    context.dataStore.edit { home_data ->
        home_data[key] = new_value
    }
}

suspend fun getIntData(key: Preferences.Key<Int>, defaultValue: Int): Int = context.dataStore.data.map{ preferences ->
    preferences[key]?:defaultValue
}.first()

suspend fun updateFloatData(new_value: Float, key: Preferences.Key<Float>) {
    context.dataStore.edit { home_data ->
        home_data[key] = new_value
    }
}

suspend fun getFloatData(key: Preferences.Key<Float>, defaultValue: Float): Float = context.dataStore.data.map{ preferences ->
    preferences[key]?:defaultValue
}.first()

suspend fun getAllData(defaultValue: Float): HomeData = context.dataStore.data.map{ preferences ->
    HomeData(
        preferences[MONTHLY_EXPENSE]?:defaultValue,
        preferences[MONTHLY_REVENUE]?:defaultValue,
        preferences[DAILY_EXPENSE]?:defaultValue,
        preferences[DAILY_REVENUE]?:defaultValue,
        preferences[BUDGET]?:defaultValue
    )
}.first()