package com.example.clocker

import Controller.ClockController
import Controller.PersonController
import Entity.Clock
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ClockActivity : AppCompatActivity() {

    private lateinit var textId: EditText
    private lateinit var textName: EditText
    private lateinit var imgPhoto: ImageView
    private lateinit var clockController: ClockController
    private lateinit var personController: PersonController

    private val cameraPreviewLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                imgPhoto.setImageBitmap(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clock)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        clockController = ClockController(this)
        personController = PersonController(this)

        textId = findViewById(R.id.TextID)
        textName = findViewById(R.id.TextName)
        imgPhoto = findViewById(R.id.imgPhoto)

        val btnSelectPhoto = findViewById<ImageButton>(R.id.btnSelectPicture)
        btnSelectPhoto.setOnClickListener { takePhoto() }

        textId.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) loadPersonName()
        }

        textId.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadPersonName()
                true
            } else {
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_crud, menu)
        menu?.findItem(R.id.btnDelete)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnSave -> {
                saveClock()
                true
            }
            R.id.btnCancel -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clear() {
        textId.text.clear()
        textName.setText("")
        imgPhoto.setImageBitmap(null)
    }

    private fun isValidate(): Boolean {
        val drawable = imgPhoto.drawable as? BitmapDrawable
        return textId.text.isNotBlank() && drawable?.bitmap != null
    }

    private fun takePhoto() {
        cameraPreviewLauncher.launch(null)
    }

    private fun loadPersonName() {
        val idPerson = textId.text.toString().trim()
        if (idPerson.isBlank()) {
            textName.setText("")
            return
        }

        lifecycleScope.launch {
            try {
                val person = personController.getByIdPerson(idPerson)
                if (person == null) {
                    Toast.makeText(this@ClockActivity, R.string.MsgDataNoFound, Toast.LENGTH_LONG).show()
                    textName.setText("")
                } else {
                    val fullName = "${person.Name} ${person.FLastName} ${person.SLastName}"
                    textName.setText(fullName)
                }
            } catch (e: Exception) {
                textName.setText("")
            }
        }
    }

    private fun saveClock() {
        lifecycleScope.launch {
            try {
                val idPerson = textId.text.toString().trim()
                if (idPerson.isBlank()) {
                    Toast.makeText(this@ClockActivity, R.string.ErrorMsgGetById, Toast.LENGTH_LONG).show()
                    return@launch
                }

                val person = personController.getByIdPerson(idPerson)
                if (person == null) {
                    Toast.makeText(this@ClockActivity, R.string.MsgDataNoFound, Toast.LENGTH_LONG).show()
                    textName.setText("")
                    return@launch
                } else {
                    textName.setText("${person.Name} ${person.FLastName} ${person.SLastName}")
                }

                if (!isValidate()) {
                    Toast.makeText(this@ClockActivity, R.string.ErrorMsgAdd, Toast.LENGTH_LONG).show()
                    return@launch
                }

                val idClock = ""
                val bitmap = (imgPhoto.drawable as BitmapDrawable).bitmap
                val dateClock = LocalDateTime.now()
                val type = ""
                val address = ""
                val latitude = 0
                val longitude = 0

                val clock = Clock(
                    idClock = idClock,
                    idPerson = idPerson,
                    dateClock = dateClock,
                    type = type,
                    address = address,
                    latitude = latitude,
                    longitude = longitude,
                    photo = bitmap
                )

                clockController.addClock(clock)
                Toast.makeText(this@ClockActivity, getString(R.string.MsgSave), Toast.LENGTH_LONG).show()
                clear()

            } catch (e: Exception) {
                Toast.makeText(this@ClockActivity, e.message ?: "", Toast.LENGTH_LONG).show()
            }
        }
    }
}