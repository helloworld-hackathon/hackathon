package com.example.helloworld

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_nutritional.*

class NutritionalFragment : Fragment() {

    lateinit var searchbtn: ImageButton
    private lateinit var healthAdapter: HealthAdapter
    private lateinit var input: String
    private lateinit var foodList: MutableList<FoodData>
    private lateinit var recyclerview_foodList: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        return inflater.inflate(R.layout.fragment_nutritional, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         healthAdapter = HealthAdapter(requireContext())

        recyclerview_foodList = view.findViewById(R.id.rv_foodList)
        recyclerview_foodList.adapter = healthAdapter


        // clicked search button
        searchbtn = view.findViewById(R.id.btn_searchFood)
        searchbtn.setOnClickListener {
            input = et_inputFood.text.toString()

            val thread = ApiFoodInfo(input)
            thread.start()
            thread.join()
            foodList = thread.foodList
            healthAdapter.data = foodList
            healthAdapter.notifyDataSetChanged()

            et_inputFood.setText("")
        }
    }


    }