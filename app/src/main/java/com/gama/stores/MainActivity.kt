package com.gama.stores

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.gama.stores.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

//Implementar interfaz y metodos
class MainActivity : AppCompatActivity(), OnClickListener, MainAux {
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

    private fun launchEditFragment(args : Bundle? = null) { //SINTAXIS BASICA PARA INICIAR UN FRAGMENT
       val fragment = EditStoreFragment() //Instanciamos el Fragment
        if(args != null) fragment.arguments = args
        val fragmentManager = supportFragmentManager //Gestor para controlar los fragmentos
        val fragmentTransaction = fragmentManager.beginTransaction() //Dcide como ejecutar el fragment
        fragmentTransaction.add(R.id.containerMain,fragment) //LLAMA AL AFRAGMENT
        fragmentTransaction.commit()//Inicia el fragment
        fragmentTransaction.addToBackStack(null)
        //Si es true muestra el boton flotante si no lo esconde
        hideFab()

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

    override fun onClick(storeId: Long) {
        val args  = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)
        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        //Cambiar el valor a la tienda
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            //Llamada a la base de datos
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                updateStore(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        //*ALERT de multiples opciones
       val items = resources.getStringArray(R.array.array_options_item)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items) { dialogInterface, i ->
                when (i) {
                    0 -> confirmDelete(storeEntity)

                    1 -> dial(storeEntity.phone)

                    2 -> goToWebSite(storeEntity.websiste)
                }
            }
            .show()

    }
    private fun confirmDelete(storeEntity: StoreEntity){
        //      **AlertDialog para confirmacion de elimancion**
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm) { dialogInterface, i ->
                doAsync {
                    StoreApplication.database.storeDao().deleteStore(storeEntity)
                    uiThread {
                        mAdapter.delete(storeEntity)
                    }
                }
            }
            .setNegativeButton(R.string.dialog_delete_cancel,null)
            .show()
    }
    private fun dial(phone : String){
        //*Ir a la patanlla de marcacion del telefono 
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }
        startIntent(callIntent)
    }

    private fun goToWebSite(website : String){
        if(website.isEmpty()){
            Toast.makeText(this,R.string.main_error_no_website, Toast.LENGTH_LONG).show()
        }else {
            //Abrir la patanalla del navegador en la ruta estabnlecida en la tienda previamente
            val webIntent = Intent().apply {
                action = Intent.ACTION_DIAL
                data = Uri.parse(website)
            }
            startIntent(webIntent)
        }
    }

    private fun startIntent(intent : Intent){
        if(intent.resolveActivity(packageManager) != null) startActivity(intent)
        else Toast.makeText(this, R.string.object_not_found,
            Toast.LENGTH_LONG).show()
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

    override fun hideFab(isVisible: Boolean) {
        //Si es true muestra el boton flotante si no lo esconde
        if(isVisible) mBinding.fab.show() else mBinding.fab.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }
}