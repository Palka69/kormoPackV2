package com.example.kormopack.presentation.fragments.authorizationfrag

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kormopack.R
import com.example.kormopack.databinding.FragmentAuthorizationBinding
import com.example.kormopack.domain.authfrag.CheckStringUseClass
import com.example.kormopack.presentation.mainactivity.DrawerViewModel
import com.example.kormopack.presentation.mainactivity.KEY_USER_STRING
import com.example.kormopack.presentation.mainactivity.MainAuthViewModel
import com.example.kormopack.presentation.mainactivity.PREF_NAME
import com.example.kormopack.presentation.mainactivity.ToolbarViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.sheets.v4.SheetsScopes

class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: CheckStringViewModel by activityViewModels {
        CheckStringViewModelFactory(CheckStringUseClass())
    }

    private val drawerViewModel: DrawerViewModel by activityViewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val mainAuthViewModel: MainAuthViewModel by activityViewModels()

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                mainAuthViewModel.authenticate(account)
            } catch (e: ApiException) {
                Toast.makeText(context, "Помилка під час авторизації.", Toast.LENGTH_SHORT).show()
                Log.w("SignIn", "SignInResult:failed code=" + e.statusCode)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.googleSignInButton.isEnabled = false
        drawerViewModel.lockDrawer()
        toolbarViewModel.hideToolbar()

        toolbarViewModel.changeToolbarColor(1)

        mainAuthViewModel.authResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                if (mainAuthViewModel.isSignOut.value != true) {
                    val string = binding.editTextPIB.text.toString().trim()
                    val sharPref = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                        .edit().putString(KEY_USER_STRING, string).commit()
                    drawerViewModel.changeUserString(binding.editTextPIB.text.toString().trim())
                    findNavController().navigate(R.id.action_authorizationFragment_to_specsFragment)
                } else {
                    mainAuthViewModel.setSignOut(false)
                }
            }
            result.onFailure {
                if (it.message == "Немає інтернет-з'єднання") {
                    Toast.makeText(context, "Немає інтернет-з'єднання. Перевірте ваше підключення.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Вхід дозволений лише з певної електронної пошти.", Toast.LENGTH_SHORT).show()
                }
                mainAuthViewModel.signOut()
            }
        }

        binding.editTextPIB.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                authViewModel.checkString(s.toString())
            }
        })

        binding.googleSignInButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
                .requestEmail()
                .build()

            val signInIntent = GoogleSignIn.getClient(requireContext(), gso).signInIntent
            signInLauncher.launch(signInIntent)
        }

        authViewModel.regexString.observe(viewLifecycleOwner) { regex ->
            if (regex[0]) {
                binding.editTextPIB.error = "Некоректний ПІБ"
                binding.googleSignInButton.isEnabled = false
            } else if (regex[1]) {
                binding.editTextPIB.error = null
                binding.googleSignInButton.isEnabled = true
            } else {
                binding.editTextPIB.error = null
                binding.googleSignInButton.isEnabled = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.googleSignInButton.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}