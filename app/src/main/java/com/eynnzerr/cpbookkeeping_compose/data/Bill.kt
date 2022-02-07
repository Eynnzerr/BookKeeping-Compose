package com.eynnzerr.cpbookkeeping_compose.data

import android.icu.util.Calendar
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.util.*

/**
 * id: primary key in database
 * type: detailed type of the bill, which is a drawable res id.
 * amount: amount of money
 * remark: more information
 * date: time when this bill is created
 * category: expenses or revenue
 */
@Parcelize
@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var type: Int = 0,
    var amount: Float = 0f,
    var remark: String = "",
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var date: String = "",
    var category: Int = 0): Parcelable {
    //quickly create a bill
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        type: Int,
        amount: Float,
        remark: String,
        category: Int): this() {
        this.type = type
        this.amount = amount
        this.remark = remark
        this.category = category
        with(Calendar.getInstance()) {
            day = get(java.util.Calendar.DATE)
            month = get(java.util.Calendar.MONTH) + 1
            year = get(java.util.Calendar.YEAR)
        }
        date = LocalDate.now().toString()
    }
}