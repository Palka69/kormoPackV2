package com.example.kormopack.presentation.fragments.specsfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kormopack.R
import com.example.kormopack.data.specsdatabase.mapper.SpecificationBrandMapper.toModel
import com.example.kormopack.databinding.FragmentSpecsBinding
import com.example.kormopack.presentation.mainactivity.DrawerViewModel
import com.example.kormopack.presentation.mainactivity.KEY_USER_STRING
import com.example.kormopack.presentation.mainactivity.MainAuthViewModel
import com.example.kormopack.presentation.mainactivity.PREF_NAME
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel
import com.example.kormopack.server.RetrofitClient

class SpecsFragment : Fragment() {

    private var _binding: FragmentSpecsBinding? = null
    private val binding get() = _binding!!

    private val drawerViewModel: DrawerViewModel by activityViewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val mainAuthViewModel: MainAuthViewModel by activityViewModels()
    private val specsViewModel: SpecsViewModel by viewModels {
        SpecsViewModelFactory(RetrofitClient.apiService)
    }

    lateinit var myAdapter: FeedBrandsRecycleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawerViewModel.unlockDrawer()
        toolbarViewModel.showToolbar()
        toolbarViewModel.changeToolbarColor(0)

        mainAuthViewModel.checkUserAuth()

        val sharPref = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharPref.getString(KEY_USER_STRING, "User")?.let { drawerViewModel.changeUserString(it) }

        val onBrandClickListener: FeedBrandsRecycleAdapter.OnBrandClickListener =
            object : FeedBrandsRecycleAdapter.OnBrandClickListener {
                override fun onBrandClick(text: String) {
                    val bundle = Bundle()
                    bundle.putString("brand", text)
                    findNavController().navigate(R.id.action_nav_specs_to_feedsFragment, bundle)
                }
            }
        myAdapter = FeedBrandsRecycleAdapter(requireContext(), listOf(), onBrandClickListener)
        binding.recycle.adapter = myAdapter

        specsViewModel.brands.observe(viewLifecycleOwner) {brands ->
            myAdapter.updateData(brands.map { it.toModel() })
            myAdapter.notifyDataSetChanged()
            binding.recycle.visibility = View.VISIBLE
            binding.prgresbar.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        specsViewModel.fetchFeedBrands()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}