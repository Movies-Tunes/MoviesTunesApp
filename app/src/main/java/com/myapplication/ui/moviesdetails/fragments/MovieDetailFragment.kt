package com.myapplication.ui.moviesdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.myapplication.databinding.FragmentMovieDetailBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {
    private lateinit var _binding: FragmentMovieDetailBinding
    val binding: FragmentMovieDetailBinding get() = _binding
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.let { safeArguments ->
            val rated = safeArguments.topRated
            val id = rated?.id
            val posterPath = rated?.posterPath
            val title = rated?.title
            val result = "$id $title $posterPath"
            _binding.tvTest.text = result
        }
    }
}
