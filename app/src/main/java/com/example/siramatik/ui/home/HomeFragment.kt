package com.example.siramatik.ui.home

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.siramatik.MainActivity
import com.example.siramatik.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.logout
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.HashMap

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
          //  textView.text = it
        })
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val MyActivity = activity as MainActivity
        logout.setOnClickListener {
            MyActivity.logOut()
        }
            val rq = Volley.newRequestQueue(activity)
            val url = "http://10.0.2.2:5000/api/bilgilerigetir"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONTokener(response).nextValue() as JSONObject

                        val ad = jsonObject.getString("Ad")
                        val soyad =jsonObject.getString("Soyad")

                        editName.hint =ad +" "+ soyad
                        editName.isEnabled=false

                        val tarih = jsonObject.getString("Tarih")
                        editDateofBirth.hint=tarih
                        editDateofBirth.isEnabled=false

                        val eposta = jsonObject.getString("mail")
                        editEmail.hint=eposta
                        editEmail.isEnabled=false

                    }catch (ex: JSONException) {
                    }
                }, object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        Toast.makeText(activity, volleyError.toString(), Toast.LENGTH_LONG).show()
                    }
                }) {
                override fun getParams(): Map<String, String> {
                    val params: HashMap<String, String> = HashMap()
                    params.put("Id", MyActivity.getMyId())
                    return params
                }
            }
            rq.add(stringRequest)
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}