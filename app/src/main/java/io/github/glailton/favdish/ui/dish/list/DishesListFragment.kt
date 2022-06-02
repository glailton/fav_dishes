package io.github.glailton.favdish.ui.dish.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.favdish.R
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.databinding.FragmentDishesListBinding
import io.github.glailton.favdish.ui.MainActivity
import io.github.glailton.favdish.ui.adapters.FavDishesAdapter

@AndroidEntryPoint
class DishesListFragment : Fragment() {

    private val dishesListViewModel: DishesListViewModel by viewModels()
    private var _binding: FragmentDishesListBinding? = null

    private val binding get() = _binding!!

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
        val favDishesAdapter = FavDishesAdapter(this)
        binding.rvDishesList.adapter = favDishesAdapter

        dishesListViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()){
                    binding.rvDishesList.visibility = VISIBLE
                    binding.tvNoDishesAddedYet.visibility = GONE

                    favDishesAdapter.setDishesList(it)
                } else {
                    binding.rvDishesList.visibility = GONE
                    binding.tvNoDishesAddedYet.visibility = VISIBLE
                }
                favDishesAdapter.setDishesList(it)
            }
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
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}