package com.ruchanokal.satellites.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ruchanokal.satellites.R
import com.ruchanokal.satellites.adapter.ListAdapter
import com.ruchanokal.satellites.databinding.FragmentListBinding
import com.ruchanokal.satellites.model.SatelliteModel
import java.io.IOException
import java.util.*


class ListFragment : Fragment() {

    private var binding: FragmentListBinding? = null
    private val TAG = "ListFragment"
    private var menuItem : MenuItem? = null
    private var searchView : SearchView? = null
    var tempSatelliteList = arrayListOf<SatelliteModel>()
    var mySatelliteList : List<SatelliteModel>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mySatelliteList = getSatellite(requireContext())
        Log.i(TAG,"satellite list: " + mySatelliteList)
        tempSatelliteList.clear()
        tempSatelliteList.addAll(mySatelliteList!!)

        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.adapter = ListAdapter(tempSatelliteList)
        binding!!.progressBar.visibility= View.GONE

        backButton()
    }

    private fun backButton() {

        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu,menu)
        menuItem = menu.findItem(R.id.searchId)
        searchView = MenuItemCompat.getActionView(menuItem!!) as SearchView
        searchView!!.isIconified = true

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                tempSatelliteList.clear()

                val searchText = p0!!.lowercase(Locale.getDefault())

                if (searchText.isNotEmpty()) {

                    mySatelliteList?.forEach {

                        if (it.name.lowercase(Locale.getDefault()).contains(searchText)){

                            tempSatelliteList.add(it)
                        }

                    }

                    binding!!.recyclerView.adapter!!.notifyDataSetChanged()

                } else {

                    tempSatelliteList.clear()
                    mySatelliteList?.let { tempSatelliteList.addAll(it) }
                    binding!!.recyclerView.adapter!!.notifyDataSetChanged()


                }


                return false
            }


        })

        super.onCreateOptionsMenu(menu, inflater)


    }

    fun getSatellite(context: Context): List<SatelliteModel> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("satellite-list.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            Log.e(TAG,"error: " + ioException)
        }

        val satelliteModelType = object : TypeToken<List<SatelliteModel>>() {}.type
        return Gson().fromJson(jsonString, satelliteModelType)
    }



}