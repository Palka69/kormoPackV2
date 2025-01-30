package com.example.kormopack.presentation.fragments.instructionfrag

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kormopack.data.PdfRepositoryImpl
import com.example.kormopack.databinding.FragmentInstructionBinding
import com.example.kormopack.domain.instructionfrag.LoadPdfUseCase
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel
import java.io.File
import java.io.IOException

class InstructionFragment : Fragment() {

    private var _binding: FragmentInstructionBinding? = null
    private val binding get() = _binding!!

    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var totalPages: Int = 0
    private var displayPage: Int = 0

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    private val pdfViewModel: PdfViewModel by activityViewModels {
        PdfViewModelFactory(LoadPdfUseCase(PdfRepositoryImpl(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.changeToolbarColor(0)

        pdfViewModel.pdfFile.observe(viewLifecycleOwner) { file ->
            openRenderer(file)
        }

        binding.nextPage.setOnClickListener {
            if (displayPage < (totalPages - 1)) {
                displayPage++
                showPage(displayPage)
            }
        }

        binding.previousPage.setOnClickListener {
            if (displayPage > 0) {
                displayPage--
                showPage(displayPage)
            }
        }

        pdfViewModel.loadPdf("leepackPDF.pdf")
    }


    override fun onDestroy() {
        super.onDestroy()
        closeRenderer()
        _binding = null
    }

    private fun openRenderer(file: File) {
        try {
            val fileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor)
            totalPages = pdfRenderer!!.pageCount
            showPage(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun closeRenderer() {
        currentPage?.close()
        pdfRenderer?.close()
    }

    private fun showPage(index: Int) {
        pdfRenderer?.let { renderer ->
            currentPage?.close()
            currentPage = renderer.openPage(index)
            displayPage = index
            currentPage?.let { page ->
                val bitmap = Bitmap.createBitmap(currentPage!!.width, currentPage!!.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                binding.img.setImageBitmap(bitmap)
            }
            binding.pagetxt.text = "${index + 1}/$totalPages"
        }
    }
}
