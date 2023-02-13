package com.hana.umuljeong.ui.component.imagepicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImagePicker() {
    val context = LocalContext.current
    val viewModel: ImageViewModel = viewModel(
        factory = ImageViewModelFactory(
            repository = ImageRepository(context = context)
        )
    )

    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    ImageGallery(viewModel = viewModel)
}

@Composable
private fun ImageGallery(viewModel: ImageViewModel) {
    val images = viewModel.images

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3)
    ) {
        items(images) { image ->
            ImageItem(image)
        }
    }
}

@Composable
fun ImageItem(image: ImageInfo) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(image.contentUri)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}