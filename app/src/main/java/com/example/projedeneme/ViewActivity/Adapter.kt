package com.example.projedeneme.ViewActivity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.projedeneme.MapActivity.PolyActivity
import com.example.projedeneme.R
import com.example.projedeneme.RequestActivity.Ride
import kotlinx.android.synthetic.main.ride_line_layout.view.*


class Adapter( rideListtemp:List<Ride>) : RecyclerView.Adapter<Adapter.RideViewHolder>(){
    var rideList =rideListtemp

    override fun getItemCount(): Int {
       return rideList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        var inflater= LayoutInflater.from(parent?.context)
        var rideLine=inflater.inflate(R.layout.ride_line_layout,parent,false)

        return RideViewHolder(
            rideLine
        )
    }



    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {

        var newRide=rideList.get(position)
        holder?.setData(newRide,position)

    /*   holder?.rideDestination?.text=rideList.get(position).destination
        holder?.rideFrom?.text=rideList.get(position).from
        holder?.rideDate?.text=rideList.get(position).date
        holder?.rideTime?.text=rideList.get(position).time*/


    }

    class RideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
            var rideLine = itemView as CardView
            var rideDestination=rideLine.destination_text
            var rideFrom=rideLine.from_text
            var rideDate=rideLine.date_text
            var rideTime= rideLine.time_text

        fun setData(CreatedRide : Ride, position: Int){
            rideDestination.text=CreatedRide.destination
            rideFrom.text=CreatedRide.from
            rideDate.text=CreatedRide.date
            rideTime.text=CreatedRide.time


            rideLine.setOnClickListener {v ->
                var intent =Intent(v.context,
                    PolyActivity::class.java)
                intent.putExtra("userid",CreatedRide.userId)
                intent.putExtra("destination",CreatedRide.destination)
                intent.putExtra("from",CreatedRide.from)
                intent.putExtra("date",CreatedRide.date)
                intent.putExtra("time",CreatedRide.time)
                v.context.startActivity(intent)


                   }

            }


        }

}