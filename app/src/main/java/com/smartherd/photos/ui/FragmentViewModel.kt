package com.smartherd.photos.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartherd.photos.models.Hit
import com.smartherd.photos.models.Photos
import com.smartherd.photos.repository.PhotosRepository
import com.smartherd.photos.utils.Resource
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class FragmentViewModel(
    val repository: PhotosRepository
): ViewModel() {

    val photos: MutableLiveData<Resource<Photos>> = MutableLiveData()
    var pageNo = 1
    var newPhotos: Photos? = null

    val search: MutableLiveData<Resource<Photos>> = MutableLiveData()
    var searchPage = 1
    var newSearch: Photos? = null

    val images: MutableLiveData<List<Hit>> = MutableLiveData()

    init {
        getImages()
    }

    fun getImages() = viewModelScope.launch {
        Log.e("viewModel","get  Images")

        photos.postValue(Resource.Loading())
        Log.e("viewModel","page -------------------- ${pageNo}")
        val response = repository.getPhotos(pageNo)
        photos.postValue(getImagesResource(response))
    }

    fun getImagesResource(response: Response<Photos>): Resource<Photos>{
        Log.e("viewModel","get Image Resources")
        if(response.isSuccessful){
            response?.body().let {photo ->
                pageNo++
                Log.e("viewModel","page ********************* ${pageNo}")
                if(newPhotos == null){
                    Log.e("newphotos","NULllllllllllllllllllllllllllllllllllll")
                    newPhotos = photo
                }else{
                    val oldPhoto = newPhotos?.hits
                    val newPhoto = photo?.hits
                    oldPhoto?.addAll(newPhoto!!)
                }
                Log.e("newphotos","photos \n ${newPhotos}")
                return Resource.Success(newPhotos ?: photo)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchImages(searchString: String) = viewModelScope.launch {
        search.postValue(Resource.Loading())
        val response = repository.searchPhotos(searchString)
        search.postValue(searchImagesResource(response))
    }

    fun searchImagesResource(response: Response<Photos>): Resource<Photos>{
        if(response.isSuccessful){
            response?.body().let {photo ->
                searchPage++
                if(newSearch == null){
                    newSearch = photo
                }else{
                    val oldPhoto = newSearch?.hits
                    val newPhoto = photo?.hits
                    newPhoto?.addAll(oldPhoto!!)
                }

                return Resource.Success(newSearch ?: photo)
            }
        }
        return Resource.Error(response.message())
    }

    fun savePhotos(hit: Hit) = viewModelScope.launch {
        repository.upsert(hit)
    }

    fun getAllPhotos() =   repository.getAllPhotos()


}