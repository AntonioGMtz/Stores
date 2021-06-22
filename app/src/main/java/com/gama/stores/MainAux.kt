package com.gama.stores
//Interface que comunica la MainActivity con el Fragement
interface MainAux {
    fun hideFab(isVisible : Boolean = false)

    fun addStore(storeEntity: StoreEntity)

    fun updateStore(storeEntity: StoreEntity)
}