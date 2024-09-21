package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.utils.FilterAsteroid


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(requireActivity().application) }

    private val asteroidAdapter = MainAdapter(MainAdapter.AsteroidListener { asteroid ->
        viewModel.onAsteroidClicked(asteroid)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.asteroidRecycler.adapter = asteroidAdapter

        viewModel.pictureOfDay.observe(viewLifecycleOwner) { picture ->
            picture?.let {
                Picasso.get()
                    .load(it.url)
                    .into(binding.activityMainImageOfTheDay)
            }
        }

        viewModel.navigateToDetailAsteroid.observe(viewLifecycleOwner) { asteroid ->
            if (asteroid != null) {
                this.findNavController()
                    .navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(asteroid))
                viewModel.onAsteroidNavigated()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.asteroidList.observe(viewLifecycleOwner) { pagingData ->
            asteroidAdapter.submitData(lifecycle, pagingData)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.setFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> FilterAsteroid.PRESENT_DAY
                R.id.show_all_menu -> FilterAsteroid.WEEK
                else -> FilterAsteroid.ALL
            }
        )
        return true
    }


}
