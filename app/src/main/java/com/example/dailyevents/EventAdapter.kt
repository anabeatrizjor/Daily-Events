package com.example.dailyevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private var eventList: MutableList<String>,
    private val onEditClick: (position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventTextView: TextView = itemView.findViewById(R.id.tvEvent)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditEvent)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventTextView.text = eventList[position]
        holder.btnEdit.setOnClickListener { onEditClick(position) }
        holder.btnDelete.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateEvents(newEvents: List<String>) {
        eventList = newEvents.toMutableList()
        notifyDataSetChanged()
    }
}
