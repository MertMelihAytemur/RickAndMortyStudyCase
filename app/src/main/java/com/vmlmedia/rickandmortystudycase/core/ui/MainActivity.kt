package com.vmlmedia.rickandmortystudycase.core.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.vmlmedia.rickandmortystudycase.R
import com.vmlmedia.rickandmortystudycase.common.ext.findNavController
import com.vmlmedia.rickandmortystudycase.core.ui.LoadingDialog
import com.vmlmedia.rickandmortystudycase.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = findNavController()

    }
}