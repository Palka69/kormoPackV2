package com.example.kormopack.presentation.mainactivity

import KtorServer
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kormopack.R
import com.example.kormopack.data.AuthRepositoryImp
import com.example.kormopack.data.ContextProviderImp
import com.example.kormopack.data.GoogleSignInClientProvider
import com.example.kormopack.data.SignOutProviderImp
import com.example.kormopack.databinding.ActivityMainBinding
import com.example.kormopack.domain.mainact.AuthorizationUseCase
import com.example.kormopack.domain.mainact.CheckAuthUseCase
import com.example.kormopack.domain.mainact.SignOutUseCase
import com.example.kormopack.framework.receivers.NetworkChangeReceiver


const val PREF_NAME: String = "mainUserStringSharedPref"
 const val KEY_USER_STRING: String = "key"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var networkReceiver: NetworkChangeReceiver

    private val mainAuthViewModel: MainAuthViewModel by viewModels {
        MainAuthViewModelFactory(
            CheckAuthUseCase(
                AuthRepositoryImp(
                    ContextProviderImp(applicationContext)
                )
            ), SignOutUseCase(
                SignOutProviderImp(
                    applicationContext,
                    GoogleSignInClientProvider(applicationContext)
                )
            ), AuthorizationUseCase(AuthRepositoryImp(ContextProviderImp(applicationContext)))
        )
    }

    private val drawerViewModel: DrawerViewModel by viewModels()
    private val toolbarViewModel: ToolbarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMainActivity()
    }

    override fun onStart() {
        super.onStart()

        KtorServer.startServer(context = applicationContext)
    }

    override fun onStop() {
        super.onStop()

        KtorServer.stopServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)

        KtorServer.stopServer()
    }

    private fun initMainActivity() {
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        drawerViewModel.lockDrawer()

        networkReceiver = NetworkChangeReceiver(mainAuthViewModel)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, intentFilter)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.visibility = View.GONE
        binding.introLogo.visibility = View.GONE

        mainAuthViewModel.checkUserAuth()

        mainAuthViewModel.isUserAuthenticated.observe(this) { isAuthenticated ->
            initializeNavigationAndDrawer()

            if (isAuthenticated?.isUserAuthenticated == true && navController.currentDestination?.id == R.id.authorizationFragment) {
                val bundle = Bundle()
                bundle.putParcelable("user", isAuthenticated.googleUser)
                navController.navigate(R.id.action_authorizationFragment_to_specsFragment, bundle)
            }
        }

        mainAuthViewModel.isSignOut.observe(this) { isSignOut ->
            if (isSignOut) {
                when(navController.currentDestination?.id) {
                    R.id.nav_specs -> navController.navigate(R.id.action_nav_specs_to_authorizationFragment)
                    R.id.nav_pay_calc -> navController.navigate(R.id.action_nav_pay_calc_to_authorizationFragment)
                    R.id.nav_about -> navController.navigate(R.id.action_nav_about_to_authorizationFragment)
                    R.id.nav_instruction -> navController.navigate(R.id.action_instructionFragment_to_authorizationFragment)
                    R.id.nav_cabinet -> navController.navigate(R.id.action_personalCabinetFragment_to_authorizationFragment)
                }
            }
        }

        drawerViewModel.isLocked.observe(this) { isLocked ->
            if (isLocked) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        drawerViewModel.userString.observe(this) { userString ->
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.header_user).text = userString
        }

        toolbarViewModel.isShowed.observe(this) { isShowed ->
            if (isShowed) {
                binding.toolbar.visibility = View.VISIBLE
            } else {
                binding.toolbar.visibility = View.GONE
            }
        }

        toolbarViewModel.toolbarName.observe(this) { toolbarName ->
            binding.toolbar.title = toolbarName
        }

        toolbarViewModel.toolbarColor.observe(this) { toolbarColor ->
            when(toolbarColor) {
                1 -> binding.toolbar.setBackgroundColor(resources.getColor(R.color.kormoTech_calc_blue, theme))
                else -> binding.toolbar.setBackgroundColor(resources.getColor(R.color.white, theme))
            }
        }
    }


    private fun initializeNavigationAndDrawer() {
        navController = findNavController(R.id.fragment_container)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_specs, R.id.nav_cabinet, R.id.nav_pay_calc, R.id.nav_instruction, R.id.settingsFragment, R.id.nav_about),
            binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        setNavItemSelected()
    }

    private fun setNavItemSelected() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_specs -> {
                    when(navController.currentDestination?.id) {
                        R.id.nav_pay_calc -> navController.navigate(R.id.action_nav_pay_calc_to_nav_specs)
                        R.id.nav_about -> navController.navigate(R.id.action_nav_about_to_nav_specs)
                        R.id.nav_instruction -> navController.navigate(R.id.action_instructionFragment_to_nav_specs)
                        R.id.nav_cabinet -> navController.navigate(R.id.action_personalCabinetFragment_to_nav_specs)
                        R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_nav_specs)
                    }
                }
                R.id.nav_pay_calc -> {
                    when(navController.currentDestination?.id) {
                        R.id.nav_specs -> navController.navigate(R.id.action_nav_specs_to_nav_pay_calc)
                        R.id.nav_about -> navController.navigate(R.id.action_nav_about_to_nav_pay_calc)
                        R.id.nav_instruction -> navController.navigate(R.id.action_instructionFragment_to_nav_pay_calc)
                        R.id.nav_cabinet -> navController.navigate(R.id.action_personalCabinetFragment_to_nav_pay_calc)
                        R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_nav_pay_calc)
                    }
                }
                R.id.nav_about -> {
                    when(navController.currentDestination?.id) {
                        R.id.nav_specs -> navController.navigate(R.id.action_nav_specs_to_aboutFragment)
                        R.id.nav_pay_calc -> navController.navigate(R.id.action_nav_pay_calc_to_aboutFragment)
                        R.id.nav_instruction -> navController.navigate(R.id.action_instructionFragment_to_nav_about)
                        R.id.nav_cabinet -> navController.navigate(R.id.action_personalCabinetFragment_to_nav_about)
                        R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_nav_about)
                    }
                }
                R.id.nav_instruction -> {
                    when(navController.currentDestination?.id) {
                        R.id.nav_specs -> navController.navigate(R.id.action_nav_specs_to_instructionFragment)
                        R.id.nav_pay_calc -> navController.navigate(R.id.action_nav_pay_calc_to_instructionFragment)
                        R.id.nav_about -> navController.navigate(R.id.action_nav_about_to_instructionFragment)
                        R.id.nav_cabinet -> navController.navigate(R.id.action_personalCabinetFragment_to_nav_instruction)
                        R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_nav_instruction)
                    }
                }
                R.id.nav_cabinet -> {
                    when(navController.currentDestination?.id) {
                        R.id.nav_specs -> navController.navigate(R.id.action_nav_specs_to_personalCabinetFragment)
                        R.id.nav_pay_calc -> navController.navigate(R.id.action_nav_pay_calc_to_personalCabinetFragment)
                        R.id.nav_about -> navController.navigate(R.id.action_nav_about_to_personalCabinetFragment)
                        R.id.nav_instruction -> navController.navigate(R.id.action_nav_instruction_to_personalCabinetFragment)
                        R.id.settingsFragment -> navController.navigate(R.id.action_settingsFragment_to_nav_cabinet)
                    }
                }
                R.id.settingsFragment -> {
                    when(navController.currentDestination?.id) {
                        R.id.nav_specs -> navController.navigate(R.id.action_nav_specs_to_settingsFragment)
                        R.id.nav_pay_calc -> navController.navigate(R.id.action_nav_pay_calc_to_settingsFragment)
                        R.id.nav_about -> navController.navigate(R.id.action_nav_about_to_settingsFragment)
                        R.id.nav_instruction -> navController.navigate(R.id.action_nav_instruction_to_settingsFragment)
                        R.id.nav_cabinet -> navController.navigate(R.id.action_nav_cabinet_to_settingsFragment)
                    }
                }
                R.id.nav_exit -> {
                    if (mainAuthViewModel.networkStatus.value!!) {
                        mainAuthViewModel.setSignOut(true)
                        mainAuthViewModel.signOut()
                    } else {
                        Toast.makeText(applicationContext, "Немає інтернет-з'єднання", Toast.LENGTH_SHORT).show()
                    }
                } else -> {
                    navController.navigate(R.id.action_nav_specs_to_authorizationFragment)
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}