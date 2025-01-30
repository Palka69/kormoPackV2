package com.example.kormopack.presentation.fragments.feedsfrag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kormopack.R
import com.example.kormopack.domain.specsfrag.model.SpecificationFeedModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class FeedRecycleAdapter (
    private val context: Context,
    list: MutableList<SpecificationFeedModel>,
): RecyclerView.Adapter<FeedRecycleAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater
    var list: MutableList<SpecificationFeedModel> = list
        private set

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedRecycleAdapter.ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.feed_recycle_adapter, parent, false)
        return FeedRecycleAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedSpec = list[position]

        feedSpec.recipe_num

        holder.numtxt.text = feedSpec.spec_num
        holder.matrixtxt.text = feedSpec.matrix_type

        when (holder.matrixtxt.text) {
            "ВК-1" -> holder.matrixImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bk_1_matrix))
            "ВК-2" -> holder.matrixImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bk_2_matrix))
            "ВК-4" -> holder.matrixImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bk_3_matrix))
        }

        holder.nametxt.text = feedSpec.feed_name

        var itProp = "${feedSpec.pieces_perc}%"
        if (feedSpec.addition_one_perc != 0) {
            itProp += " | ${feedSpec.addition_one_perc}%"
        }
        if (feedSpec.addition_two_perc != 0) {
            itProp += " | ${feedSpec.addition_two_perc}%"
        }
        itProp += " | ${feedSpec.sauce_perc}%"
        holder.proport.text = itProp

        holder.regtxt.text = feedSpec.reg_data

        holder.additionalRegContainer.removeAllViews()

        holder.chart.centerText = "${feedSpec.total_weight}г"
        holder.chart.setCenterTextSize(48f)
        holder.chart.isRotationEnabled = false

        holder.chart.holeRadius = 44f
        holder.chart.transparentCircleRadius = 48f
        val piecesWeight = feedSpec.total_weight * (feedSpec.pieces_perc.toFloat() / 100)
        val additionOneWeight = feedSpec.total_weight * (feedSpec.addition_one_perc.toFloat() / 100)
        val sauceWeight = feedSpec.total_weight * (feedSpec.sauce_perc.toFloat() / 100)
        val additionTwoWeight = feedSpec.total_weight * (feedSpec.addition_two_perc.toFloat() / 100)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(piecesWeight, "Шматочки"))
        if (feedSpec.addition_one_perc > 0) {
            entries.add(PieEntry(additionOneWeight, "1 Добавка"))
        }
        entries.add(PieEntry(sauceWeight, "Соус"))
        if (feedSpec.addition_two_perc > 0) {
            entries.add(PieEntry(additionTwoWeight, "2 Добавка"))
        }

        val dataSet = PieDataSet(entries, null)
        dataSet.colors = listOf(
            ContextCompat.getColor(context, R.color.kormotech_light_blue),
            ContextCompat.getColor(context, R.color.kormotech_green),
            ContextCompat.getColor(context, R.color.kormoTech_light_orange),
            ContextCompat.getColor(context, R.color.kormoTech_dark_orange),
        )
        holder.chart.description.text = "${String.format("%.1f", piecesWeight + additionOneWeight + additionTwoWeight)}г | ${String.format("%.1f", sauceWeight)}г"
        holder.chart.description.textSize = 20f
        holder.chart.description.textColor = ContextCompat.getColor(context, R.color.kormoTech_blue)
        dataSet.valueTextSize = 16f
        dataSet.sliceSpace = 1f

        val pieData = PieData(dataSet)
        holder.chart.data = pieData
        holder.chart.invalidate()
    }


    override fun getItemCount(): Int = list.size

    fun updateData(newFeedSpecList: MutableList<SpecificationFeedModel>) {
        list = newFeedSpecList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var numtxt: TextView
        var matrixtxt: TextView
        var matrixImage: ImageView
        var nametxt: TextView
        var proport: TextView
        var chart: PieChart
        var regtxt: TextView
        var additionalRegContainer: ViewGroup

        init {
            numtxt = itemView.findViewById(R.id.numtxt)
            matrixtxt = itemView.findViewById(R.id.matrixtxt)
            matrixImage = itemView.findViewById(R.id.matrix_view)
            nametxt = itemView.findViewById(R.id.nametxt)
            proport = itemView.findViewById(R.id.proportiontxt)
            chart = itemView.findViewById(R.id.pieChart)
            regtxt = itemView.findViewById(R.id.regtxt)
            additionalRegContainer = itemView.findViewById(R.id.additionalRegContainer)
        }
    }
}