package com.example.kormopack.presentation.fragments.paycalcfrag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kormopack.databinding.FragmentPayCalcBinding
import com.example.kormopack.presentation.mainactivity.MainAuthViewModel
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel

class PayCalcFragment : Fragment() {

    private var _binding: FragmentPayCalcBinding? = null
    private val binding get() = _binding!!

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    private val mainAuthViewModel: MainAuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPayCalcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.changeToolbarColor(1)

        val webView = binding.webView
        val progressBar = binding.progress

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?, errorCode: Int, description: String?, failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error loading page", Toast.LENGTH_SHORT).show()
            }

            override fun onReceivedHttpError(
                view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                progressBar.visibility = View.GONE
                Toast.makeText(context, "HTTP error loading page", Toast.LENGTH_SHORT).show()
            }
        }

        mainAuthViewModel.networkStatus.observe(viewLifecycleOwner) { connection ->
            if (connection) {
                webView.loadUrl("https://salarycalculator-72940.web.app/")
            } else {
                Toast.makeText(requireContext(), "Немає інтернет-з'єднання", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}