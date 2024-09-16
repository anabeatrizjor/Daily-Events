package com.example.dailyevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para a lista de eventos
class EventAdapter(
    private var eventList: MutableList<Event>,
    private val onEditClick: (position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder para cada item da lista de eventos
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvEventTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvEventDescription)
        val timeTextView: TextView = itemView.findViewById(R.id.tvEventTime)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditEvent)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.titleTextView.text = event.title
        holder.descriptionTextView.text = event.description
        holder.timeTextView.text = event.time

        holder.btnEdit.setOnClickListener { onEditClick(position) }
        holder.btnDelete.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int = eventList.size

    // Atualiza a lista de eventos e notifica o adaptador
    fun updateEvents(newEvents: List<Event>) {
        eventList = newEvents.toMutableList()
        notifyDataSetChanged()
    }
}
