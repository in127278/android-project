package com.example.student.covidstats

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.student.covidstats.dummy.DummyContent
import com.example.student.covidstats.dummy.DummyContent.DummyItem

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [CountryFragment.OnListFragmentSelectionListener] interface.
 */
class CountryFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    lateinit var adapter: MyCountryRecyclerViewAdapter
    private var listener: OnListFragmentSelectionListener? = null
    private lateinit var countryViewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryViewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_country_list, container, false)
        context?.let { BackgroundIntentService.startActionFetchAll(it) }
        val recycler = view.findViewById<RecyclerView>(R.id.list)
        if (recycler is RecyclerView) {
            with(recycler) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
//                var db = CountryEntityRoomDatabase.getDatabase(context)
//                items = db.countryEntityDao().getCountriesOrdered()
                adapter = MyCountryRecyclerViewAdapter(listener)
                countryViewModel.allCountries.observe(viewLifecycleOwner, Observer { words ->
                    words?.let {  (adapter as MyCountryRecyclerViewAdapter).setCountries(it) }
                })
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentSelectionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentSelectionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentSelectionListener {
        fun onListFragmentSelection(item: CountryEntity)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CountryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
