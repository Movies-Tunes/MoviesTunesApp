package com.myapplication.ui.moviesdetails.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myapplication.R
import com.myapplication.core.Constants
import com.myapplication.databinding.FragmentMovieDetailBinding
import com.myapplication.util.extension.concatParam
import com.squareup.picasso.Picasso

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

        setMovieDetailInView()
        setInitialVisibleStates()
        setNavigationToolbar()
    }

    private fun setMovieDetailInView() {
        args.let { safeArguments ->
            val rated = safeArguments.topRated
            val id = rated?.id
            val posterPath = rated?.posterPath
            val title = rated?.title
            val result = "$id $title $posterPath"
            posterPath?.let { safePath ->
                Picasso.get()
                    .load(Constants.BASE_POSTER_PATH.concatParam(safePath))
                    .into(_binding.ivPoster)
            }
            _binding.detailToolbar.title = title
            /*TODO binding other fields*/
        }
    }

    private fun setInitialVisibleStates() {
        _binding.tvDescriptionSinopse.visibility = View.GONE
        _binding.pbLoadingDetails.visibility = View.VISIBLE
    }

    private fun setNavigationToolbar() {
        _binding.detailToolbar.setNavigationIcon(R.drawable.ic_back)
        _binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
