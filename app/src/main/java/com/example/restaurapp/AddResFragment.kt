package com.example.restaurapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restaurapp.databinding.FragmentAddResBinding
import android.text.format.DateFormat;
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.reservartions.Reservation
import timber.log.Timber
import java.util.*

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddResBinding.inflate(inflater, container, false)
        app = (requireActivity().application as MyApplication)


        _binding!!.numberPicker.displayedValues = app.data.rastaurantNames()
        _binding!!.numberPicker.maxValue = app.data.restaurants.size - 1;

        val bundle = this.arguments
        if (bundle != null) {
            if (!bundle.isEmpty) {
                uuid = bundle.getString("uuid").toString()
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
                    app.data.push(
                        Reservation(
                            Date(year!!, month!!, day!!, hour!!, minute!!),
                            title,
                            _binding!!.numberPicker.value.toString()//app.data.checkRestaurant(restaurant)
                        )

                    )
                    val bundle = Bundle()
                    app.saveToFile()
                    bundle.putString("result", "created")
                    findNavController().navigate(
                        R.id.action_addResFragment_to_reservationsFragment,
                        bundle
                    )
                } else {
                    var succes = app.data.updateReservation(
                        uuid, Reservation(
                            Date(year!!, month!!, day!!, hour!!, minute!!),
                            title,
                            _binding!!.numberPicker.value.toString()
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
                    findNavController().navigate(
                        R.id.action_addResFragment_to_reservationsFragment,
                        bundle
                    )
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