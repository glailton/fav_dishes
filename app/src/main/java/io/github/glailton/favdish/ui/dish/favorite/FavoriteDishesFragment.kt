package io.github.glailton.favdish.ui.dish.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.databinding.FragmentFavoriteDishesBinding
import io.github.glailton.favdish.ui.MainActivity
import io.github.glailton.favdish.ui.adapters.FavDishesAdapter

@AndroidEntryPoint
class FavoriteDishesFragment : Fragment() {

    private val favoriteDishesViewModel: FavoriteDishesViewModel by viewModels()
    private var _binding: FragmentFavoriteDishesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavoriteDishesList.layoutManager = GridLayoutManager(requireContext(), 2)
        val favDishesAdapter = FavDishesAdapter(this)
        binding.rvFavoriteDishesList.adapter = favDishesAdapter

        favoriteDishesViewModel.allFavoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty()) {
                    binding.rvFavoriteDishesList.visibility = View.VISIBLE
                    binding.tvNoFavoriteDishesAvailable.visibility = View.GONE

                    favDishesAdapter.setDishesList(it)
                } else {
                    binding.rvFavoriteDishesList.visibility = View.GONE
                    binding.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
                favDishesAdapter.setDishesList(it)
            }
        }
    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            FavoriteDishesFragmentDirections
                .actionNavigationFavoriteDishesToDishDetailsFragment(favDish)
        )

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}