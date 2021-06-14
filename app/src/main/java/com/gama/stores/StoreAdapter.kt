package com.gama.stores

import android.content.Context
import android.net.sip.SipSession
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.gama.stores.databinding.ItemStoreBinding

class StoreAdapter(private var stores : MutableList<Store>, private var listener : OnClickListener):
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext : Context
    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view)
        fun setListener(store:Store){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = stores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores.get(position)
        with(holder){
            binding.tvName.text = store.name
        }
    }

    fun add(store: Store) {
        //Añadir el dato store
        stores.add(store)
        //refrescar el dato en pantalla
        notifyDataSetChanged()
    }
}