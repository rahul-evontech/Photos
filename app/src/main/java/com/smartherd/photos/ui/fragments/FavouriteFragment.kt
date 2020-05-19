package com.smartherd.photos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smartherd.photos.R
import com.smartherd.photos.adapter.PhotosAdapter
import com.smartherd.photos.ui.FragmentViewModel
import com.smartherd.photos.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    lateinit var viewModel: FragmentViewModel
    lateinit var photosAdapter: PhotosAdapter

    val TAG = "favorite"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setRecyclerView()

        viewModel.getAllPhotos().observe(viewLifecycleOwner, Observer {
            photosAdapter.differ.submitList(it)
        })

        photosAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("photosHit",it)
            }
            findNavController().navigate(
                R.id.action_favouriteFragment_to_photosFragment,
                bundle
            )
        }

    }

    private fun setRecyclerView(){
        photosAdapter = PhotosAdapter()
        Log.e(TAG, "recyclerview")
        favouriteRV.apply {
            adapter = photosAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            Log.e(TAG,"Layout inflated")

        }
    }

}
