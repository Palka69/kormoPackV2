package com.example.kormopack.presentation.fragments.aboutfrag

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kormopack.databinding.FragmentAboutBinding
import com.example.kormopack.presentation.mainactivity.MainAuthViewModel
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel

class AboutFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val mainAuthViewModel: MainAuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.changeToolbarColor(0)


        binding.devInstTxtLink.setOnClickListener(this)
        binding.devGithubTxtLink.setOnClickListener(this)
        binding.kormotechInstTxtLink.setOnClickListener(this)
        binding.kormotechSTxtLink.setOnClickListener(this)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        var url = ""
        if (mainAuthViewModel.networkStatus.value!!) {
            when (v?.id) {
                binding.devInstTxtLink.id -> url = "https://www.instagram.com/aezakmi___34?igsh=MXYzZnMxdmJlNzlybg=="
                binding.devGithubTxtLink.id -> url = "https://github.com/Palka69/kormoPackApp"
                binding.kormotechInstTxtLink.id -> url = "https://www.instagram.com/kormotech.global/"
                binding.kormotechSTxtLink.id -> url = "https://kormotech.com/uk"
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Немає інтернет-з'єднання", Toast.LENGTH_SHORT).show()
        }
    }
}