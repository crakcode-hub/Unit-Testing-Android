package `in`.crakcode.unittests.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object RoomDbFactory {

    private lateinit var db: RoomDatabase

    fun initRoomDb(context: Context){
        db = Room.databaseBuilder(context,
            AppDatabase::class.java, "my-database")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

}