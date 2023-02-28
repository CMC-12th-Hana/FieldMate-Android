package com.hana.umuljeong.ui.report

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeReportData
import com.hana.umuljeong.domain.Report
import com.hana.umuljeong.getCurrentTime
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportUiState(
    val report: Report = Report(0L, "", "", "", getCurrentTime(), "")
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
            _uiState.update { it.copy(report = fakeReportData[id.toInt()]) }
        }

        selectImages(_uiState.value.report.images)
    }

    fun selectImages(selectedImages: List<ImageInfo>) {
        _selectedImageList.clear()
        _selectedImageList.addAll(selectedImages)
    }

    fun removeImage(image: ImageInfo) {
        _selectedImageList.remove(image)
    }
}