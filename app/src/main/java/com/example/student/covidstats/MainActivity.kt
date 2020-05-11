package com.example.student.covidstats

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.student.covidstats.db.CountryEntity
import com.example.student.covidstats.view.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), CountryFragment.OnListFragmentSelectionListener,
    NavigationView.OnNavigationItemSelectedListener {
    private var mDetailsFragment: DetailsFragment? = null
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mCountryFragment: CountryFragment
    private lateinit var countryViewModel: CountryViewModel
    private lateinit var mDrawer: DrawerLayout
//    private lateinit var mToolbar: Toolbar
//    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        mDrawer = findViewById(R.id.drawer_layout)
        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar)
        var toggle = ActionBarDrawerToggle(
            this, mDrawer, toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()


        mFragmentManager = supportFragmentManager
        countryViewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction().run {
                mCountryFragment =
                    CountryFragment()
                val args = Bundle()
                args.putString(ARG_COUNTRIES_FILTER, "ALL")
                mCountryFragment.arguments = args
                replace(R.id.countries_fragment_container, mCountryFragment)
                commit()
            }
        }
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    override fun onListFragmentSelection(item: CountryEntity) {
        if (mDetailsFragment === null) {
            mDetailsFragment =
                DetailsFragment()
        }
        mDetailsFragment?.let {
            if (!it.isAdded) {
                val args = Bundle()
                args.putString(ARG_COUNTRY_NAME, item.name)
                mDetailsFragment!!.arguments = args
                val fragmentTransaction = mFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.countries_fragment_container, it)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                mFragmentManager.executePendingTransactions()
                BackgroundIntentService.startActionFetchDetails(applicationContext, item.name)
            }
        }
    }

    companion object {
        var MY_NOTIFICATION_ID = 1337
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.all -> {
                manageFragment("ALL")
            }
            R.id.africa -> {
                manageFragment(PARAM_AFRICA)
            }
            R.id.asia -> {
                manageFragment(PARAM_ASIA)
            }
            R.id.australia -> {
                manageFragment(PARAM_OCEANIA)
            }
            R.id.europe -> {
                manageFragment(PARAM_EUROPE)
            }
            R.id.america -> {
                manageFragment(PARAM_NORTH_AMERICA)
            }
            R.id.samerica -> {
                manageFragment(PARAM_SOUTH_AMERICA)
            }
        }
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun manageFragment(parameter: String) {
        mFragmentManager.beginTransaction().run {
            mFragmentManager.popBackStack()
            mCountryFragment =
                CountryFragment()
            val args = Bundle()
            args.putString(ARG_COUNTRIES_FILTER, parameter)
            mCountryFragment!!.arguments = args
            replace(R.id.countries_fragment_container, mCountryFragment)
            commit()
        }


    }

}
