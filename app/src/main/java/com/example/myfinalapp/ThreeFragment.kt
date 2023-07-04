package com.example.myfinalapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinalapp.databinding.ActivityMainBinding
import com.example.myfinalapp.databinding.FragmentThreeBinding
import com.example.myfinalapp.databinding.FragmentTwoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ThreeFragment : Fragment() {
    private lateinit var binding: FragmentThreeBinding

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
        binding = FragmentThreeBinding.inflate(inflater, container, false)

        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try {
                val calRatio = calculateInSampleSize(it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize) )
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio
                var inputStream = requireActivity().contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let{ Log.d("mobileApp", "bitmap NULL")}

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        binding.galleryButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        val profileTextView = binding.root.findViewById<TextView>(R.id.profile)
        val formattedText = MyApplication.email?.let { "${it}님" } ?: ""
        profileTextView.text = formattedText


        //add................................



        return binding.root
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(fileUri)

            // inJustDecodeBounds 값을 true로 설정한 상태에서 decodeXXX()를 호출.
            // 로딩하고자 하는 이미지의 각종 정보가 options에 설정됩니다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 비율 계산...
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        // inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ThreeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThreeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
