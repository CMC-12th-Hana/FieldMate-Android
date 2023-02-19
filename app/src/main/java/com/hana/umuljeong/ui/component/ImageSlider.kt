package com.hana.umuljeong.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo
import com.hana.umuljeong.ui.theme.*

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
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
                            .clip(Shapes.large),
                        filterQuality = FilterQuality.Low,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }
        }
    }
}