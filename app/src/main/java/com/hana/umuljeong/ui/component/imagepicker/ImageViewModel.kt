package com.hana.umuljeong.ui.component.imagepicker

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

internal class ImageViewModel(
    private val repository: ImageRepository
) : ViewModel() {
    private val _images = mutableStateListOf<ImageInfo>()
    val images = _images

    private val _selectedImages = mutableStateListOf<ImageInfo>()
    val selectedImages = _selectedImages

    fun loadImages() {
        images.clear()
        images.addAll(repository.getImages())
    }

    fun insertImage(): Uri? {
        return repository.insertImage()
    }

    fun selectImage(image: ImageInfo) {
        _selectedImages.add(image)
    }

    fun removeImage(image: ImageInfo) {
        _selectedImages.remove(image)
    }
}