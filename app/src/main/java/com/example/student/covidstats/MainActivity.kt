package com.example.student.covidstats

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), CountryFragment.OnListFragmentSelectionListener {
    private var mDetailsFragment: DetailsFragment? = null
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mCountryFragment: CountryFragment
    private lateinit var countryViewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFragmentManager = supportFragmentManager
        countryViewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction().run {
                mCountryFragment = CountryFragment()
                replace(R.id.countries_fragment_container, mCountryFragment)
                commit()
            }
        }
    }

    override fun onListFragmentSelection(item: CountryEntity) {
        if (mDetailsFragment === null) {
            mDetailsFragment = DetailsFragment()
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
}
