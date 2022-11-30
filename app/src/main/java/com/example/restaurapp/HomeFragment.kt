package com.example.restaurapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.restaurapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        _binding!!.addReservation2.setOnClickListener(){

            findNavController().navigate(R.id.action_homeFragment_to_addResFragment)
        }

        _binding!!.reservations.setOnClickListener(){
            findNavController().navigate(R.id.action_homeFragment_to_reservationsFragment)
        }

        return _binding!!.root
    }

}