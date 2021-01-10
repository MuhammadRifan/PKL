package id.manlyman.petto.ui.facility.animalcare

import android.app.Activity
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.HomeActivity
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.activity_add_a_c.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ActivityAddAC : AppCompatActivity() {
    private var imageFile: File? = null
    var pict = 0

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_a_c)

        val mcurrentTime = Calendar.getInstance()
        var hour: Int
        var minute: Int
        val sessionId = intent.getStringExtra("ID").toString()

        if (sessionId == "null") {
            supportActionBar?.title = "Tambah Animal Care"
            btnHapusAC.visibility = View.GONE
            btnTambahAC.text = "Tambah"
        } else {
            load(sessionId)
            supportActionBar?.title = "Edit Animal Care"
            btnHapusAC.visibility = View.VISIBLE
            btnTambahAC.text = "Simpan"
        }

        btnTambahAC.setOnClickListener {
            var error = 0

            if (txtNamaAC.text.toString().length < 3) {
                txtNamaAC.error = "Mohon masukan nama dengan benar"
                error++
            }

            if (txtAlamatAC.text.toString().length < 5) {
                txtAlamatAC.error = "Mohon masukan alamat dengan benar"
                error++
            }

            if (txtKotaAC.text.toString().length < 3) {
                txtKotaAC.error = "Mohon masukan kota dengan benar"
                error++
            }

            if (!cbACSenin.isChecked &&
                !cbACSelasa.isChecked &&
                !cbACRabu.isChecked &&
                !cbACKamis.isChecked &&
                !cbACJumat.isChecked &&
                !cbACSabtu.isChecked &&
                !cbACMinggu.isChecked) {
                Toast.makeText(this, "Mohon pilih hari buka", Toast.LENGTH_SHORT).show()
                error++
            }

            if (txtBukaAC.text.toString().isEmpty()) {
                txtBukaAC.error = "Mohon masukkan jam buka toko"
                error++
            }

            if (txtTutupAC.text.toString().isEmpty()) {
                txtTutupAC.error = "Mohon masukkan jam tutup toko"
                error++
            }

            if (txtNomorAC.text.toString().length != 12) {
                txtNomorAC.error = "Mohon masukan nomor telfon dengan benar"
                error++
            }

            if (imageFile == null && sessionId == "null") {
                Toast.makeText(this, "Mohon masukan foto", Toast.LENGTH_LONG).show()
                error++
            }

            if (error == 0) {
                if (sessionId == "null") {
                    addAC(imageFile!!)
                } else {
                    if (pict == 0) {
                        UpdateWoImage(sessionId)
                    } else {
                        UpdateWiImage(sessionId, imageFile!!)
                        pict = 0
                    }
                }
            } else {
                Toast.makeText(this, "Form tidak lengkap", Toast.LENGTH_SHORT).show()
            }

        }

        txtBukaAC.setOnClickListener {
            if (txtBukaAC.text.toString().isNotEmpty()) {
                val buka = txtBukaAC.text.toString().split(":")
                hour = buka[0].toInt()
                minute = buka[1].toInt()
            } else {
                hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                minute = mcurrentTime[Calendar.MINUTE]
            }

            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this,
                { timePicker, selectedHour, selectedMinute ->
                    fixTime(selectedHour.toString(), selectedMinute.toString(), txtBukaAC)
                }, hour, minute, true)
            mTimePicker.show()
        }

        txtTutupAC.setOnClickListener {
            if (txtTutupAC.text.toString().isNotEmpty()) {
                val tutup = txtTutupAC.text.toString().split(":")
                hour = tutup[0].toInt()
                minute = tutup[1].toInt()
            } else {
                hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                minute = mcurrentTime[Calendar.MINUTE]
            }

            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this,
                { timePicker, selectedHour, selectedMinute ->
                    fixTime(selectedHour.toString(), selectedMinute.toString(), txtTutupAC)
                }, hour, minute, true)
            mTimePicker.show()
        }

        btnHapusAC.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("Ok" ) { dialog, id ->
                    delete(sessionId)
                }
                setNegativeButton("Cancel" ) { dialog, id -> }
            }
            // Set other dialog properties
            builder.setMessage("Apa anda yakin ?")
                    .setTitle("Hapus Animal Care")

            // Create the AlertDialog
            builder.create().show()
        }
        
        choosePict.setOnClickListener {
            if (EasyPermissions.hasPermissions(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) && EasyPermissions.hasPermissions(
                            this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) && EasyPermissions.hasPermissions(
                            this, android.Manifest.permission.CAMERA
                    ))
            {
                val picName = Uri.fromFile(
                        File(
                                externalCacheDir, "tmp_" + System.currentTimeMillis().toString() + ".jpg"
                        )
                )
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setOutputUri(picName)
                        .start(this)
            } else {
                EasyPermissions.requestPermissions(
                        this, "This application need your permission to access photo gallery.",
                        IMAGE_PICK_CODE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                EasyPermissions.requestPermissions(
                        this, "This application need your permission to access photo gallery.",
                        IMAGE_PICK_CODE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                EasyPermissions.requestPermissions(
                        this, "This application need your permission to access photo gallery.",
                        IMAGE_PICK_CODE,
                        android.Manifest.permission.CAMERA
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {

            val result = CropImage.getActivityResult(data)
            try {
                val selectedImageUri = result.uri
                if (selectedImageUri != null) {
                    shopPict.setImageURI(selectedImageUri)
                    imageFile = File(selectedImageUri.path.toString())
                    pict = 1
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun fixTime(hour: String, minute: String, input: EditText) {
        var time = ""

        if (hour.length == 1) {
            time += "0$hour"
        } else {
            time += hour
        }

        time += ":"

        if (minute.length == 1) {
            time += "0$minute"
        } else {
            time += minute
        }

        input.setText(time)
        input.error = null
    }

    private fun addAC(file: File) {
        val config = FConfig(applicationContext)
        val loading = ProgressDialog(this)
        loading.setTitle("Adding Your Animal Care...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
                .addMultipartFile("image", file)
                .addMultipartParameter("folder","img_animalcare")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                    loading.setMessage("Uploading image (" + (((bytesUploaded / totalBytes) * 100)-20).toString() + "/100)")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                            AndroidNetworking.post(ApiEndPoint.AddAC)
                                    .addBodyParameter("owner", config.getCustom("uname", ""))
                                    .addBodyParameter("nama", txtNamaAC.text.toString())
                                    .addBodyParameter("address", txtAlamatAC.text.toString())
                                    .addBodyParameter("city", txtKotaAC.text.toString())
                                    .addBodyParameter("phone", txtNomorAC.text.toString())
                                    .addBodyParameter("picture", response)
                                    .addBodyParameter("h1", cbACMinggu.isChecked.toString())
                                    .addBodyParameter("h2", cbACSenin.isChecked.toString())
                                    .addBodyParameter("h3", cbACSelasa.isChecked.toString())
                                    .addBodyParameter("h4", cbACRabu.isChecked.toString())
                                    .addBodyParameter("h5", cbACKamis.isChecked.toString())
                                    .addBodyParameter("h6", cbACJumat.isChecked.toString())
                                    .addBodyParameter("h7", cbACSabtu.isChecked.toString())
                                    .addBodyParameter("buka", txtBukaAC.text.toString())
                                    .addBodyParameter("tutup", txtTutupAC.text.toString())
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject?) {
                                            if (response?.getString("message")?.contains("berhasil")!!) {
                                                loading.setMessage("Saving data (100/100)")
                                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                            }

                                            Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                                            loading.dismiss()
                                        }

                                        override fun onError(anError: ANError?) {
                                            loading.dismiss()
                                            Log.d("OnError", anError?.errorDetail?.toString()!!)
                                            Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                                        }

                                    })
                        } else {
                            loading.dismiss()
                            Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        loading.dismiss()
                        Toast.makeText(
                                applicationContext,
                                "Error : " + anError.message,
                                Toast.LENGTH_LONG
                        ).show()
                    }
                })
    }

    private fun load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()
        pict = 0

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "animalcare")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    picture(response?.getString("picture").toString(), shopPict)
                    txtNamaAC.setText(response?.getString("nama").toString())
                    txtAlamatAC.setText(response?.getString("address").toString())
                    txtKotaAC.setText(response?.getString("city").toString())
                    txtBukaAC.setText(response?.getString("jam_buka").toString())
                    txtTutupAC.setText(response?.getString("jam_tutup").toString())
                    txtNomorAC.setText(response?.getString("phone").toString())

                    if (response?.getString("hari_buka2").toString() != "0") cbACSenin.isChecked = true
                    if (response?.getString("hari_buka3").toString() != "0") cbACSelasa.isChecked = true
                    if (response?.getString("hari_buka4").toString() != "0") cbACRabu.isChecked = true
                    if (response?.getString("hari_buka5").toString() != "0") cbACKamis.isChecked = true
                    if (response?.getString("hari_buka6").toString() != "0") cbACJumat.isChecked = true
                    if (response?.getString("hari_buka7").toString() != "0") cbACSabtu.isChecked = true
                    if (response?.getString("hari_buka1").toString() != "0") cbACMinggu.isChecked = true
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun picture(url: String, img: ImageView){
        AndroidNetworking.get(ApiEndPoint.Pictures + url)
            .setTag("Foto")
            .setPriority(Priority.MEDIUM)
            .setBitmapConfig(Bitmap.Config.ARGB_8888)
            .build()
            .getAsBitmap(object : BitmapRequestListener {
                override fun onResponse(bitmap: Bitmap) {
                    img.setImageBitmap(bitmap)
                }

                override fun onError(error: ANError) {
                    Log.d("OnError", error.errorDetail.toString())
                }
            })
    }

    private fun delete(ID: String){
        val loading = ProgressDialog(this)
        loading.setTitle("Deleting ...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Delete)
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .addBodyParameter("table", "animalcare")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        finish()
                    }
                    loading.dismiss()
                    Toast.makeText(this@ActivityAddAC, response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(this@ActivityAddAC, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun UpdateWoImage(ID: String){
        val loading = ProgressDialog(this)
        loading.setTitle("Updating ...")
        loading.setMessage("Saving data ...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.UpdateAC)
            .addBodyParameter("id", ID)
            .addBodyParameter("nama", txtNamaAC.text.toString())
            .addBodyParameter("address", txtAlamatAC.text.toString())
            .addBodyParameter("city", txtKotaAC.text.toString())
            .addBodyParameter("phone", txtNomorAC.text.toString())
            .addBodyParameter("picture", "")
            .addBodyParameter("h1", cbACMinggu.isChecked.toString())
            .addBodyParameter("h2", cbACSenin.isChecked.toString())
            .addBodyParameter("h3", cbACSelasa.isChecked.toString())
            .addBodyParameter("h4", cbACRabu.isChecked.toString())
            .addBodyParameter("h5", cbACKamis.isChecked.toString())
            .addBodyParameter("h6", cbACJumat.isChecked.toString())
            .addBodyParameter("h7", cbACSabtu.isChecked.toString())
            .addBodyParameter("buka", txtBukaAC.text.toString())
            .addBodyParameter("tutup", txtTutupAC.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        loading.setMessage("Saving data ...")
                        load(response.getString("id"))
                    }
                    loading.dismiss()
                    Toast.makeText(this@ActivityAddAC, response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(this@ActivityAddAC, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun UpdateWiImage(ID: String, file: File) {
        val loading = ProgressDialog(this)
        loading.setTitle("Updating ...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
                .addMultipartFile("image", file)
                .addMultipartParameter("folder", "img_animalcare")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                    loading.setMessage("Uploading image (" + ((bytesUploaded / totalBytes) * 100).toString() + "/100)")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                            AndroidNetworking.post(ApiEndPoint.UpdateAC)
                                .addBodyParameter("id", ID)
                                .addBodyParameter("nama", txtNamaAC.text.toString())
                                .addBodyParameter("address", txtAlamatAC.text.toString())
                                .addBodyParameter("city", txtKotaAC.text.toString())
                                .addBodyParameter("phone", txtNomorAC.text.toString())
                                .addBodyParameter("picture", response)
                                .addBodyParameter("h1", cbACMinggu.isChecked.toString())
                                .addBodyParameter("h2", cbACSenin.isChecked.toString())
                                .addBodyParameter("h3", cbACSelasa.isChecked.toString())
                                .addBodyParameter("h4", cbACRabu.isChecked.toString())
                                .addBodyParameter("h5", cbACKamis.isChecked.toString())
                                .addBodyParameter("h6", cbACJumat.isChecked.toString())
                                .addBodyParameter("h7", cbACSabtu.isChecked.toString())
                                .addBodyParameter("buka", txtBukaAC.text.toString())
                                .addBodyParameter("tutup", txtTutupAC.text.toString())
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject?) {
                                        if (response?.getString("message")?.contains("berhasil")!!) {
                                            loading.setMessage("Saving data ...")
                                            load(response.getString("id"))
                                        }
                                        loading.dismiss()
                                        Toast.makeText(this@ActivityAddAC, response.getString("message"), Toast.LENGTH_LONG).show()
                                    }

                                    override fun onError(anError: ANError?) {
                                        loading.dismiss()
                                        Log.d("OnError", anError?.errorDetail?.toString()!!)
                                        Toast.makeText(this@ActivityAddAC, "Connection Error", Toast.LENGTH_LONG).show()
                                    }
                                })
                        } else {
                            loading.dismiss()
                            Toast.makeText(this@ActivityAddAC, response, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        loading.dismiss()
                        Toast.makeText(this@ActivityAddAC, "Error : " + anError.message, Toast.LENGTH_LONG).show()
                    }
                })
    }

}