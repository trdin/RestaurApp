package com.example.restaurapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class ReservationsFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ReservationsAdapter
    lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservations, container, false)
        app = (requireActivity().application as MyApplication)

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.layoutManager = LinearLayoutManager(view.context);



        adapter = ReservationsAdapter(app.data, object : ReservationsAdapter.MyOnClick {
            override fun onClick(p0: View?, position: Int) {
                val bundle = Bundle()
                bundle.putString("uuid", app.data.reservartions[position].uuid.toString())
                (activity as MainActivity).bundle = bundle
                (requireActivity().findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                    R.id.addResFragment
                /*findNavController().navigate(
                    R.id.action_reservationsFragment_to_addResFragment,
                    bundle
                )*/
            }
        })

        val bundle = (activity as MainActivity).bundle

        if (bundle != null) {

            if (!bundle.isEmpty) {

                if(bundle.getString("result").toString() == "created"){
                    Toast.makeText(
                        activity as MainActivity,
                        "Reservation Created",
                        Toast.LENGTH_LONG
                    ).show()
                    adapter.notifyDataSetChanged()
                }else{
                    if(bundle.getString("result").toString() == "true" ){
                        Toast.makeText(
                            activity as MainActivity,
                            "Reservation Updated",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            activity as MainActivity,
                            "Not found",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    adapter.notifyDataSetChanged()
                }
            }
        }

        recyclerView.adapter = adapter;
        adapter.onLongClickObject = object : ReservationsAdapter.MyOnClick {
            override fun onClick(p0: View?, position: Int) {
                val builder =
                    this@ReservationsFragment.context?.let { AlertDialog.Builder(it) } //access context from inner class
                //set title for alert dialog
                if (builder != null) {
                    builder.setTitle("Delete")
                    builder.setMessage("Are you sure you want to delete ${app.data.reservartions[position].title}")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton("Yes") { dialogInterface, which -> //performing positive action
                        Toast.makeText(
                            app,
                            "You deleted ${app.data.reservartions[position].title}",
                            Toast.LENGTH_LONG
                        ).show()
                        //deleting notification
                        (activity as MainActivity).deleteNotification( app.data.reservartions[position].alarmId)
                        app.data.reservartions.removeAt(position)
                        adapter.notifyDataSetChanged()
                        app.saveToFile()
                    }
                    builder.setNeutralButton("Cancel") { dialogInterface, which -> //performing cancel action
                        Toast.makeText(app, "You canceled the deletion.", Toast.LENGTH_LONG).show()
                    }

                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }

            }
        }

        return view
    }
}