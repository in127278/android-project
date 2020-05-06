package com.example.student.covidstats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.student.covidstats.dummy.DummyContent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
private const val ARG_COUNTRY_NAME = "position"
/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
//    private const val ARG_COUNTRY_NAME = "position"
    val ARG_COUNTRY_NAME = "country"
    var mCurrentItem: String? = null
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    private val ARG_COUNTRY_NAME: String? = "position"
    private lateinit var mItem: DummyContent.DummyItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("asd","CreateDetails")
//        arguments?.let {
//            mItem = ArrayAdapter(it, R.layout.details, 123)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("asd","CreateViewDetails")
        if (savedInstanceState != null) {
            mCurrentItem =
                savedInstanceState.getString(ARG_COUNTRY_NAME)
        }
        return inflater.inflate(R.layout.fragment_details, container, false)
    }
    fun showItem(item: DummyContent.DummyItem) {
        mItem = item
        Log.d("asd","showItem")
    }

    override fun onStart() {
        super.onStart()
        val args = arguments
        if (args != null) { // Set article based on argument passed in
            args.getString(ARG_COUNTRY_NAME)?.let { updateArticleView(it) }
        } else if (mCurrentItem !== null) { // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentItem!!)
        }
    }
    fun updateArticleView(position: String) {
        val article = activity!!.findViewById(R.id.position) as TextView
        article.text = position
        mCurrentItem = position
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current article selection in case we need to recreate the fragment
        outState.putString(
            ARG_COUNTRY_NAME,
            mCurrentItem
        )
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
