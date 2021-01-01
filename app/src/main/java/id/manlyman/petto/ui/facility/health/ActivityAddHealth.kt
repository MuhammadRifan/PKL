package id.manlyman.petto.ui.facility.health

import android.app.Activity
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.HomeActivity
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.activity_add_health.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ActivityAddHealth : AppCompatActivity() {
    private var imageFile: File? = null

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_health)

        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]

        btnTambahHealth.setOnClickListener {
            var error = 0

            if (txtNamaHealth.text.toString().length < 3) {
                txtNamaHealth.error = "Mohon masukan nama dengan benar"
                error++
            }

            if (txtDeskripsiHealth.text.toString().length < 5) {
                txtDeskripsiHealth.error = "Mohon masukan deskripsi dengan benar"
                error++
            }

            if (txtAlamatHealth.text.toString().length < 5) {
                txtAlamatHealth.error = "Mohon masukan alamat dengan benar"
                error++
            }

            if (txtKotaHealth.text.toString().length < 3) {
                txtKotaHealth.error = "Mohon masukan kota dengan benar"
                error++
            }

            if (!cbHSenin.isChecked &&
                    !cbHSelasa.isChecked &&
                    !cbHRabu.isChecked &&
                    !cbHKamis.isChecked &&
                    !cbHJumat.isChecked &&
                    !cbHSabtu.isChecked &&
                    !cbHMinggu.isChecked) {
                Toast.makeText(this, "Mohon pilih hari buka", Toast.LENGTH_SHORT).show()
                error++
            }

            if (txtBukaHealth.text.toString().isEmpty()) {
                txtBukaHealth.error = "Mohon masukkan jam buka toko"
                error++
            }

            if (txtTutupHealth.text.toString().isEmpty()) {
                txtTutupHealth.error = "Mohon masukkan jam tutup toko"
                error++
            }

            if (txtNomorSIP.text.toString().isEmpty()) {
                txtNomorSIP.error = "Mohon masukan nomor SIP dengan benar"
                error++
            }

            if (txtNomorHealth.text.toString().length != 12) {
                txtNomorHealth.error = "Mohon masukan nomor telfon dengan benar"
                error++
            }

            if (imageFile == null) {
                Toast.makeText(this, "Mohon masukan foto", Toast.LENGTH_LONG).show()
                error++
            }

            if (error == 0) {
                addShop(imageFile!!)
            } else {
                Toast.makeText(this, "Form tidak lengkap", Toast.LENGTH_SHORT).show()
            }

        }

        txtBukaHealth.setOnClickListener {
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this,
                    { timePicker, selectedHour, selectedMinute ->
                        fixTime(selectedHour.toString(), selectedMinute.toString(), txtBukaHealth)
                    }, hour, minute, true)
            mTimePicker.show()
        }

        txtTutupHealth.setOnClickListener {
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this,
                    { timePicker, selectedHour, selectedMinute ->
                        fixTime(selectedHour.toString(), selectedMinute.toString(), txtTutupHealth)
                    }, hour, minute, true)
            mTimePicker.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {

            val result = CropImage.getActivityResult(data)
            try {
                val selectedImageUri = result.uri
                if (selectedImageUri != null) {
                    healthPict.setImageURI(selectedImageUri)
                    imageFile = File(selectedImageUri.path.toString())
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

    private fun addShop(file: File) {
        val config = FConfig(applicationContext)
        val loading = ProgressDialog(this)
        loading.setTitle("Adding Your Animal Care...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
                .addMultipartFile("image", file)
                .addMultipartParameter("folder","img_faskes")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                    loading.setMessage("Uploading image (" + (((bytesUploaded / totalBytes) * 100)-20).toString() + "/100)")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                            AndroidNetworking.post(ApiEndPoint.AddHealth)
                                    .addBodyParameter("sip", txtNomorSIP.text.toString())
                                    .addBodyParameter("owner", config.getCustom("uname", ""))
                                    .addBodyParameter("nama", txtNamaHealth.text.toString())
                                    .addBodyParameter("deskripsi", txtDeskripsiHealth.text.toString())
                                    .addBodyParameter("address", txtAlamatHealth.text.toString())
                                    .addBodyParameter("city", txtKotaHealth.text.toString())
                                    .addBodyParameter("phone", txtNomorHealth.text.toString())
                                    .addBodyParameter("picture", response)
                                    .addBodyParameter("h1", cbHMinggu.isChecked.toString())
                                    .addBodyParameter("h2", cbHSenin.isChecked.toString())
                                    .addBodyParameter("h3", cbHSelasa.isChecked.toString())
                                    .addBodyParameter("h4", cbHRabu.isChecked.toString())
                                    .addBodyParameter("h5", cbHKamis.isChecked.toString())
                                    .addBodyParameter("h6", cbHJumat.isChecked.toString())
                                    .addBodyParameter("h7", cbHSabtu.isChecked.toString())
                                    .addBodyParameter("buka", txtBukaHealth.text.toString())
                                    .addBodyParameter("tutup", txtTutupHealth.text.toString())
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
}