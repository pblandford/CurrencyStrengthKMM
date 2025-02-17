package com.philblandford.currencystrength.common.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<RoomDatabaseManager.AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<RoomDatabaseManager.AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}