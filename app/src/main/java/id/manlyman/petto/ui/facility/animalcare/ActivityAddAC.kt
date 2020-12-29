package id.manlyman.petto.ui.facility.animalcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import id.manlyman.petto.R

class ActivityAddAC : AppCompatActivity() {

    lateinit var option : Spinner
    lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_a_c)

        option = findViewById(R.id.categoryAC1) as Spinner
        result = findViewById(R.id.result) as TextView

        val options = arrayOf("Option 1", "Option 2", "Option 3")

        option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                result.text = "Select Category"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                result.text = options.get(position)
            }
        }
    }
}