package com.gama.stores

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gama.stores.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreFragment : Fragment() {
    //Creamos la val para instanciar el bindeon con MBINFIND
    private lateinit var mBinding : FragmentEditStoreBinding
    //Creamos una variable global para el casteo de MAINactivity
    private var mActivity:MainActivity? = null

    private var mstoreEntity : StoreEntity? = null

    //Variable para comprobar que el ID este lleno
    private var misEditMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //Bindeamos el fragment para presentarlo en patalla
        mBinding = FragmentEditStoreBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(getString(R.string.arg_id),0)
        if(id != null && id != 0L){
            misEditMode = true
            getStore(id)
        }else{
            misEditMode = false
            mstoreEntity = StoreEntity(name = "", phone = "", photoUrl = "")
        }

        setupActionBar()
        setUpTextFields()
    }
    private fun setupActionBar(){

        //Hacer un casteo del MAIN ACTIVITY
        mActivity = activity as? MainActivity?
        //Muestra la barra en la parte de arriba   <-
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Muestra el titulo que lleva la barra en la parte de arriba
        mActivity?.supportActionBar?.title = if(misEditMode) getString(R.string.edit_store_tittle_add)
                                             else getString(R.string.edit_store_tittle_visualizar)

        //Tenga accseo al menus
        setHasOptionsMenu(true)
    }

    private fun setUpTextFields() {
        //Cargamos la imagen con ayuda de la libreria GLIDE
        with(mBinding){
            //Revisar en tiempo real los TEXTFIELDS
            etName.addTextChangedListener{ validateFields(tilName) }
            etPhone.addTextChangedListener{ validateFields(tilPhone) }
            etPhotoUrl.addTextChangedListener{
                validateFields(tilPhotoUrl)
                loadImage(it.toString().trim())
            }
        }
    }
    private fun loadImage(url : String){
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhoto)
    }

    //funcion para llenar los campos con los datos de BDD traidos por el id
    private fun getStore(id: Long) {
        doAsync {
            mstoreEntity = StoreApplication.database.storeDao().getStoreById(id) //Creamos la consulta
            uiThread {
                if(mstoreEntity != null) setUiStore(mstoreEntity!!)
            }
        }
    }

    //funcion que trae la tienda y escribe los valores en ella
    private fun setUiStore(storeEntity: StoreEntity) {
        with(mBinding){
            etName.text = storeEntity.name.editable()
            etPhone.text = storeEntity.phone.editable()
            etPhotoUrl.text = storeEntity.photoUrl.editable()
            etWebSite.text = storeEntity.websiste.editable()
          /*  Glide.with(activity!!)
                .load(storeEntity.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgPhoto)*/
        }
    }
    //Funcion EXTENSION para poder pasar Strings en campos de texto
    private fun String.editable() : Editable = Editable.Factory.getInstance().newEditable(this)


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
                if(mstoreEntity != null && validateFields(mBinding.tilName,mBinding.tilPhone,mBinding.tilPhotoUrl)){
                    /* val store = StoreEntity(name = mBinding.etName.text.toString().trim(),
               phone = mBinding.etPhone.text.toString().trim(),
               websiste = mBinding.etWebSite.text.toString().trim(),
               photoUrl = mBinding.etPhotoUrl.text.toString().trim())*/
                    with(mstoreEntity!!){
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        websiste = mBinding.etWebSite.text.toString().trim()
                        photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                    }
                    doAsync {

                        if(misEditMode) StoreApplication.database.storeDao().updateStore(mstoreEntity!!)
                        else mstoreEntity!!.id = StoreApplication.database.storeDao().addStore(mstoreEntity!!)
                        uiThread {

                            hideKeyboard()

                            if(misEditMode){
                               mActivity?.updateStore(mstoreEntity!!)
                               Snackbar.make(mBinding.root,
                               R.string.edit_store_message_update_store,
                               Snackbar.LENGTH_LONG).show()

                           }
                            else{
                               mActivity?.addStore(mstoreEntity!!)
                              //hideKeyboard()
                               //Creacion de mensaje en patanlla como el TOAST
                               /* Snackbar.make(mBinding.root,
                                        getString(R.string.edit_store_message_save_succes),
                                        Snackbar.LENGTH_LONG).show()*/
                               Toast.makeText(mActivity,R.string.edit_store_message_save_succes,Toast.LENGTH_LONG).show()
                               mActivity?.onBackPressed()       //Castea la MainActivity y el metodo ObackPresed retorna a ella

                           }
                           }
                    }

                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Validando multiples TEXFIELDS a traves de varios argumentos
    private fun validateFields(vararg textFields : TextInputLayout) : Boolean{
        var isValid = true
        for(textField in textFields){
            if(textField.editText?.text.toString().trim().isEmpty()){
                textField.error = getString(R.string.helper_required)
                isValid = false
            }else textField.error = null
        }
        if(!isValid) Snackbar.make(mBinding.root,
            R.string.edit_store_message_valid,
            Snackbar.LENGTH_SHORT).show()
        return isValid

    }
  /*  private fun validateFields(): Boolean {
        var isValid = true

        if(mBinding.etName.text.toString().trim().isEmpty()){
            mBinding.tilName.error = getString(R.string.helper_required)
            mBinding.etName.requestFocus()
            isValid = false
        }
        if(mBinding.etPhone.text.toString().trim().isEmpty()){
            mBinding.tilPhone.error = getString(R.string.helper_required)
            mBinding.etPhone.requestFocus()
            isValid = false
        }
        if(mBinding.etPhotoUrl.text.toString().trim().isEmpty()){
            mBinding.tilPhotoUrl.error  = getString(R.string.helper_required)
            mBinding.etPhotoUrl.requestFocus()
            isValid = false
        }
        return isValid

    }*/

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