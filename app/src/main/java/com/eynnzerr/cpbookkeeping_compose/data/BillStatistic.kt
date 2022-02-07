package com.eynnzerr.cpbookkeeping_compose.data

import androidx.room.ColumnInfo

data class BillStatistic(
    @ColumnInfo(name = "mIndex")
    var mIndex: Int = 0,
    @ColumnInfo(name = "mValue")
    var mValue: Float = 0f
) {
    override fun toString(): String {
        return "BillStatistic(mIndex=$mIndex, mValue=$mValue)"
    }
}
