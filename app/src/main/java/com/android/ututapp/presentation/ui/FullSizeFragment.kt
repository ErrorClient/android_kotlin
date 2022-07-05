package com.android.ututapp.presentation.ui

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.ututapp.R
import com.android.ututapp.databinding.FragmentFullSizeBinding
import com.bumptech.glide.Glide

class FullSizeFragment : Fragment() {

    private var _binding: FragmentFullSizeBinding? = null
    private val binding get() = _binding!!
    private var urlImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater
            .from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFullSizeBinding.inflate(inflater, container, false)
        arguments?.let {
            urlImage = it.getString("Image").toString()
        }

        Glide.with(this)
            .load(urlImage)
            .error(R.drawable.empty)
            .into(binding.fullSizeImage)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}