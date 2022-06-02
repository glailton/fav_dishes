package io.github.glailton.favdish.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.github.glailton.favdish.R
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.databinding.ItemDishLayoutBinding
import io.github.glailton.favdish.ui.dish.add.AddUpdateDishActivity
import io.github.glailton.favdish.ui.dish.favorite.FavoriteDishesFragment
import io.github.glailton.favdish.ui.dish.list.DishesListFragment
import io.github.glailton.favdish.ui.extensions.loadImage
import io.github.glailton.favdish.ui.utils.Constants
import timber.log.Timber

class FavDishesAdapter(private val fragment: Fragment): RecyclerView.Adapter<FavDishesAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding
            .inflate(LayoutInflater.from(fragment.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]

        holder.ivDishImage.loadImage(dish.image)

        holder.tvTitle.text = dish.title

        holder.itemView.setOnClickListener {
            if (fragment is DishesListFragment){
                fragment.dishDetails(dish)
            } else if(fragment is FavoriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }

        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_edit_dish -> {
                        val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                        intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                        fragment.requireActivity().startActivity(intent)
                    }
                    R.id.action_delete_dish -> {
                        if (fragment is DishesListFragment) {
                            fragment.deleteDish(dish)
                            Timber.d("delete")
                        }
                    }
                }
                true
            }

            popup.show()
        }
    }

    override fun getItemCount() = dishes.size

    fun setDishesList(dishes: List<FavDish>) {
        this.dishes = dishes
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
    }

}