package com.hana.umuljeong.ui.component.imagepicker

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

internal class ImageViewModel(
    private val repository: ImageRepository
) : ViewModel() {
    var images = emptyList<ImageInfo>()

    private val _selectedImages = mutableStateListOf<ImageInfo>()
    val selectedImages = _selectedImages

    fun loadImages() {
        viewModelScope.launch {
            images = repository.getImages()
        }
    }
}