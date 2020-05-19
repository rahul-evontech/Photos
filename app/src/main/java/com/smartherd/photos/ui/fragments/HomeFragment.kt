package com.smartherd.photos.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smartherd.photos.R
import com.smartherd.photos.adapter.GridItemDecoration
import com.smartherd.photos.adapter.PhotosAdapter
import com.smartherd.photos.db.PhotoDatabase
import com.smartherd.photos.models.Hit
import com.smartherd.photos.network.Api
import com.smartherd.photos.repository.PhotosRepository
import com.smartherd.photos.ui.FragmentViewModel
import com.smartherd.photos.ui.FragmentViewModelFactory
import com.smartherd.photos.ui.MainActivity
import com.smartherd.photos.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.schedule

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel: FragmentViewModel
    lateinit var photosAdapter: PhotosAdapter

    var TAG = "hello"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        Log.e(TAG, "view created")

        setRecyclerView()


        photosAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("photosHit", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_photosFragment,
                bundle
            )
        }



        viewModel.photos.observe(viewLifecycleOwner, Observer { response ->
            Log.e(TAG, "obsrever")
            when (response) {
                is Resource.Success -> {
                    Log.e(TAG, "Inside success")
                    hideProgressbar()
                    response?.data.let { newResponse ->
                        Log.e(TAG, "success")
                        photosAdapter.differ.submitList(newResponse?.hits?.toList())
                        val totalPages = newResponse!!.total / 20 + 2
                        isLastpage = viewModel.pageNo == totalPages

                        if(isLastpage){
                            homeRV.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressbar()
                    response?.message.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressbar()
                }
            }
        })


    }

    private fun hideProgressbar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressbar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastpage = false
    var isScrolling = false



    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
//            homeRV.addItemDecoration(GridItemDecoration(10,2))
            val firstVisibleItems : IntArray? = null
            val firstVisibleItemsPosition = layoutManager.findFirstVisibleItemPositions(firstVisibleItems)
            var pastVisibleItems : Int = 0
            if(firstVisibleItemsPosition != null && firstVisibleItemsPosition.size  > 0){
                pastVisibleItems = firstVisibleItemsPosition[0]
            }

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            if(isScrolling && ((visibleItemCount + pastVisibleItems) == totalItemCount)){
                Log.e("hello","********************************************")
                    viewModel.getImages()
                    isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            isScrolling = true
        }
    }

    private fun setRecyclerView() {
        photosAdapter = PhotosAdapter()
        Log.e(TAG, "recyclerview")
        homeRV.apply {
            adapter = photosAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(this@HomeFragment.scrollListener)



            Log.e(TAG, "Layout inflated")

        }

    }

}