package io.github.xfy9326.glance.ui.screen.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.xfy9326.atools.coroutines.suspendLazy
import io.github.xfy9326.glance.ml.MLManager
import io.github.xfy9326.glance.ml.beans.ModelType
import io.github.xfy9326.glance.ml.beans.PixelsData
import io.github.xfy9326.glance.ui.data.AnalysisResult
import io.github.xfy9326.glance.ui.data.convertToAnalysisResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GuideViewModel : ViewModel() {
    private val classLabels by suspendLazy { MLManager.loadLabels(ModelType.GUIDE_MODEL) }
    private val mlModel = MLManager.getModel(ModelType.GUIDE_MODEL)

    private val _analysisResult = MutableStateFlow<AnalysisResult>(AnalysisResult.Initializing)
    val analysisResult = _analysisResult.asStateFlow()

    fun detectImage(pixelsData: PixelsData) {
        viewModelScope.launch(Dispatchers.Default) {
            val result = mlModel.detectByPixelsData(pixelsData)
            _analysisResult.value = result.convertToAnalysisResult(classLabels.value())
        }
    }
}
