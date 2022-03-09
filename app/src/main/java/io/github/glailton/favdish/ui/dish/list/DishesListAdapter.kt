package io.github.glailton.favdish.ui.dish.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.databinding.ItemCustomListBinding
import io.github.glailton.favdish.databinding.ItemDishLayoutBinding
import io.github.glailton.favdish.ui.adapters.CustomListItemAdapter

class DishesListAdapter(private val fragment: Fragment): RecyclerView.Adapter<DishesListAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding
            .inflate(LayoutInflater.from(fragment.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]

        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
    }

    override fun getItemCount() = dishes.size

    fun setDishesList(dishes: List<FavDish>) {
        this.dishes = dishes
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
    }

}