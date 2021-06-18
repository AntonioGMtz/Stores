package com.gama.stores

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.gama.stores.databinding.FragmentEditStoreBinding

class EditStoreFragment : Fragment() {
    //Creamos la val para instanciar el bindeon con MBINFIND
    private lateinit var mBinding : FragmentEditStoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //Bindeamos el fragment para presentarlo en patalla
        mBinding = FragmentEditStoreBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Hacer un casteo del MAIN ACTIVITY
        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.supportActionBar?.title = getString(R.string.edit_store_tittle_add)

        //Tenga accseo al menus
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Inflamos la vista del menu para verla en pantalla
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}