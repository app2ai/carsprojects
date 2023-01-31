package com.sevenpeakssoftware.vishalr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.sevenpeakssoftware.vishalr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as CarApplication).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val myNavController = navHostFragment.navController

        myNavController.addOnDestinationChangedListener { cntr, dest, args ->
            when (dest.id) {
                R.id.splashFragment -> {
                    myNavController.popBackStack(R.id.splashFragment, true)
                }
                R.id.carListFragment -> {}
            }
        }
    }
}
