package com.ruchanokal.satellites.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ruchanokal.satellites.R
import com.ruchanokal.satellites.databinding.FragmentDetailBinding
import com.ruchanokal.satellites.model.AllPositionModel
import com.ruchanokal.satellites.model.DetailsModel
import com.ruchanokal.satellites.model.SatelliteDetailsModel
import com.ruchanokal.satellites.model.TopPositionModel
import com.ruchanokal.satellites.roomdb.DetailDatabase
import com.ruchanokal.satellites.roomdb.DetailsDao
import com.ruchanokal.satellites.roomdb.PositionListDao
import java.io.IOException
import java.text.NumberFormat
import java.util.*


class DetailFragment : Fragment() {

    private var binding : FragmentDetailBinding? = null
    private val TAG = "DetailFragment"
    private var chosenSatelliteDetailsModel : SatelliteDetailsModel? = null
    private lateinit var db: DetailDatabase
    private lateinit var detailsDao: DetailsDao
    private lateinit var positionListDao: PositionListDao

    private var myPosition : TopPositionModel? = null
    private var myPosX : Double = 0.0
    private var myPosY : Double = 0.0


    val timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {

            val satelliteModel = DetailFragmentArgs.fromBundle(it).satellite

            val dataFromRoomDB = roomDatabaseProcess(satelliteModel.id)

            if (dataFromRoomDB != null) {

                //get datas from Room Database
                Log.i(TAG,"get data from Room Database")

                val name = dataFromRoomDB.name
                val firstFlight = dataFromRoomDB.firstFlight
                val heightToMass = dataFromRoomDB.heightMass
                val formattedCost = dataFromRoomDB.cost


                binding!!.satelliteNameTextView.text = name
                binding!!.firstLaunchTextView.text = firstFlight
                binding!!.heightMassTextView.text = heightToMass
                binding!!.costTextView.text = formattedCost

                var currentIndex = 0
                val maxIndex = dataFromRoomDB.positions.size - 1

                timer.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {

                        myPosX = dataFromRoomDB.positions.get(currentIndex).posX
                        myPosY = dataFromRoomDB.positions.get(currentIndex).posY

                        requireActivity().runOnUiThread {
                            binding!!.myPositionTextView.text = "(" + myPosX +","+ myPosY +")"
                        }

                        currentIndex++

                        if (currentIndex > maxIndex) {
                            currentIndex = 0
                        }
                    }
                }, 0, 3000)


            } else {

                //get datas from json files in assert folder
                Log.i(TAG,"get data from json files")
                val satelliteDetailList = getSatelliteDetails(requireContext())
                val positions = getAllPositions(requireContext())
                val positionList = positions.list

                for (position in positionList){

                    if (satelliteModel.id == position.id) {
                        myPosition = position
                        break
                    }
                }


                getSatelliteDetailFromId(satelliteModel.id,satelliteDetailList)

                if (chosenSatelliteDetailsModel != null) {

                    val heightToMass = chosenSatelliteDetailsModel!!.height.toString() + "/" + chosenSatelliteDetailsModel!!.mass.toString()
                    val firstFlight = chosenSatelliteDetailsModel!!.first_flight
                    val name = satelliteModel.name
                    val id = satelliteModel.id
                    val formattedCost = NumberFormat.getNumberInstance(Locale.getDefault()).format(chosenSatelliteDetailsModel!!.cost_per_launch).toString()

                    binding!!.satelliteNameTextView.text = name
                    binding!!.firstLaunchTextView.text = firstFlight
                    binding!!.heightMassTextView.text = heightToMass
                    binding!!.costTextView.text = formattedCost

                    if (myPosition != null) {
                        var currentIndex = 0
                        val maxIndex = myPosition!!.positions.size - 1

                        timer.scheduleAtFixedRate(object : TimerTask() {
                            override fun run() {

                                myPosX = myPosition!!.positions.get(currentIndex).posX
                                myPosY = myPosition!!.positions.get(currentIndex).posY

                                requireActivity().runOnUiThread {
                                    binding!!.myPositionTextView.text = "(" + myPosX +","+ myPosY +")"
                                }

                                currentIndex++

                                if (currentIndex > maxIndex) {
                                    currentIndex = 0
                                }
                            }
                        }, 0, 3000)


                        val detailsModel = DetailsModel(id,name,firstFlight,heightToMass,formattedCost,myPosition!!.positions)
                        positionListDao.insert(myPosition!!.positions)
                        detailsDao.insert(detailsModel)

                    }

                }

            }

        }

        backButton(timer)

    }

    private fun roomDatabaseProcess(id : Int) : DetailsModel {

        db = Room.databaseBuilder(
            requireContext().applicationContext,
            DetailDatabase::class.java, "Details"
        ) .allowMainThreadQueries()
            .build()

        detailsDao = db.detailsDao()
        positionListDao = db.positionListDao()

        positionListDao.getAll()
        val data = detailsDao.getDataById(id)

        Log.e(TAG,"data: " + data)
        return data
    }

    private fun backButton(timer: Timer) {

        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                timer.cancel()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    private fun getAllPositions(context: Context): AllPositionModel {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("positions.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            Log.e(TAG,"error: " + ioException)
        }

        val modelType = object : TypeToken<AllPositionModel>() {}.type
        return Gson().fromJson(jsonString, modelType)

    }

    private fun getSatelliteDetailFromId(id: Int, satelliteDetailsList : List<SatelliteDetailsModel>) {

        for (satelliteDetail in satelliteDetailsList){

             if (satelliteDetail.id == id){

                chosenSatelliteDetailsModel = satelliteDetail
                break
             }

        }

    }

    fun getSatelliteDetails(context: Context): List<SatelliteDetailsModel> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("satellite-detail.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            Log.e(TAG,"error: " + ioException)
        }

        val satelliteDetailsModelType = object : TypeToken<List<SatelliteDetailsModel>>() {}.type
        return Gson().fromJson(jsonString, satelliteDetailsModelType)
    }



}