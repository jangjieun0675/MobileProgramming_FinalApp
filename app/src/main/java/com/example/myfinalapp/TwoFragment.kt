package com.example.myfinalapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myfinalapp.databinding.FragmentTwoBinding
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TwoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentTwoBinding.inflate(inflater, container, false)
        var mutableList: MutableList<MyItem>

        binding.btnSearch.setOnClickListener {
            var keyword = binding.edtProduct.text.toString()
            val call: Call<MyModel> = MyApplication.networkService.getList(
                keyword,
                "UKkAe8jvrkN/T9mQ2kldttyWkMq2zV5+i8RNss/8TvkCb95RyVfgHgxMq4MUt4BcyXNoz5y46QkuGeKclDfLSA==",
                1,
                3,
                "json"
            )
            Log.d("mobileApp","${call.request()}")

            call?.enqueue(object : Callback<MyModel> {
                override fun onResponse(call: retrofit2.Call<MyModel>, response: Response<MyModel>) {
                    if(response.isSuccessful){
                        Log.d("mobileApp","${response.body()}")
                        binding.retrofitRecyclerView.layoutManager = LinearLayoutManager(context)
                        binding.retrofitRecyclerView.adapter = MyRetrofitAdapter(requireContext(), response.body()!!.body.items)
                    }
                }

                override fun onFailure(call: retrofit2.Call<MyModel>, t: Throwable) {
                    Log.d("mobileApp","${t.toString()}")
                }
            })

        }

        mutableList = mutableListOf<MyItem>()
        binding.retrofitRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.retrofitRecyclerView.adapter = MyRetrofitAdapter(requireContext(), mutableList)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TwoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}