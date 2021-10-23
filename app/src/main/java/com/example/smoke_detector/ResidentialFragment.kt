package com.example.smoke_detector

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smoke_detector.databinding.FragmentResidentialBinding
import com.google.firebase.database.*


class ResidentialFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentResidentialBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private val TAG = javaClass.simpleName


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        dataGet()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        dataGet()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataGet()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResidentialBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun dataGet() {
        database = FirebaseDatabase.getInstance().reference.child("light").child("燈號")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val light_1 = dataSnapshot.child("R1").value.toString().toInt()
                val light_2 = dataSnapshot.child("R2").value.toString().toInt()
                val light_3 = dataSnapshot.child("R3").value.toString().toInt()
                val light_4 = dataSnapshot.child("R4").value.toString().toInt()
                val light_5 = dataSnapshot.child("R5").value.toString().toInt()
                val light_6 = dataSnapshot.child("R6").value.toString().toInt()
                val light_7 = dataSnapshot.child("R7").value.toString().toInt()
                val light_8 = dataSnapshot.child("R8").value.toString().toInt()
                val light_9 = dataSnapshot.child("R9").value.toString().toInt()

                if (light_1 == 1) {
                    binding.circle1.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle1.setImageResource(R.drawable.gray_circle)
                }

                if (light_2 == 1) {
                    binding.circle2.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle2.setImageResource(R.drawable.gray_circle)
                }

                if (light_3 == 1) {
                    binding.circle3.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle3.setImageResource(R.drawable.gray_circle)
                }

                if (light_4 == 1) {
                    binding.circle4.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle4.setImageResource(R.drawable.gray_circle)
                }

                if (light_5 == 1) {
                    binding.circleCenter.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circleCenter.setImageResource(R.drawable.gray_circle)
                }

                if (light_6 == 1) {
                    binding.circle6.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle6.setImageResource(R.drawable.gray_circle)
                }

                if (light_7 == 1) {
                    binding.circle7.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle7.setImageResource(R.drawable.gray_circle)
                }

                if (light_8 == 1) {
                    binding.circle8.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle8.setImageResource(R.drawable.gray_circle)
                }

                if (light_9 == 1) {
                    binding.circle9.setImageResource(R.drawable.red_circle)
                }else{
                    binding.circle9.setImageResource(R.drawable.gray_circle)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
    }



    companion object {

    }


}

