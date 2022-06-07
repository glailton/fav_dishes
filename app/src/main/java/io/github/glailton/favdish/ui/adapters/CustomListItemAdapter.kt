package io.github.glailton.favdish.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.github.glailton.favdish.databinding.ItemCustomListBinding
import io.github.glailton.favdish.ui.dish.add.AddUpdateDishActivity
import io.github.glailton.favdish.ui.dish.list.DishesListFragment

class CustomListItemAdapter(private val activity: Activity,
                            private val fragment: Fragment?,
                            private val listItems : List<String>,
                            private val selection: String):
    RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(activity), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]

        holder.bind(item)

        holder.itemView.setOnClickListener{
            if (activity is AddUpdateDishActivity){
                activity.selectedListItem(item, selection)
            }

            if (fragment is DishesListFragment){
                fragment.filterSelection(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(view: ItemCustomListBinding): RecyclerView.ViewHolder(view.root) {
        private val tvText = view.tvText

        fun bind(item: String) {
            tvText.text = item
        }

    }
}