package com.example.restaurapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.reservartions.Reservation
import com.example.restaurapp.databinding.FragmentAddResBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

//TODO https://stackoverflow.com/questions/10024739/how-to-determine-when-fragment-becomes-visible-in-viewpager
class AddResFragment : Fragment() {
    lateinit var app: MyApplication
    private var _binding: FragmentAddResBinding? = null
    var minute: Int? = null
    var hour: Int? = null
    var day: Int? = null
    var month: Int? = null
    var year: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    var uuid = ""


    /*@SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        Toast.makeText(
            activity as MainActivity,
            "visible" ,
            Toast.LENGTH_LONG
        ).show()
        if(_binding != null){
            _binding!!.datePicker.setText("$day-${month!! + 1}-$year")
            _binding!!.timePicker.setText("$hour : $minute")
            var reservation = app.data.getReservation(uuid)
            _binding!!.titleInput.setText(reservation?.title)
            //_binding!!.numberPicker.value = reservation?.restaurantId!!.toInt()
        }

        //INSERT CUSTOM CODE HERE
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddResBinding.inflate(inflater, container, false)
        app = (requireActivity().application as MyApplication)


        _binding!!.numberPicker.displayedValues = app.data.rastaurantNames()
        _binding!!.numberPicker.maxValue = app.data.restaurants.size - 1;


        val bundle = (activity as MainActivity).bundle

        if (!bundle.isEmpty) {

            var check = bundle.getString("uuid")
            var checkRest = bundle.getString("restaurantId")
            (activity as MainActivity).bundle=Bundle();

            if (check != null) {
                uuid = check.toString()
                var reservation = app.data.getReservation(uuid)

                if (reservation != null) {
                    minute = reservation.dateTime.minutes
                    hour = reservation.dateTime.hours
                    day = reservation.dateTime.date
                    month = reservation.dateTime.month
                    year = reservation.dateTime.year

                    _binding!!.datePicker.setText("$day-${month!! + 1}-$year")
                    _binding!!.timePicker.setText("$hour : $minute")
                    _binding!!.titleInput.setText(reservation.title)
                    _binding!!.numberPicker.value = reservation.restaurantId.toInt()
                }
            } else if (checkRest != null) {
                _binding!!.numberPicker.value = checkRest.toString().toInt()
            }
        }

        val dateField = _binding!!.datePicker.setOnClickListener {
            if (year == null && month == null && day == null) {
                val c = Calendar.getInstance()
                year = c.get(Calendar.YEAR)
                month = c.get(Calendar.MONTH)
                day = c.get(Calendar.DAY_OF_MONTH)
            }
            val datePickerDialog = DatePickerDialog(
                activity as MainActivity, { view, year, month, day ->
                    this.year = year
                    this.month = month
                    this.day = day
                    _binding!!.datePicker.setText("$day-${(month + 1)}-$year")

                },
                year!!,
                month!!,
                day!!
            )
            datePickerDialog.show()
        }

        val timeField = _binding!!.timePicker.setOnClickListener {
            if (hour == null && minute == null) {
                val c = Calendar.getInstance()
                hour = c.get(Calendar.HOUR_OF_DAY)
                minute = c.get(Calendar.MINUTE)
            }
            val datePickerDialog = TimePickerDialog(
                activity as MainActivity, { view, hourOfDay, Minute ->
                    this.hour = hourOfDay
                    this.minute = Minute
                    _binding!!.timePicker.setText("$hourOfDay : $Minute")
                },
                hour!!,
                minute!!,
                is24HourFormat(getActivity())
            )
            datePickerDialog.show()
        }

        _binding!!.addButton.setOnClickListener {

            var title = _binding!!.titleInput.text.toString()

            if (title != "" && minute != null && hour != null && day != null && month != null && year != null) {
                if (uuid == "") {
                    val cal = Calendar.getInstance()

                    cal.set(year!!, month!!, day!!, hour!!, minute!!)

                    val ac = activity as MainActivity
                    var  reservartions = Reservation(
                        Date(year!!, month!!, day!!, hour!!, minute!!),
                        title,
                        _binding!!.numberPicker.value.toString(),//app.data.checkRestaurant(restaurant)
                        0
                    )

                    var notId = ac.createNotication(
                        reservartions.uuid,
                        cal,
                        app.data.getRestaurant(_binding!!.numberPicker.value.toString()),
                        title
                    );
                    reservartions.alarmId = notId;

                    app.data.push( reservartions)

                    val bundle = Bundle()
                    app.saveToFile()
                    bundle.putString("result", "created")

                    /*findNavController().navigate(
                        R.id.action_addResFragment_to_reservationsFragment,
                        bundle
                    )*/

                    (activity as MainActivity).bundle = bundle
                    //val manager: FragmentManager = ac.supportFragmentManager
                   //manager.beginTransaction().replace(R.id.nav_host_fragment_activity_main2, fragment , "sometag").commit()

                    (requireActivity().findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                        R.id.reservationsFragment
                } else {

                    val cal = Calendar.getInstance()

                    cal.set(year!!, month!!, day!!, hour!!, minute!!)

                    val ac = activity as MainActivity

                    //deleting notification before updating
                    ac.deleteNotification(app.data.getReservation(uuid)!!.alarmId)

                    var notId = ac.createNotication(
                        uuid,
                        cal,
                        app.data.getRestaurant(_binding!!.numberPicker.value.toString()),
                        title
                    );

                    var succes = app.data.updateReservation(
                        uuid, Reservation(
                            Date(year!!, month!!, day!!, hour!!, minute!!),
                            title,
                            _binding!!.numberPicker.value.toString(),
                            notId
                        )
                    )
                    app.saveToFile()
                    val bundle = Bundle()
                    Toast.makeText(
                        activity as MainActivity,
                        succes.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    bundle.putString("result", succes.toString())
                    /*findNavController().navigate(
                        R.id.action_addResFragment_to_reservationsFragment,
                        bundle
                    )*/
                    //val manager: FragmentManager = ac.supportFragmentManager
                   // manager.beginTransaction().replace(R.id.nav_host_fragment_activity_main2, ReservationsFragment()).commit()
                    (activity as MainActivity).bundle = bundle
                    (requireActivity().findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                        R.id.reservationsFragment
                }


            } else {
                Toast.makeText(
                    activity as MainActivity,
                    "Erron not all fields are filled",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        return _binding!!.root
    }

}