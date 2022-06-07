package io.github.glailton.favdish.ui.dish.list

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.favdish.R
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.databinding.DialogCustomListBinding
import io.github.glailton.favdish.databinding.FragmentDishesListBinding
import io.github.glailton.favdish.ui.MainActivity
import io.github.glailton.favdish.ui.adapters.CustomListItemAdapter
import io.github.glailton.favdish.ui.adapters.FavDishesAdapter
import io.github.glailton.favdish.ui.utils.Constants
import timber.log.Timber

@AndroidEntryPoint
class DishesListFragment : Fragment() {

    private val dishesListViewModel: DishesListViewModel by viewModels()
    private var _binding: FragmentDishesListBinding? = null

    private val binding get() = _binding!!

    private lateinit var favDishesAdapter: FavDishesAdapter
    private lateinit var customListDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDishesListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDishesList.layoutManager = GridLayoutManager(requireContext(), 2)
        favDishesAdapter = FavDishesAdapter(this)

        binding.rvDishesList.adapter = favDishesAdapter

        dishesListViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            setDishesList(dishes)
        }
    }

    fun dishDetails(favDish: FavDish){
        findNavController().navigate(DishesListFragmentDirections.actionNavigationDishesListToDishDetailsFragment(favDish))

        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    fun deleteDish(favDish: FavDish){
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_delete_dish))
            .setMessage(getString(R.string.msg_delete_dish_dialog, favDish.title))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(getString(R.string.lbl_yes)){ dialogInterface, _ ->
                dishesListViewModel.delete(favDish)
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.lbl_no)){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesDialog(){
        customListDialog = Dialog(requireContext())
        val bindingDialog: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        customListDialog.setContentView(bindingDialog.root)
        bindingDialog.tvTitle.text = getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)

        bindingDialog.rvList.layoutManager = LinearLayoutManager(requireContext())

        val adapter = CustomListItemAdapter(requireActivity(), this@DishesListFragment, dishTypes, Constants.FILTER_SELECTION)

        bindingDialog.rvList.adapter = adapter
        customListDialog.show()
    }

    fun filterSelection(filterItemSelection: String){
        customListDialog.dismiss()

        Timber.i("Filter Selection", filterItemSelection)

        when (filterItemSelection) {
            Constants.ALL_ITEMS -> {
                dishesListViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
                    setDishesList(dishes)
                }
            }
            else -> {
                Timber.i("Get Filter List", filterItemSelection)
                dishesListViewModel.getFilteredDishesList(filterItemSelection).observe(viewLifecycleOwner) { dishes ->
                    setDishesList(dishes)
                }
            }
        }
    }

    private fun setDishesList(dishes: List<FavDish>?) {
        dishes?.let {
            if (it.isNotEmpty()) {
                binding.rvDishesList.visibility = VISIBLE
                binding.tvNoDishesAddedYet.visibility = GONE

                it.let { it1 -> favDishesAdapter.setDishesList(it1) }
            } else {
                binding.rvDishesList.visibility = GONE
                binding.tvNoDishesAddedYet.visibility = VISIBLE
            }
            it.let { it1 -> favDishesAdapter.setDishesList(it1) }
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dishes_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish -> {
                val directions = DishesListFragmentDirections
                    .actionNavigationDishesListToAddUpdateDishActivity()
                findNavController().navigate(directions)
            }
            R.id.action_filter_dish -> {
                filterDishesDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}