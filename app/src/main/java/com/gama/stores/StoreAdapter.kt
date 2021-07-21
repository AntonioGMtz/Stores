package com.gama.stores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gama.stores.databinding.ItemStoreBinding

class StoreAdapter(private var stores : MutableList<StoreEntity>, private var listener : OnClickListener):
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext : Context

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view)   //Val que nos bindeamo en lugar de usar R.findviid

        fun setListener(storeEntity:StoreEntity){
            with(binding.root){  //Agrupa los binding en UNO solo
                setOnClickListener{  //Un clic nos lleva al detalle de la tienda
                    listener.onClick(storeEntity.id)
                }
                setOnLongClickListener{ //un clid largo nos eliminara la tienda
                    listener.onDeleteStore(storeEntity) //debemos regresar un booleano
                    true
                }
            }
            binding.cbFavorite.setOnClickListener{
                listener.onFavoriteStore(storeEntity)
            }

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
            //Bindeando los campos de la app
            setListener(store)
            binding.tvName.text = store.name
            binding.cbFavorite.isChecked=store.isFavorite
            //Se agrega la foto a la pantalla principal de recicler view
            Glide.with(mContext)
                    .load(store.photoUrl) //Carga la imagen desde la url de la BDD  de room
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(binding.imgPhoto)
        }
    }

    fun add(storeEntity: StoreEntity) {
        if(!stores.contains(storeEntity)){
            stores.add(storeEntity) //Añadir el dato store
            //refrescar el dato en pantalla
            notifyItemChanged(stores.size-1)
        }

    }

    fun setStore(stores: MutableList<StoreEntity>) {
        this.stores = stores
        notifyDataSetChanged()

    }
    //metodo para actualizar el estado de la tienda
    fun update(storeEntity: StoreEntity) {

        //saber el indice en el cual esta la tienda
        val index= stores.indexOf(storeEntity)
        if(index !=-1){
            stores.set(index,storeEntity)
            //solamente refresca el registro afectado
            notifyItemChanged(index)
        }
    }
    fun delete(storeEntity: StoreEntity) {

        //saber el indice en el cual esta la tienda
        val index= stores.indexOf(storeEntity)
        if(index !=-1){
            //Removemos la tienda
            stores.removeAt(index)
            //refresaca la vista con el elemento eliminado
            notifyItemRemoved(index)
        }
    }
}