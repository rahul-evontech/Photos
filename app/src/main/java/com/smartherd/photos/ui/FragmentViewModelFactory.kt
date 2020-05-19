package com.smartherd.photos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartherd.photos.repository.PhotosRepository

class FragmentViewModelFactory(
    val repository: PhotosRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FragmentViewModel(repository) as T
    }
}