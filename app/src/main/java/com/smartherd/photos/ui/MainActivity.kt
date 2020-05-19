package com.smartherd.photos.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.smartherd.photos.R
import com.smartherd.photos.db.PhotoDatabase
import com.smartherd.photos.network.Api
import com.smartherd.photos.repository.PhotosRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: FragmentViewModel

    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val api = Api()
        val db = PhotoDatabase(this)

        val repository = PhotosRepository(api,db)

        val factory = FragmentViewModelFactory(repository)

        viewModel = ViewModelProvider(this,factory).get(FragmentViewModel::class.java)






        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            supportActionBar?.hide()
            bottomNavigationView.setupWithNavController(findNavController(R.id.fragment))
        }else{
            navController = findNavController(R.id.fragment)
            drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

            NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
            NavigationUI.setupWithNavController(navView,navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

}
