package io.github.glailton.favdish.ui.dish.list

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
import io.github.glailton.favdish.databinding.FragmentDishesListBinding
import timber.log.Timber

@AndroidEntryPoint
class DishesListFragment : Fragment() {

    private val dishesListViewModel: DishesListViewModel by viewModels()
    private var _binding: FragmentDishesListBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
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
        val favDishesListAdapter = DishesListAdapter(this)
        binding.rvDishesList.adapter = favDishesListAdapter

        dishesListViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()){
                    binding.rvDishesList.visibility = VISIBLE
                    binding.tvNoDishesAddedYet.visibility = GONE

                    favDishesListAdapter.setDishesList(it)
                } else {
                    binding.rvDishesList.visibility = GONE
                    binding.tvNoDishesAddedYet.visibility = VISIBLE
                }
                favDishesListAdapter.setDishesList(it)
            }
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