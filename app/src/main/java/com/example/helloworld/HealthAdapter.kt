package com.example.helloworld

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HealthAdapter(private val context: Context) :
    RecyclerView.Adapter<HealthAdapter.ViewHolder>() {

    var data = mutableListOf<FoodData>()
    private var listener: OnItemClickListener? = null  // for click event

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_food_data, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: FoodData, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val foodName: TextView = itemView.findViewById(R.id.rv_foodname)
        private val foodCalories: TextView = itemView.findViewById(R.id.rv_calories)
        private val foodCarbohydrate: TextView = itemView.findViewById(R.id.rv_carbohydrate)
        private val foodProtein: TextView = itemView.findViewById(R.id.rv_protein)
        private val foodFat: TextView = itemView.findViewById(R.id.rv_fat)
        private val foodSugars: TextView = itemView.findViewById(R.id.rv_sugars)
        private val foodNatrium: TextView = itemView.findViewById(R.id.rv_natrium)

        fun bind(foodItem: FoodData) {
            foodName.text = foodItem.name
            if (foodItem.NUTR_CONT1 == null) {
                foodCalories.text = "데이터 없음"
            } else {
                foodCalories.text = foodItem.NUTR_CONT1.toString() + "(kcal)"
                foodCarbohydrate.text = foodItem.NUTR_CONT2.toString() + "(g)"
                foodProtein.text = foodItem.NUTR_CONT3.toString() + "(g)"
                foodFat.text = foodItem.NUTR_CONT4.toString() + "(g)"
                foodSugars.text = foodItem.NUTR_CONT5.toString() + "(g)"
                foodNatrium.text = foodItem.NUTR_CONT6.toString() + "(mg)"
            }

            // click listener
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, foodItem, pos)
                }
            }
        }
    }
}