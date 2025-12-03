package com.example.clocker

import Controller.AttendanceController
import Controller.PersonController
import Entity.AttendanceWithPerson
import Entity.Attendances
import Interface.OnAttendanceItemClickListener
import Util.Util
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class AttendanceListActivity : AppCompatActivity(), OnAttendanceItemClickListener {

    private lateinit var attendanceController: AttendanceController
    private lateinit var personController: PersonController
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_attendances_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recycler = findViewById(R.id.rvAttendances)
        val btnBack = findViewById<Button>(R.id.btnBack2)
        btnBack.setOnClickListener { finish() }

        attendanceController = AttendanceController(this)
        personController = PersonController(this)

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val peopleList = personController.getAllPeople()
                val attendanceList = attendanceController.getAllAttendances()

                val uiList = attendanceList.map { attendance ->
                    val person = peopleList.find { it.ID == attendance.idPerson }
                    val fullName = if (person != null) {
                        "${person.Name} ${person.FLastName} ${person.SLastName}"
                    } else {
                        "${R.string.MsgUnknow} (ID: ${attendance.idPerson})"
                    }
                    AttendanceWithPerson(attendance, fullName)
                }

                val sortedList = uiList.sortedWith(
                    compareByDescending<AttendanceWithPerson> { it.attendance.DateAttendance }
                        .thenBy { it.personName }
                )

                val adapter = AttendanceListAdapter(sortedList, this@AttendanceListActivity)
                recycler.layoutManager = LinearLayoutManager(this@AttendanceListActivity)
                recycler.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(this@AttendanceListActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDeleteClick(attendance: Attendances) {
        Util.showDialogCondition(this, getString(R.string.TextDeleteActionQuestion)) {
            lifecycleScope.launch {
                try {
                    attendanceController.deleteAttendance(attendance.idAttendance)
                    Toast.makeText(this@AttendanceListActivity, getString(R.string.MsgDelete), Toast.LENGTH_SHORT).show()
                    loadData()
                } catch (e: Exception) {
                    Toast.makeText(this@AttendanceListActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}