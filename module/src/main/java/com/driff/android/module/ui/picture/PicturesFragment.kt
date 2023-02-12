package com.driff.android.module.ui.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.driff.android.module.databinding.FragmentPicturesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [PicturesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PicturesFragment : Fragment() {

    private lateinit var binding: FragmentPicturesBinding
    private val viewModel: PicturesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentPicturesBinding.inflate(inflater, container, false).apply {
            binding = this
        }.root
    }

    override fun onStart() {
        super.onStart()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PicturesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = PicturesFragment()
    }
}