package com.example.clocker

import Controller.ClockController
import Controller.PersonController
import Entity.Clock
import Entity.ClockWithPerson
import Interface.OnClockItemClickListener
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

class ClockListActivity : AppCompatActivity(), OnClockItemClickListener {

    private lateinit var clockController: ClockController
    private lateinit var personController: PersonController
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clock_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recycler = findViewById(R.id.rvClock)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        clockController = ClockController(this)
        personController = PersonController(this)

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val peopleList = personController.getAllPeople()
                val clockList = clockController.getAllClocks()

                val uiList = clockList.map { clock ->
                    val person = peopleList.find { it.ID == clock.IDPerson }
                    val fullName = if (person != null) {
                        "${person.Name} ${person.FLastName} ${person.SLastName}"
                    } else {
                        "Desconocido (ID: ${clock.IDPerson})"
                    }
                    ClockWithPerson(clock, fullName)
                }

                val sortedList = uiList.sortedByDescending { it.clock.DateClock }

                val adapter = ClockListAdapter(sortedList, this@ClockListActivity)

                recycler.layoutManager = LinearLayoutManager(this@ClockListActivity)
                recycler.adapter = adapter

            } catch (e: Exception) {
                Toast.makeText(this@ClockListActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDeleteClick(clock: Clock) {
        Util.showDialogCondition(this, getString(R.string.TextDeleteActionQuestion)) {
            lifecycleScope.launch {
                try {
                    clockController.removeClock(clock.IDClock)
                    Toast.makeText(this@ClockListActivity, getString(R.string.MsgDelete), Toast.LENGTH_SHORT).show()
                    loadData()
                } catch (e: Exception) {
                    Toast.makeText(this@ClockListActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}