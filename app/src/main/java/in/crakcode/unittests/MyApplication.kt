package `in`.crakcode.unittests

import android.app.Application
import `in`.crakcode.unittests.db.RoomDbFactory

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RoomDbFactory.initRoomDb(this)
    }
}