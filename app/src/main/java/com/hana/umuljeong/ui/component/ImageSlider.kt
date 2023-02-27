package com.hana.umuljeong.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo
import com.hana.umuljeong.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    maxImgCount: Int = 10,
    onSelect: (Int) -> Unit,
    removeImage: ((ImageInfo) -> Unit)? = null,
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
                itemsIndexed(selectedImages) { index, image ->
                    DeletableImageItem(
                        image = image,
                        onSelect = { onSelect(index) },
                        removeImage = removeImage
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun DeletableImageItem(
    image: ImageInfo,
    onSelect: () -> Unit,
    removeImage: ((ImageInfo) -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.TopEnd
    ) {
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

        if (removeImage != null) {
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(
                    onClick = { removeImage(image) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_btn),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DetailImageDialog(
    modifier: Modifier = Modifier,
    selectedImages: List<ImageInfo>,
    imageIndex: Int = 0,
    onClosed: () -> Unit,
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            BackHandler {
                onClosed()
            }

            Box(contentAlignment = Alignment.BottomCenter) {
                HorizontalPager(
                    count = selectedImages.size,
                    state = pagerState,
                ) { page ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedImages[page].contentUri)
                            .build(),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 70.dp),
                        filterQuality = FilterQuality.None,
                        contentScale = ContentScale.Fit,
                        contentDescription = null
                    )
                }

                Row(
                    modifier = Modifier.height(70.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in selectedImages.indices) {
                            val selected = (i == pagerState.currentPage)

                            Spacer(
                                modifier
                                    .size(10.dp)
                                    .background(
                                        color = if (selected) Main356DF8 else BgD3D3D3,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            LaunchedEffect(true) {
                coroutineScope.launch {
                    pagerState.scrollToPage(imageIndex)
                }
            }
        }
    }
}