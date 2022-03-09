package io.github.glailton.favdish.ui.dish.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.glailton.favdish.databinding.FragmentFavoriteDishesBinding

class FavoriteDishesFragment : Fragment() {

    private lateinit var favoriteDishesViewModel: RandomDishViewModel
    private var _binding: FragmentFavoriteDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteDishesViewModel =
            ViewModelProvider(this).get(RandomDishViewModel::class.java)

        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}