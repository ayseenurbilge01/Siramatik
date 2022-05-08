package com.example.siramatik.ui.dashboard

import android.content.Context.LOCATION_SERVICE
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.siramatik.MainActivity
import com.example.siramatik.databinding.FragmentDashboardBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.logout
import org.json.JSONException
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class DashboardFragment : Fragment(), OnMapReadyCallback {
    private var kontrol: Boolean = false
    private var lat1: Double = 0.0
    private var lon1: Double = 0.0
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private var runnable : Runnable = Runnable {  }
    private var handler = Handler(Looper.myLooper()!!)
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

       // val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, {
            //textView.text = it

        })
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val MyActivity1 = activity as MainActivity
        showSequenceNumber()//sıra numarası gösterme


        btn.setOnClickListener {

            if(distanceInMeter(lat1,lon1,40.997472,28.696965)>30000000)
            {
                Toast.makeText(activity, "Sıra alabilmek için fazla uzaktasınız.", Toast.LENGTH_LONG).show()
            }
            else
            {
                getSequenceNumber()//sıra numarası alma ve gösterme
                start()

            }

        }
        logout.setOnClickListener{
            MyActivity1.logOut()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val myActivity1 = activity as MainActivity

        var locationManager = myActivity1.getSystemService(LOCATION_SERVICE) as LocationManager
        var locationListener = LocationListener { location -> //Konum Değişince Yapılacak İşlemler
            lat1 = location.latitude
            lon1 = location.longitude

            mMap.clear()
            val guncelKonum = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(guncelKonum).title("Konumunuz"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(guncelKonum, 15f))
        }

    }

    private fun showSequenceNumber()
    {
        val MyActivity1 = activity as MainActivity
        val rq = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2:5000/api/siranogetir"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener{ response ->
                try {
                    val strRes = response.toString()
                    if(strRes!="null")
                        txt.text = strRes.substring(15, 20)

                } catch (ex: JSONException) {
                }
            },
            Response.ErrorListener { volleyError -> Toast.makeText(activity, volleyError.toString(), Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): Map<String, String> {
                val params: HashMap<String, String> = HashMap()
                params["Id"] = MyActivity1.getMyId()
                return params
            }
        }
        rq.add(stringRequest)
    }
    private fun getSequenceNumber()
    {
        val MyActivity1 = activity as MainActivity
        val rq = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2:5000/api"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener{ response ->
                try {
                    val strRes = response.toString()
                    if(strRes == "false")
                        Toast.makeText(activity, "GÜNLÜK SIRA ALMA LİMİTİNİZ DOLMUŞTUR", Toast.LENGTH_LONG).show()
                    else
                    {
                        txt.text = strRes.substring(15, 20)
                        kontrol = true
                    }


                } catch (ex: JSONException) {
                }
            },
            Response.ErrorListener { volleyError -> Toast.makeText(activity, volleyError.toString(), Toast.LENGTH_LONG).show() })
        {
            override fun getParams(): Map<String, String> {
                val params: HashMap<String, String> = HashMap()
                params["Id"] = MyActivity1.getMyId()
                return params
            }
        }
        rq.add(stringRequest)


    }
    private fun waitingNumber()
    {
        val MyActivity1 = activity as MainActivity
            val rq1 = Volley.newRequestQueue(activity)
            val url1 = "http://10.0.2.2:5001/apii/bekleyensayisigoster"
            val stringRequest1 =object :  StringRequest(
                Method.POST, url1,
                Response.Listener{ response1 ->
                    try {
                        val strRes1 = response1.toString()
                        val number = strRes1.substring(4,5).toInt()
                        if(number == 3)
                        {
                            stop()
                            MyActivity1.sendNotification()
                        }
                    } catch (ex: JSONException) {
                    }
                },
                Response.ErrorListener { volleyError -> Toast.makeText(activity, volleyError.toString(), Toast.LENGTH_LONG).show() })
            {
                override fun getParams(): Map<String, String> {
                    val params: HashMap<String, String> = HashMap()
                    params["Id"] = MyActivity1.getMyId()
                    return params
                }
            }
            rq1.add(stringRequest1)

    }

    private fun start(){
        runnable = Runnable {
            waitingNumber()
            handler.postDelayed(runnable,5000)
        }
        handler.post(runnable)
    }

    private fun stop(){
        handler.removeCallbacks(runnable)
    }
    private fun distanceInMeter(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371000.0 // Radius of the earth in m
        val dLat = (lat1 - lat2) * Math.PI / 180f
        val dLon = (lon1 - lon2) * Math.PI / 180f
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1 * Math.PI / 180f) * cos(
            lat2 * Math.PI / 180f
        ) *
                sin(dLon / 2) * sin(dLon / 2)
        val c =
            2f * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }


}