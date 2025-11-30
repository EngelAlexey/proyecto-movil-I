package com.example.clocker

import Entity.Clock
import Entity.ClockWithPerson
import Interface.OnClockItemClickListener
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class ClockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtName: TextView = view.findViewById(R.id.txtName)
    val txtDate: TextView = view.findViewById(R.id.txtDate)
    val txtType: TextView = view.findViewById(R.id.txtType)
    val imgPhoto: ImageView = view.findViewById(R.id.imgPhoto)
    val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

    fun bind(item: ClockWithPerson, clickListener: OnClockItemClickListener) {
        txtName.text = item.personName

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
        txtDate.text = item.clock.DateClock.format(formatter)

        if (item.clock.Type.equals(Clock.TYPE_ENTRY, ignoreCase = true)) {
            txtType.setText(R.string.MsgEntry)
            txtType.setTextColor(Color.parseColor("#2E7D32"))
            txtType.setBackgroundColor(Color.parseColor("#E8F5E9"))
        } else {
            txtType.setText(R.string.MsgDeparture)
            txtType.setTextColor(Color.parseColor("#C62828"))
            txtType.setBackgroundColor(Color.parseColor("#FFEBEE"))
        }

        if (item.clock.Photo != null) {
            imgPhoto.setImageBitmap(item.clock.Photo)
            imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            imgPhoto.setImageResource(android.R.drawable.ic_menu_camera)
        }

        btnDelete.setOnClickListener {
            clickListener.onDeleteClick(item.clock)
        }
    }
}

class ClockListAdapter(
    private val itemList: List<ClockWithPerson>,
    private val itemClickListener: OnClockItemClickListener
) : RecyclerView.Adapter<ClockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_clock, parent, false)
        return ClockViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        holder.bind(itemList[position], itemClickListener)
    }

    override fun getItemCount(): Int = itemList.size
}