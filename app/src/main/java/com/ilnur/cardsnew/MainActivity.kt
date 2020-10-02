package com.ilnur.cardsnew

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.github.rubensousa.raiflatbutton.RaiflatButton
import com.github.rubensousa.raiflatbutton.RaiflatImageButton
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.ilnur.cardsnew.backend.ApiRequests
import com.ilnur.cardsnew.backend.ApiRequestsImp
import com.ilnur.cardsnew.database.AppDatabase
import com.ilnur.cardsnew.database.Subject
import com.ilnur.cardsnew.utils.observeOnce
import com.ilnur.cardsnew.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    val viewModel: MainViewModel by viewModels()

    //@Inject lateinit var apiRequests: ApiRequests
    @Inject lateinit var apiRequests: ApiRequestsImp

    suspend fun downloadNotLogged(subjects: List<Subject>){
        apiRequests.downloadAllSubjs(subjects, viewModel)
    }

    fun modifyToolbar(){
        val collTool: CollapsingToolbarLayout =
            this.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
                /*.apply {
                //title = sharedModel.currentSubj.value?.title
                expandedTitleMarginBottom = resources.getDimension(R.dimen.margin_title_col).toInt()
                setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
                setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
            }*/

        //collTool.isEnabled = false

        val learn = collTool.findViewById<RaiflatButton>(R.id.learn).apply { visibility = View.GONE }
        val watch = collTool.findViewById<RaiflatButton>(R.id.watch).apply { visibility = View.GONE }
        val rever = collTool.findViewById<RaiflatImageButton>(R.id.rever).apply { visibility = View.GONE }

        /*val appBar = this.findViewById<AppBarLayout>(R.id.appbar).apply {
            setExpanded(false)
            isClickable = false
        }*/
    }

    //@SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val appBar = findViewById<AppBarLayout>(R.id.appbar)
        appBar.setExpanded(false)



        modifyToolbar()
        //CoroutineScope(Dispatchers.IO).launch { apiRequests.downloadSubjNotLogged("math") }

        val content: CoordinatorLayout = findViewById(R.id.coordinator_layout)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //drawer.setDrawerListener(toggle)
        drawer.setScrimColor(Color.TRANSPARENT)
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
                content?.translationX = slideX
            }

        }
        drawer.drawerElevation = 4f
        drawer.addDrawerListener(drawerToggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val bar =supportActionBar


        viewModel.currentSubj.observe(this, {
            Log.d("MainActModel", it.toString())
            //Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            title = it.title
        })

        viewModel.subjects.observeOnce (this, {
            CoroutineScope(Dispatchers.IO).launch { downloadNotLogged(it) }
        })

    }// 22:20:04.526 22:21:04.018

    fun getNavController(): NavController {
        return findNavController(this, R.id.container)
    }

    override fun onCreateOptionsMenu(item: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, item)
        return true
    }

    //this is for drawer
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}