package com.gama.stores

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.gama.stores.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreFragment : Fragment() {
    //Creamos la val para instanciar el bindeon con MBINFIND
    private lateinit var mBinding : FragmentEditStoreBinding
    //Creamos una variable global para el casteo de MAINactivity
    private var mActivity:MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //Bindeamos el fragment para presentarlo en patalla
        mBinding = FragmentEditStoreBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Hacer un casteo del MAIN ACTIVITY
        mActivity = activity as? MainActivity?
        //Muestra la barra en la parte de arriba   <-
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Muestra el titulo que lleva la barra en la parte de arriba
        mActivity?.supportActionBar?.title = getString(R.string.edit_store_tittle_add)

        //Tenga accseo al menus
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Inflamos la vista del menu para verla en pantalla
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Se crea un when para el tratado de los eventos
        return when(item.itemId){
            //Opcion home del check
            android.R.id.home->{
                mActivity?.onBackPressed()
                true
            }
            //Opcion del guardado de datos
            R.id.action_save->{
                val store = StoreEntity(name = mBinding.etName.text.toString().trim(),
                phone = mBinding.etPhone.text.toString().trim(),
                websiste = mBinding.etWebSite.text.toString().trim())
                doAsync {
                    StoreApplication.database.storeDao().addStore(store)
                    uiThread {
                        hideKeyboard()
                        //Creacion de mensaje en patanlla como el TOAST
                        Snackbar.make(mBinding.root,
                                getString(R.string.edit_store_message_save_succes),
                                Snackbar.LENGTH_LONG).show()
                        mActivity?.onBackPressed()
                    }
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //METODO PARA OCULTAR EL TECLADO
    fun hideKeyboard(){
        //Constante que
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(view != null){
            imm.hideSoftInputFromWindow(view!!.windowToken,0)
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        //No muestra la barra en la parte de arriba  ( <- )
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //Muestra el titulo que lleva la barra en la parte de arriba
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mActivity?.hideFab(true)
        //Tenga accseo al menus
        setHasOptionsMenu(false)
        super.onDestroy()
    }

}