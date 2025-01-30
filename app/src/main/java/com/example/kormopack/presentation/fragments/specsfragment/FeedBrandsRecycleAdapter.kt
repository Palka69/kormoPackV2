package com.example.kormopack.presentation.fragments.specsfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kormopack.R
import com.example.kormopack.domain.specsfrag.model.SpecificationBrandModel

class FeedBrandsRecycleAdapter(
    private val context: Context,
    list: List<SpecificationBrandModel>,
    private val onBrandClickListener: OnBrandClickListener
): RecyclerView.Adapter<FeedBrandsRecycleAdapter.ViewHolder>() {

    interface OnBrandClickListener {
        fun onBrandClick(text: String)
    }

    private val layoutInflater: LayoutInflater
    private var list: List<SpecificationBrandModel>

    init {
        layoutInflater = LayoutInflater.from(context)
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.feed_brand_recycle_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thatFeedBrandCard = list[position]

        holder.name.text = thatFeedBrandCard.name

        val tempCardColor = when (thatFeedBrandCard.cardBackColor) {
            1 -> ContextCompat.getColor(context, R.color.kormoTech_dark_orange)
            2 -> ContextCompat.getColor(context, R.color.myau_red)
            3 -> ContextCompat.getColor(context, R.color.opti_green)
            4 -> ContextCompat.getColor(context, R.color.smart_choice_blue)
            else -> ContextCompat.getColor(context, R.color.white)
        }
        holder.card.setCardBackgroundColor(tempCardColor)

        val tempTextColor = when (thatFeedBrandCard.textColor) {
            0 -> ContextCompat.getColor(context, R.color.white)
            1 -> ContextCompat.getColor(context, R.color.sjobogarden_blue)
            else -> ContextCompat.getColor(context, R.color.black)
        }
        holder.name.setTextColor(tempTextColor)

        val tempDrawable = when (thatFeedBrandCard.brandLogo) {
            1 -> ContextCompat.getDrawable(context, R.drawable.c4p_logo)
            2 -> ContextCompat.getDrawable(context, R.drawable.myau_logo)
            3 -> ContextCompat.getDrawable(context, R.drawable.opti_meal_white_logo)
            4 -> ContextCompat.getDrawable(context, R.drawable.smart_choice_white_logo)
            5 -> ContextCompat.getDrawable(context, R.drawable.sjobogarden_logo)
            else -> ContextCompat.getDrawable(context, R.drawable.kormo_pets_paw)
        }
        holder.image.setImageDrawable(tempDrawable)

        holder.card.setOnClickListener {
            onBrandClickListener.onBrandClick(holder.name.text as String)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card: CardView
        var name: TextView
        var image: ImageView

        init {
            card = itemView.findViewById(R.id.card_view)
            name = itemView.findViewById(R.id.text_view)
            image = itemView.findViewById(R.id.image_view)
        }
    }

    fun updateData(newData: List<SpecificationBrandModel>) {
        this.list = newData
        notifyDataSetChanged()
    }
}