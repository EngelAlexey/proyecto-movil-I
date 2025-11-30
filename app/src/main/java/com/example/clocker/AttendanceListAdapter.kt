package com.example.clocker

import Entity.AttendanceWithPerson
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class AttendanceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtDate: TextView = view.findViewById(R.id.txtDate)
    val txtName: TextView = view.findViewById(R.id.txtName)
    val txtEntry: TextView = view.findViewById(R.id.txtEntryTime)
    val txtExit: TextView = view.findViewById(R.id.txtExitTime)

    fun bind(item: AttendanceWithPerson) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a") // Ej: 08:30 PM

        txtDate.text = item.attendance.DateAttendance.format(dateFormatter)
        txtName.text = item.personName

        // Mostrar Hora formateada
        txtEntry.text = item.attendance.timeEntry.format(timeFormatter)

        // Verificar si es la fecha dummy (año 2000)
        if (item.attendance.timeExit.year == 2000) {
            txtExit.text = "Pendiente"
            txtExit.setTextColor(Color.GRAY)
        } else {
            txtExit.text = item.attendance.timeExit.format(timeFormatter)
            txtExit.setTextColor(Color.parseColor("#F44336"))
        }
    }
}
class AttendanceListAdapter(
    private val itemList: List<AttendanceWithPerson>
) : RecyclerView.Adapter<AttendanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_attendances, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}