package com.gama.stores

import androidx.room.Entity
import androidx.room.PrimaryKey


//Tratarla o convertila en ENTIDAD
@Entity(tableName = "StoreEntity")
//Primary key defina cual va a aser la llave primaria y que se va a generar de forma automatica
data class StoreEntity (@PrimaryKey(autoGenerate = true) var id : Long = 0,
                        var name : String,
                        var phone : String = "",
                        var websiste : String = "",
                        var photoUrl : String,
                        var isFavorite : Boolean=false)
