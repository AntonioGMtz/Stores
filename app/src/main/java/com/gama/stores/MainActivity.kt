package com.gama.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gama.stores.databinding.ActivityMainBinding

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
            //Variable para guardar los datos
            //Llamada a los datos de la data class
            var store = Store(name = mBinding.etName.text.toString().trim())
            //Lllebar los datos en el recyclerview
            mAdapter.add(store)
        }
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,2)
        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter

        }
    }

    override fun onClick(store: Store) {

    }
}