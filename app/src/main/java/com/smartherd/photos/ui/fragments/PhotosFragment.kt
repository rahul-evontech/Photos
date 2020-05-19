package com.smartherd.photos.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.smartherd.photos.R
import com.smartherd.photos.adapter.PhotosAdapter
import com.smartherd.photos.repository.PhotosRepository
import com.smartherd.photos.ui.FragmentViewModel
import com.smartherd.photos.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_photos.*

/**
 * A simple [Fragment] subclass.
 */
class PhotosFragment : Fragment(R.layout.fragment_photos) {

    val args: PhotosFragmentArgs by navArgs()
    lateinit var viewModel: FragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        val photos = args.photosHit

        Glide.with(this)
            .load(photos.largeImageURL)
            .into(photoImageView)

        type.text = photos.type
        user.text = photos.user

        floating_action_button.setOnClickListener {
            viewModel.savePhotos(photos)
            Snackbar.make(it,"Photo has been saved",Snackbar.LENGTH_LONG).show()
        }
    }


}
