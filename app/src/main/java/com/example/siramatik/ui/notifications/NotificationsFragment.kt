package com.example.siramatik.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.siramatik.MainActivity
import com.example.siramatik.R
import com.example.siramatik.databinding.FragmentNotificationsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.logout
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    private lateinit var mMap: GoogleMap

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, {
           // textView.text = it
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myActivity = activity as MainActivity

        logout.setOnClickListener{
            myActivity.logOut()
        }
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

    }
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val uni = LatLng(40.997472,28.696965)
        mMap.addMarker(MarkerOptions().position(uni).title("İSTANBUL GELİŞİM ÜNİVERSİTESİ"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uni,15f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}