package com.example.kormopack.presentation.fragments.instructionfrag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kormopack.domain.instructionfrag.LoadPdfUseCase


class PdfViewModelFactory(
    private val loadPdfUseCase: LoadPdfUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfViewModel::class.java)) {
            return PdfViewModel(loadPdfUseCase,) as T
        }
        throw IllegalArgumentException("Невідомий ViewModel-клас")
    }
}


