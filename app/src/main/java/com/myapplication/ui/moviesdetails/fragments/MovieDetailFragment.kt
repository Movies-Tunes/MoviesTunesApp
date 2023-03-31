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
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
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
                    .into(binding.ivPoster)
            }
            binding.detailToolbar.title = title
            /*TODO binding other fields*/
        }
    }

    private fun setInitialVisibleStates() {
        binding.tvDescriptionSinopse.visibility = View.GONE
        binding.pbLoadingDetails.visibility = View.VISIBLE
    }

    private fun setNavigationToolbar() {
        binding.detailToolbar.setNavigationIcon(R.drawable.ic_back)
        binding.detailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
