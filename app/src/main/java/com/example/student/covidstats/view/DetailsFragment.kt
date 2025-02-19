package com.example.student.covidstats.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.student.covidstats.CountryViewModel
import com.example.student.covidstats.R

const val ARG_COUNTRY_NAME = "name"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    private lateinit var mSelectedCountry: String
//    private var listFilter: String? = null
    private lateinit var countryViewModel: CountryViewModel
    private lateinit var mGraphViewConfirmed: GraphView
    private lateinit var mGraphViewActive: GraphView
    private lateinit var mGraphViewDeaths: GraphView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (savedInstanceState != null) {
            mSelectedCountry = savedInstanceState.getString(ARG_COUNTRY_NAME)!!
//            listFilter = savedInstanceState.getString(ARG_COUNTRIES_FILTER)
        }

        if (arguments != null) {
            mSelectedCountry = arguments!!.getString(ARG_COUNTRY_NAME)!!
//            listFilter = arguments!!.getString(ARG_COUNTRIES_FILTER)

        }
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        countryViewModel.setCountryName(mSelectedCountry)
        countryViewModel.singleCountry.observe(viewLifecycleOwner, Observer { singleCountry ->
            view.findViewById<TextView>(R.id.name).text = singleCountry?.name ?: ""
            view.findViewById<TextView>(R.id.total_confirmed).text =
                singleCountry?.total_confirmed.toString()
            view.findViewById<TextView>(R.id.new_confirmed).text =
                singleCountry?.new_confirmed.toString()
            view.findViewById<TextView>(R.id.total_deaths).text =
                singleCountry?.total_deaths.toString()
            view.findViewById<TextView>(R.id.new_deaths).text = singleCountry?.new_deaths.toString()
            view.findViewById<TextView>(R.id.total_recovered).text =
                singleCountry?.total_recovered.toString()
            view.findViewById<TextView>(R.id.new_recovered).text =
                singleCountry?.new_recovered.toString()
        })

        mGraphViewConfirmed = view.findViewById(R.id.graph1)
        mGraphViewActive = view.findViewById(R.id.graph2)
        mGraphViewDeaths = view.findViewById(R.id.graph3)
        var label1 = view.findViewById<TextView>(R.id.graph1_label)
        var label2 = view.findViewById<TextView>(R.id.graph2_label)
        var label3 = view.findViewById<TextView>(R.id.graph3_label)
        var lastUpdate = view.findViewById<TextView>(R.id.last_updated)
        mGraphViewConfirmed.invalidate()
        mGraphViewActive.invalidate()
        mGraphViewDeaths.invalidate()
        countryViewModel.lastUpdate.observe(viewLifecycleOwner, Observer { date ->
            if(date != null) {
                lastUpdate.text = "Last updated:\n" + date.date
            }

        })
        countryViewModel.countryDetail.observe(viewLifecycleOwner, Observer { details ->
            if (details.isNotEmpty()) {
                mGraphViewConfirmed.visibility = View.VISIBLE
                mGraphViewActive.visibility = View.VISIBLE
                mGraphViewDeaths.visibility = View.VISIBLE
                label1.visibility = View.VISIBLE
                label2.visibility = View.VISIBLE
                label3.visibility = View.VISIBLE
            } else {
                mGraphViewConfirmed.visibility = View.GONE
                mGraphViewActive.visibility = View.GONE
                mGraphViewDeaths.visibility = View.GONE
                label1.visibility = View.GONE
                label2.visibility = View.GONE
                label3.visibility = View.GONE
            }
            mGraphViewConfirmed.setParameters(details,
                GRAPH_TYPE_CONFIRMED
            )
            mGraphViewConfirmed.invalidate()
            mGraphViewActive.setParameters(details,
                GRAPH_TYPE_ACTIVE
            )
            mGraphViewActive.invalidate()
            mGraphViewDeaths.setParameters(details,
                GRAPH_TYPE_DEATHS
            )
            mGraphViewDeaths.invalidate()
        })

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_COUNTRY_NAME, mSelectedCountry)
//        outState.putString(ARG_COUNTRIES_FILTER, listFilter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        countryViewModel =
            activity?.let { ViewModelProvider(it).get(CountryViewModel::class.java) }!!
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(country: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COUNTRY_NAME, country)
                }
            }
    }
}
