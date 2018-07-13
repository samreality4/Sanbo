package com.sxg.sam.sanbo

import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.graphics.Movie
import android.view.ViewGroup
import android.view.LayoutInflater


class MovieAdapter(private val mContext: Context, private val mData: List<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater
    var listener: CustomItemClickListener? = null
    internal var TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"


    interface CustomItemClickListener {
        fun onItemClick(v: View, position: Int)
    }


    init {
        this.listener = mContext
        inflater = LayoutInflater.from(mContext)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.post_pix, parent, false)
        val myHolder = MyHolder(view)
        view.setOnClickListener(object : View.OnClickListener() {
            fun onClick(v: View) {
                if (listener != null) {
                    listener!!.onItemClick(v, myHolder.adapterPosition)

                }
            }
        })
        return myHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyHolder
        val current = mData[position]


        Picasso.with(mContext)
                .load(TMDB_POSTER_BASE_URL + current.getPosterPath())
                .resize(185, 278)
                .error(R.drawable.failure)
                .placeholder(R.drawable.loading)
                .into(myHolder.moviePoster)

    }

    override fun getItemCount(): Int {
        return mData.size
    }


    internal inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieTitle: TextView
        var overView: TextView
        var voteAverage: TextView
        var releaseDate: TextView
        var moviePoster: ImageView
        var moviePoster1: ImageView


        init {
            movieTitle = itemView.findViewById(R.id.original_title)
            overView = itemView.findViewById(R.id.overview)
            releaseDate = itemView.findViewById(R.id.release_date)
            voteAverage = itemView.findViewById(R.id.vote_average)
            moviePoster = itemView.findViewById(R.id.left_image1) as ImageView
            moviePoster1 = itemView.findViewById(R.id.poster) as ImageView


        }
    }

}