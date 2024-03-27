package com.pipe.d.dev.eventbuspatt.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pipe.d.dev.eventbuspatt.R
import com.pipe.d.dev.eventbuspatt.SportEvent
import com.pipe.d.dev.eventbuspatt.databinding.ItemEventBinding

/****
 * Project: Event Bus Pattern
 * From: com.cursosant.eventbuspattern
 * Created by Alain Nicolás Tello on 23/01/24 at 13:48
 * All rights reserved 2024.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
class ResultAdapter(private val listener: OnClickListener) : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private val results = mutableListOf<SportEvent.ResultSuccess>()

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.setListener(result)
        with(holder.binding){
            tvSport.text = result.sportName
            if (result.results?.size == 3){
                tvResults.text = context.getString(
                    R.string.item_event_results,
                    result.results[0],
                    result.results[1],
                    result.results[2])
            }
            imgSport.setImageResource(result.getImageRes())
            tvWarning.visibility = if (result.isWarning) View.VISIBLE else View.GONE
        }
    }

    override fun getItemCount() = results.size

    fun add(result: SportEvent.ResultSuccess) {
        if (!results.contains(result)) {
            results.add(result)
            notifyItemInserted(results.size - 1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        results.clear()
        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemEventBinding.bind(view)

        fun setListener(result: SportEvent.ResultSuccess) {
            binding.root.setOnClickListener { listener.onClick(result) }
        }
    }
}