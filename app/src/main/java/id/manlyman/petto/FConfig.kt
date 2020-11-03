package id.manlyman.petto

import android.content.Context
import android.content.SharedPreferences

class FConfig(context: Context) {
    val PREFERENCE_NAME = "DataSess"

    val preference : SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCustom(Name:String, Value:String) : String{
        return preference.getString(Name, Value).toString()
    }

    fun setCustom(Name:String, Value: String){
        val editor = preference.edit()
        editor.putString(Name, Value)
        editor.apply()
    }

    fun delCustom(){
        val editor = preference.edit()
        editor.clear()
        editor.commit()
    }
}