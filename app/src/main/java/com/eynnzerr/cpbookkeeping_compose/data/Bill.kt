package com.eynnzerr.cpbookkeeping_compose.data

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

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
    var date: String = "",
    var category: Int = 0): Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(type: Int, amount: Float, remark: String, category: Int): this() {
        this.type = type
        this.amount = amount
        this.remark = remark
        this.category = category
        date = LocalDate.now().toString()
    }
}