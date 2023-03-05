package com.hana.fieldmate.ui.report

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.local.fakeReportDataSource
import com.hana.fieldmate.domain.model.ReportEntity
import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportUiState(
    val reportEntity: ReportEntity = ReportEntity(0L, "", "", "", getCurrentTime(), "")
)

class ReportViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    private val _selectedImageList = mutableStateListOf<ImageInfo>()
    val selectedImageList = _selectedImageList

    init {
        val id: Long? = savedStateHandle["reportId"]
        if (id != null) loadReport(id)
    }

    fun loadReport(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(reportEntity = fakeReportDataSource[id.toInt()]) }
        }

        selectImages(_uiState.value.reportEntity.images)
    }

    fun selectImages(selectedImages: List<ImageInfo>) {
        _selectedImageList.clear()
        _selectedImageList.addAll(selectedImages)
    }

    fun removeImage(image: ImageInfo) {
        _selectedImageList.remove(image)
    }
}