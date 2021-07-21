package com.gama.stores

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class StoreApplication : Application() {
    //Patron singlenton pars acceder desde cualquier pate de la app a la BDD
    companion object{
        lateinit var database: StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()

        //Migracion de BDD
        var MIGRACION_1_2 = object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE StoreEntity ADD COLUMN photoUrl TEXT NOT NULL DEFAULT ''")
                //En donde
                //*ALTER TABLE : es el nombre de la tabla
                //*ADD COLUMN es el nombfee de la columna nueva agregada
                //*TEXT NOT NULL DEFAULT : representa lo qu va a llenar los campos vacios que ya estaban dentro de la tabla

            }
        }




        //Creacion de un RoomDatabse de una databse persistin
        database = Room.databaseBuilder(this,
        StoreDatabase::class.java,
        "StoreDatabase")
                .addMigrations(MIGRACION_1_2) //Añadade las migracion o cambios hecho en el room
            .build()
    }
}