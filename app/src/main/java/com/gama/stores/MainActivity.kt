package com.gama.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

        mBinding.fab.setOnClickListener{
            launchEditFragment()
        }

        setupRecyclerView()

    }

    private fun launchEditFragment() { //SINTAXIS BASICA PARA INICIAR UN FRAGMENT
       val fragment = EditStoreFragment() //Instanciamos el Fragment
        val fragmentManager = supportFragmentManager //Gestor para controlar los fragmentos
        val fragmentTransaction = fragmentManager.beginTransaction() //Dcide como ejecutar el fragment
        fragmentTransaction.add(R.id.containerMain,fragment) //LLAMA AL AFRAGMENT
        fragmentTransaction.commit()//Inicia el fragment
        fragmentTransaction.addToBackStack(null)
        mBinding.fab.hide()

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