package com.example.siramatik.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.siramatik.MainActivity
import com.example.siramatik.databinding.FragmentDashboardBinding
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONException
import java.util.HashMap


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root



       // val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val MyActivity = activity as MainActivity
        txt.text = null
        btn.setOnClickListener {
            val rq = Volley.newRequestQueue(activity)
            val url = "http://10.0.2.2:5000/api"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener{ response ->
                    try {
                        val strRes = response.toString()
                        if(strRes.equals("false"))
                            Toast.makeText(activity, "GÜNLÜK SIRA ALMA LİMİTİNİZ DOLMUŞTUR", Toast.LENGTH_LONG).show()
                        else
                            txt.text = strRes.substring(15, 20)

                    } catch (ex: JSONException) {
                    }
                }, object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        Toast.makeText(activity, volleyError.toString(), Toast.LENGTH_LONG).show()

                    }
                })
            {
                override fun getParams(): Map<String, String> {
                    val params: HashMap<String, String> = HashMap()
                    params.put("Id",MyActivity.getMyId())
                    return params
                }
            }
            rq.add(stringRequest)
        }
        logout.setOnClickListener{
            MyActivity.logOut()
        }
        txt2.setOnClickListener{
            if (txt.text != null) {
                val rq1 = Volley.newRequestQueue(activity)
                val url1 = "http://10.0.2.2:5000/api/bekleyensayisigoster"
                val stringRequest1 =object :  StringRequest(
                    Method.POST, url1,
                    Response.Listener{ response1 ->
                        try {
                            val strRes1 = response1.toString()
                            Toast.makeText(
                                activity,
                                "Sıranızın gelmesine son" + strRes1.substring(15, 20) + "kişi",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (ex: JSONException) {
                        }
                    }, object : Response.ErrorListener {
                        override fun onErrorResponse(volleyError: VolleyError) {
                            Toast.makeText(activity, volleyError.toString(), Toast.LENGTH_LONG).show()

                        }
                    })
                {
                    override fun getParams(): Map<String, String> {
                        val params: HashMap<String, String> = HashMap()
                        params.put("Id", MyActivity.getMyId())
                        return params
                    }
                }
                rq1.add(stringRequest1)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}