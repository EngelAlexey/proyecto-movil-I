package Util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.example.clocker.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Util {
    companion object{
        fun openActivity(context: Context
                         , objClass: Class<*>){
            val intent= Intent(context
                , objClass)
            context.startActivity(intent)
        }

        fun showDialogCondition(context: Context, questionText: String, callback: () -> Unit) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(questionText)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.TextYes)) { dialog, _ -> callback() }
                .setNegativeButton(context.getString(R.string.TextNo)) { dialog, _ -> dialog.cancel() }
            val alert = dialogBuilder.create()
            alert.setTitle(context.getString(R.string.TextTitleDialogQuestion))
            alert.show()
        }

        fun parseStringToDate(dateString: String): LocalDate {
            return try {
                LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
            } catch (e: Exception) {
                LocalDate.now()
            }
        }

        fun formatDate(date: LocalDate): String {
            return date.format(DateTimeFormatter.ISO_DATE)
        }

        fun parseStringToDateTime(dateTimeString: String): LocalDateTime {
            return try {
                LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
            } catch (e: Exception) {
                LocalDateTime.now()
            }
        }

        fun formatDateTime(dateTime: LocalDateTime): String {
            return dateTime.format(DateTimeFormatter.ISO_DATE_TIME)
        }

        fun toBase64(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            val byteArray = outputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        fun toBitmap(base64String: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }
}