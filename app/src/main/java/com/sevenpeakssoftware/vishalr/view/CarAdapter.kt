package com.sevenpeakssoftware.vishalr.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sevenpeakssoftware.vishalr.R
import com.sevenpeakssoftware.vishalr.databinding.CarCardItemBinding
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.utils.Constant.BASE_URL
import com.squareup.picasso.Picasso

class CarAdapter(private val cars: List<CarUiModel>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val v = CarCardItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarViewHolder(v)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bindItem(cars[position])
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    inner class CarViewHolder(private val itemBinding: CarCardItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(carModel: CarUiModel) {
            itemBinding.txtCarTitle.text = carModel.title
            itemBinding.txtCarIngress.text = carModel.ingress
            itemBinding.txtCarDate.text = carModel.dateTime
            Picasso.get()
                .load(BASE_URL.plus("/").plus(carModel.image))
                .fit()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.img_not_found)
                .into(itemBinding.imgCar)
        }
    }
}