package com.hana.umuljeong.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo
import com.hana.umuljeong.ui.theme.*

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    navController: NavController,
    maxImgCount: Int = 10,
    selectedImages: List<ImageInfo>
) {
    if (selectedImages.isNotEmpty()) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "${selectedImages.size}", style = Typography.body5, color = Main356DF8)
                Text(text = "/$maxImgCount", style = Typography.body5, color = Font70747E)
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(all = 0.dp)
            ) {
                items(selectedImages) { image ->
                    val context = LocalContext.current

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(image.contentUri)
                            .build(),
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1.0F)
                            .clip(Shapes.large)
                            .clickable(onClick = {
                                navController.navigate("${UmuljeongScreen.DetailImage.name}?imageUrl=${image.contentUri}")
                            }),
                        filterQuality = FilterQuality.Low,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun PreviewImage(
    modifier: Modifier = Modifier,
    imageUri: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .build(),
            modifier = Modifier.fillMaxSize(),
            filterQuality = FilterQuality.None,
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
    }
}