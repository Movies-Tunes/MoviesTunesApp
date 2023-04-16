package com.myapplication.ui.searchsmovie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myapplication.databinding.FragmentSearchMovieBinding

class SearchMovieFragment : Fragment() {
    private val _binding: FragmentSearchMovieBinding by lazy {
        FragmentSearchMovieBinding.inflate(layoutInflater)
    }
    val binding: FragmentSearchMovieBinding = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
