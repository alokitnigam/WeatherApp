package com.example.weatherapp.Views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.DI.Models.Data
import com.example.weatherapp.R
import com.example.weatherapp.Utils.Utilities
import com.example.weatherapp.Utils.Utilities.Companion.getDate
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ImageViewHolder>() {
    private var list: ArrayList<Data> = ArrayList()
    lateinit var onItemDeleteListener: OnItemClickedListener

    fun setListener(onItemDeleteListener: OnItemClickedListener){
        this.onItemDeleteListener = onItemDeleteListener
    }

    fun setList(list: List<Data>)
    {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ImageViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Data) {
            val df = DecimalFormat("#.##")
            val formattedtemp = df.format(Utilities.kelvinToCelsius(data.main.temp))

            date.text = getDate(data.dt.toLong())
            weather_image.setImageDrawable(ContextCompat.getDrawable(itemView.context,Utilities.getWeatherDrawable(data.weather[0].main)))
            temp.text = "$formattedtemp °"





        }

        private val temp: TextView = itemView.findViewById(R.id.temp)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val weather_image: ImageView = itemView.findViewById(R.id.weather_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)


        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(list.get(position));
        if(position == 0){
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE)

        }

    }


    interface OnItemClickedListener{
        fun onItemClicked(data: Data)
    }



}

