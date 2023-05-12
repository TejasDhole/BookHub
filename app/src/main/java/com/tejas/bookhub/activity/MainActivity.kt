package com.tejas.bookhub.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.tejas.bookhub.R
import com.tejas.bookhub.fragment.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView

    private var previousMenuItem:MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinator)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        openDashboard()


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem!= null){
                onBackPressed()
                previousMenuItem?.isChecked=false

        }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

             when(it.itemId){
                R.id.dashboard ->{
                openDashboard()
                    drawerLayout.closeDrawers()
                }
                 R.id.profile ->{
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.frame, Profile())
                         .commit()

                     supportActionBar?.title = "Profile"
                     drawerLayout.closeDrawers()
                 }
                 R.id.fav ->{
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.frame, Favourites())
                         .commit()
                     supportActionBar?.title = "Favourites"

                     drawerLayout.closeDrawers()
                 }
                 R.id.about ->{
                     supportFragmentManager.beginTransaction()
                         .replace(R.id.frame, About())
                         .commit()

                     supportActionBar?.title = "about"

                     drawerLayout.closeDrawers()
                 }
            }
            return@setNavigationItemSelectedListener true
        }

    }

         fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar title"
         supportActionBar?.setHomeButtonEnabled(true)
         supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)

        }
        return super.onOptionsItemSelected(item)
    }


    private fun openDashboard(){
        val fragment = Dashboard()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        when(supportFragmentManager.findFragmentById(R.id.frame)){

            !is Dashboard -> {
                openDashboard()
            }

            else -> {
                super.onBackPressed()
            }
        }
    }




}
