package com.gama.stores

import androidx.room.*


//Definir que el tipo va a ser DAO
@Dao
interface StoreDao {
    @Query("SELECT * FROM StoreEntity")
    fun getAllStores(): MutableList<StoreEntity>

    @Insert
    fun addStore(storeEntity: StoreEntity) : Long

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)

    @Query("SELECT * FROM StoreEntity where id = :id ")
    fun getStoreById(id:Long): StoreEntity
}