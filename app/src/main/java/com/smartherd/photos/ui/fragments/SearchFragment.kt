package com.smartherd.photos.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smartherd.photos.R
import com.smartherd.photos.adapter.PhotosAdapter
import com.smartherd.photos.ui.FragmentViewModel
import com.smartherd.photos.ui.MainActivity
import com.smartherd.photos.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var viewModel: FragmentViewModel
    lateinit var photosAdapter: PhotosAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setRecyclerView()

        photosAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("photosHit",it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_photosFragment,
                bundle
            )
        }


        var job :Job? = null
        etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchImages(editable.toString())
                    }
                }
            }
        }



        viewModel.search.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressbar()
                    response?.data.let { newResponse ->
                        photosAdapter.differ.submitList(newResponse?.hits?.toList())
                        val totalPages = newResponse!!.total / 20 + 2
                        isLastpage = viewModel.pageNo == totalPages

                        if (isLastpage) {
                            homeRV.setPadding(0, 0, 0, 0)
                        }
                    }
                }


                is Resource.Error -> {
                    hideProgressbar()
                    response.let {
                        Log.e("hello","${response.message}")
                    }
                }
                is Resource.Loading -> {
                    showProgressbar()
                }
            }
        })


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
                viewModel.searchImages(etSearch.toString())
                isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            isScrolling = true
        }
    }

    private fun setRecyclerView(){
        photosAdapter = PhotosAdapter()
        searchRV.apply {
            adapter = photosAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    private fun hideProgressbar(){
        paginationProgressBarS.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressbar(){
        paginationProgressBarS.visibility = View.VISIBLE
        isLoading = true
    }
}
