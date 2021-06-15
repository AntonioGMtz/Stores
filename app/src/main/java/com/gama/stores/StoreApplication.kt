package com.gama.stores

import android.app.Application
import androidx.room.Room

class StoreApplication : Application() {
    //Patron singlenton pars acceder desde cualquier pate de la app a la BDD
    companion object{
        lateinit var database: StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()
        //Creacion de un RoomDatabse de una databse persistin
        database = Room.databaseBuilder(this,
        StoreDatabase::class.java,
        "StoreDatabase")
            .build()
    }
}