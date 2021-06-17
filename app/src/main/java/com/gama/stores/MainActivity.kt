package com.gama.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gama.stores.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//Implementar interfaz y metodos
class MainActivity : AppCompatActivity(), OnClickListener {
    //varabiles locales
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter : StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //Agregar una nueva tienda
        mBinding.btnSave.setOnClickListener{
            var store = StoreEntity(name = mBinding.etName.text.toString().trim()) //Variable para guardar los datos

            //Llamada a los datos de la data class
            Thread{  //HILO PARA QUE NO HAGA UN CHOQUE CON LOS PROCESOS
                StoreApplication.database.storeDao().addStore(store) //AÑADE LOS DATOS A LA BDD

            }.start()
            mAdapter.add(store)  //Lllebar los datos en el recyclerview
        }
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,2)
        setStores()
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter

        }
    }

    override fun onClick(storeEntity: StoreEntity) {

    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        //Cambiar el valor a la tienda
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            //Llamada a la base de datos
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                mAdapter.update(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        doAsync {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                mAdapter.delete(storeEntity)
            }
        }
    }

    private fun setStores(){
        doAsync {
            //Ejecutar en otro hilo
            val stores = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                //Cuando este lista lo hace
                mAdapter.setStore(stores)
            }
        }


    }
}