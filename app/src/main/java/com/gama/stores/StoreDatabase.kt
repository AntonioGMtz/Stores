package com.gama.stores

import androidx.room.Database
import androidx.room.RoomDatabase

//Definir que sera databse de ROOM
//Recibe como argunmentos la entidad (cllase STORE) Y el tipo de version
@Database(entities = arrayOf(StoreEntity::class),version = 1)
abstract class StoreDatabase : RoomDatabase() {
    //Configurando el dao
    abstract fun storeDao() : StoreDao
}