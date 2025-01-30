package com.example.kormopack.presentation.fragments.feedsfrag

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.kormopack.data.specsdatabase.mapper.SpecificationFeedMapper.toModel
import com.example.kormopack.databinding.FragmentFeedsBinding
import com.example.kormopack.presentation.mainactivity.DrawerViewModel
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel
import com.example.kormopack.server.RetrofitClient

class FeedsFragment : Fragment() {
    private var _binding: FragmentFeedsBinding? = null
    private val binding get() = _binding!!

    private val drawerViewModel: DrawerViewModel by activityViewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val feedsViewModel: FeedsViewModel by viewModels {
        FeedsViewModelFactory(RetrofitClient.apiService)
    }

    lateinit var myAdapter: FeedRecycleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawerViewModel.lockDrawer()

        val brand = arguments?.getString("brand", "Deff") ?: "Deff"
        toolbarViewModel.renameToolbar(brand)

        myAdapter = FeedRecycleAdapter(requireContext(), mutableListOf())
        feedsViewModel.fetchFeeds(brand)
        feedsViewModel.feeds.observe(viewLifecycleOwner) {feeds ->
            if (feeds.size == 0) {
                binding.recycle.visibility = View.GONE
                binding.emptyImage.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Специфікації не знайдені.", Toast.LENGTH_SHORT).show()
            } else {
                binding.recycle.visibility = View.VISIBLE
                binding.emptyImage.visibility = View.GONE

                myAdapter.updateData(feeds.map { it.toModel() }.toMutableList())
                binding.recycle.adapter = myAdapter
            }
        }

        binding.feedSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString()
                feedsViewModel.fetchFeedsWhichContains(brand, searchQuery)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}