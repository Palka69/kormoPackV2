package com.example.kormopack.presentation.fragments.instructionfrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kormopack.domain.instructionfrag.LoadPdfUseCase
import java.io.File

class PdfViewModel(private val loadPdfUseCase: LoadPdfUseCase) : ViewModel() {
    private val _pdfFile = MutableLiveData<File>()
    val pdfFile: LiveData<File> get() = _pdfFile

    fun loadPdf(fileName: String) {
        _pdfFile.value = loadPdfUseCase.execute(fileName)
    }
}