package com.driff.android.module.ui.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.driff.android.module.databinding.FragmentPicturesBinding
import com.driff.android.module.ui.model.PicturesCatalogUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.picturesCatalogUiStateFlow
                        .filter { it.isLoading }
                        .collect(::onLoading)
                }
                launch {
                    viewModel.picturesCatalogUiStateFlow
                        .filter { !it.isLoading && it.errorMessage.isNullOrEmpty() }
                        .collect(::onSuccess)
                }

            }
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.getPictures(false)
        binding.btnRefresh.setOnClickListener {
            viewModel.getPictures(true)
        }
    }

    private fun onLoading(state: PicturesCatalogUiState) {

    }

    private fun onSuccess(state: PicturesCatalogUiState) {
        binding.imageView.setImageBitmap(state.imageItem.image)
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