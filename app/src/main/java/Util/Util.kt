package Util
import android.content.Context
import android.content.Intent

class Util {
    companion object{
        fun OpenActivity(context: Context, objClass: Class<*>){
            val intent = Intent(context, objClass)
            context.startActivity(intent)
        }
    }
}