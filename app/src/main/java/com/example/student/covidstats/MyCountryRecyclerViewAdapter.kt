package com.example.student.covidstats


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student.covidstats.CountryFragment.OnListFragmentSelectionListener
import kotlinx.android.synthetic.main.fragment_country.view.*

/**
 * [RecyclerView.Adapter] that can display a [CountryEntity] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyCountryRecyclerViewAdapter(
//    private var countries: List<CountryEntity>,
//    context: Context,
    private val mListener: OnListFragmentSelectionListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var countries= emptyList<CountryEntity>()
    private val mOnClickListener: View.OnClickListener
    private val mOnClickSortTotalListener: View.OnClickListener
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private var TOGGLE_NAME = true
    private var TOGGLE_TOTAL_CONFIRMED = true
    private var TOGGLE_NEW_CONFIRMED = true
    private var TOGGLE_TOTAL_DEATHS = true
    private var TOGGLE_NEW_DEATHS = true

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as CountryEntity
            mListener?.onListFragmentSelection(item)
        }
        mOnClickSortTotalListener = View.OnClickListener {
            v -> when(v.tag) {
                1 -> {
                    countries = if(TOGGLE_NAME)  countries.sortedBy { it.name } else countries.sortedByDescending { it.name }
                    TOGGLE_NAME = TOGGLE_NAME.not()
                    notifyDataSetChanged()

                }
                2 -> {
                    countries = if(TOGGLE_TOTAL_CONFIRMED)  countries.sortedByDescending { it.total_confirmed } else countries.sortedBy { it.total_confirmed }
                    TOGGLE_TOTAL_CONFIRMED = TOGGLE_TOTAL_CONFIRMED.not()
                    notifyDataSetChanged()

                }
                3 -> {
                    countries = if(TOGGLE_NEW_CONFIRMED)  countries.sortedByDescending { it.new_confirmed } else countries.sortedBy { it.new_confirmed }
                    TOGGLE_NEW_CONFIRMED = TOGGLE_NEW_CONFIRMED.not()
                    notifyDataSetChanged()
                }
                4 -> {
                    countries = if(TOGGLE_TOTAL_DEATHS)  countries.sortedByDescending { it.total_deaths } else countries.sortedBy { it.total_deaths }
                    TOGGLE_TOTAL_DEATHS = TOGGLE_TOTAL_DEATHS.not()
                    notifyDataSetChanged()
                }
                5 -> {
                    countries = if(TOGGLE_NEW_DEATHS)  countries.sortedByDescending { it.new_deaths } else countries.sortedBy { it.new_deaths }
                    TOGGLE_NEW_DEATHS = TOGGLE_NEW_DEATHS.not()
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_country_header, parent, false)
            ViewHolderHeader(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_country, parent, false)
            ViewHolderList(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder) {
           is ViewHolderList -> {
               val item = countries[position]
               holder.mIdView.text = item.name
               holder.mTotalConfirmedView.text = item.total_confirmed.toString()
               holder.mNewConfirmedView.text = item.new_confirmed.toString()
               holder.mTotalDeathsView.text = item.total_deaths.toString()
               holder.mNewDeathsView.text = item.new_deaths.toString()
               with(holder.mView) {
                   tag = item
                   setOnClickListener(mOnClickListener)
               }
           }
           is ViewHolderHeader -> {
               with(holder.mIdView) {
                   tag = 1
                   setOnClickListener(mOnClickSortTotalListener)
               }
               with(holder.mTotalConfirmedView) {
                   tag = 2
                   setOnClickListener(mOnClickSortTotalListener)
               }
               with(holder.mNewConfirmedView) {
                   tag = 3
                   setOnClickListener(mOnClickSortTotalListener)
               }
               with(holder.mTotalDeathsView) {
                   tag = 4
                   setOnClickListener(mOnClickSortTotalListener)
               }
               with(holder.mNewDeathsView) {
                   tag = 5
                   setOnClickListener(mOnClickSortTotalListener)
               }
           }
           else -> Log.d("asd","ViewHolder not found");
       }

    }
//    internal fun setWords(countries: List<CountryEntity>) {
//        this.countries = countries
//        notifyDataSetChanged()
//    }
    override fun getItemCount(): Int = countries.size
    fun setCountries(countries: List<CountryEntity>) {
        this.countries = countries
        notifyDataSetChanged()
    }
    inner class ViewHolderList(val mView: View) :
        RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.name
        val mTotalConfirmedView: TextView = mView.total_confirmed
        val mNewConfirmedView: TextView = mView.new_confirmed
        val mTotalDeathsView: TextView = mView.total_deaths
        val mNewDeathsView: TextView = mView.new_deaths
    }
    inner class ViewHolderHeader(val mView: View) :
        RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.name
        val mTotalConfirmedView: TextView = mView.total_confirmed
        val mNewConfirmedView: TextView = mView.new_confirmed
        val mTotalDeathsView: TextView = mView.total_deaths
        val mNewDeathsView: TextView = mView.new_deaths
    }

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }
    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }
//    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
//        val mIdView: TextView = mView.name
//        val mTotalConfirmedView: TextView = mView.total_confirmed
//        val mNewConfirmedView: TextView = mView.new_confirmed
//        val mTotalDeathsView: TextView = mView.total_deaths
//        val mNewDeathsView: TextView = mView.new_deaths
//
////        override fun toString(): String {
////            return super.toString() + " '" + mContentView.text + "'"
////        }
//    }
}
