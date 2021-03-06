package com.example.projedeneme.MapActivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projedeneme.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_selected_request.*

val database = Firebase.database
val latitudeFrom = database.getReference("latitudeFrom")
val latitudeTo = database.getReference("latitudeTo")
val longitudeFrom = database.getReference("longitudeFrom")
val longitudeTo = database.getReference("longitudeTo")

interface MyInterface {
    fun onCallback(response: Double)
}

class PolyActivity : AppCompatActivity(), OnMapReadyCallback, OnPolylineClickListener,
    MyInterface {
    var latFrom = 3.0
    var lonFrom = 3.0
    var latTo = 3.0
    var lonTo = 3.0
    val myInterface = this
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.e("Log", "6  " + latFrom)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_request)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        var newIntent = intent
        if (intent!=null){

            textView11.text=newIntent.getStringExtra("destination")
            textView13.text=newIntent.getStringExtra("from")
            textView15.text=newIntent.getStringExtra("date")
            textView17.text=newIntent.getStringExtra("time")

        }
        var ref = FirebaseDatabase.getInstance().reference
        var user= newIntent.getStringExtra("userid")

        var sorgu=ref.child("latlong")
            .orderByKey()
            .equalTo(user)

        sorgu.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (singleSnapshot in p0!!.children){
                    var tempLatLong=singleSnapshot.getValue(LatLong::class.java)
                    latFrom=tempLatLong?.latFrom!!.toDouble()
                    lonFrom=tempLatLong?.longFrom!!.toDouble()
                    latTo=tempLatLong?.latTo!!.toDouble()
                    lonTo=tempLatLong?.longTo!!.toDouble()

                }

            }

        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e("Log", "5 altı  " + latTo)
        buttonDraw.setOnClickListener {
            val polyline1 = googleMap.addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .add(
                        LatLng(latFrom, lonFrom),
                        LatLng(latTo, lonTo)
                    )
            )
            Log.e(
                "Log",
                "latto " + latTo + "latFrom " + latFrom + "lonfrom " + lonFrom + "lonto " + lonTo
            )
            polyline1.tag = "A"
            stylePolyline(polyline1)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latFrom, lonFrom), 5f))
            googleMap.setOnPolylineClickListener(this)
        }
    }

    private val COLOR_BLACK_ARGB = -0x1000000
    private val POLYLINE_STROKE_WIDTH_PX = 12
    private fun stylePolyline(polyline: Polyline) {
        val type = polyline.tag?.toString() ?: ""
        when (type) {
            "A" -> {
                polyline.endCap = CustomCap(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 16f
                )
            }
        }
        polyline.startCap = RoundCap()
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = COLOR_BLACK_ARGB
        polyline.jointType = JointType.ROUND
    }

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)
    override fun onPolylineClick(polyline: Polyline) {

        if (polyline.pattern == null || !polyline.pattern!!.contains(DOT)) {
            polyline.pattern = PATTERN_POLYLINE_DOTTED
        } else {
            polyline.pattern = null
        }
        Toast.makeText(
            this, "Route type " + polyline.tag.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onCallback(response: Double) {
        latFrom = response
    }
}