package com.example.restaurapp.location
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.restaurapp.MainActivity
import com.example.restaurapp.R
import com.example.restaurapp.databinding.FragmentMapsBinding
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MyMarker(mapView: MapView?) : Marker(mapView) {

    lateinit var fragment : Fragment
    //lateinit var activity: MainActivity
    var id: Int = 0

    override fun onLongPress( event: MotionEvent, mapView: MapView): Boolean {
        // TODO spremeni ikono tistim ki imajo restavracije  in nato ureja prvo rezervaicjo

        if(super.onLongPress(event, mapView)){
            val bundle = Bundle()
            bundle.putString("restaurantId", id.toString())
            fragment.findNavController().navigate(R.id.addResFragment, bundle)
           // activity.findNavController(R.id.nav_host_fragment_activity_main2).navigate(R.id.addResFragment, bundle)

        }
        //Log.d("TAG", "onLongPress: "+ super.onLongPress(event, mapView)+" " + title)
        return super.onLongPress(event, mapView)
    }
/*
    override fun onDoubleTap(e: MotionEvent?, mapView: MapView?): Boolean {
        // TODO
        Log.d("DOUble tap", "onLongPress: "+ super.onDoubleTap(e, mapView)+" " + title  )
        return super.onDoubleTap(e, mapView)
    }*/
}