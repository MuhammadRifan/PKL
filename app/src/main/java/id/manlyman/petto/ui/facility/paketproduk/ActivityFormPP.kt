package id.manlyman.petto.ui.facility.paketproduk

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.form_paket_produk.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException

class ActivityFormPP : AppCompatActivity() {
    private var imageFile: File? = null
    var pict = 0

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_paket_produk)

        var sessionId = ""
        var tempat = ""
        sessionId = intent.getStringExtra("ID").toString()
        tempat = intent.getStringExtra("TEMPAT").toString()

        if (sessionId == "null") {
            supportActionBar?.title = "Tambah"
            btnTambahPP.text = "Tambah"
        } else {
            Load(sessionId, tempat)
            supportActionBar?.title = "Edit"
            btnTambahPP.text = "Simpan"
        }

        btnTambahPP.setOnClickListener {
            var error = 0

            if (txtPPTitle.text.toString().length < 5) {
                txtPPTitle.error = "Mohon isi nama dengan benar"
                error++
            }

            if (txtPPText.text.toString().isEmpty()) {
                txtPPText.error = "Mohon isi harga dengan benar"
                error++
            }

            if (imageFile == null && sessionId == "null") {
                Toast.makeText(this, "Mohon masukkan gambar", Toast.LENGTH_LONG).show()
                error++
            }

            if (error == 0) {
                if (sessionId == "null") {
                    addPP(tempat, imageFile!!)
                } else {
                    if (pict == 0) {
                        UpdateWoImage(sessionId, tempat)
                    } else {
                        UpdateWiImage(sessionId, tempat, imageFile!!)
                        pict = 0
                    }
                }
            } else {
                Toast.makeText(this, "Form tidak lengkap", Toast.LENGTH_SHORT).show()
            }
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
                    this,
                    "This application need your permission to access photo gallery.",
                    IMAGE_PICK_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                EasyPermissions.requestPermissions(
                    this,
                    "This application need your permission to access photo gallery.",
                    IMAGE_PICK_CODE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                EasyPermissions.requestPermissions(
                    this,
                    "This application need your permission to access photo gallery.",
                    IMAGE_PICK_CODE, android.Manifest.permission.CAMERA
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
                    ppPict.setImageURI(selectedImageUri)
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

    private fun addPP(tempat: String, file: File) {
        val config = FConfig(this)
        val loading = ProgressDialog(this)
        loading.setTitle("Adding...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
            .addMultipartFile("image", file)
            .addMultipartParameter("folder", "img_${tempat}")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                loading.setMessage("Uploading image (" + (((bytesUploaded / totalBytes) * 100) - 20).toString() + "/100)")
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                        AndroidNetworking.post(ApiEndPoint.AddPP)
                            .addBodyParameter("tempat", tempat)
                            .addBodyParameter("uname", config.getCustom("uname", ""))
                            .addBodyParameter("nama", txtPPTitle.text.toString())
                            .addBodyParameter("harga", txtPPText.text.toString())
                            .addBodyParameter("picture", response)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(object : JSONObjectRequestListener {
                                override fun onResponse(response: JSONObject?) {
                                    if (response?.getString("message")?.contains("berhasil")!!) {
                                        loading.setMessage("Saving data (100/100)")
                                        finish()
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
                    Toast.makeText(applicationContext, "Error : " + anError.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun Load(ID: String, tempat: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()
        pict = 0

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", tempat)
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    picture(response?.getString("picture").toString(), ppPict)
                    txtPPTitle.setText(response?.getString("name").toString())
                    txtPPText.setText(response?.getString("harga").toString())
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

    private fun UpdateWoImage(ID: String, tempat: String){
        val loading = ProgressDialog(this)
        loading.setTitle("Updating ...")
        loading.setMessage("Saving data ...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.UpdatePP)
            .addBodyParameter("id", ID)
            .addBodyParameter("tmp", tempat)
            .addBodyParameter("nama", txtPPTitle.text.toString())
            .addBodyParameter("harga", txtPPText.text.toString())
            .addBodyParameter("picture", "")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        loading.setMessage("Saving data ...")
                        Load(response.getString("id"), tempat)
                    }
                    loading.dismiss()
                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun UpdateWiImage(ID: String, tempat: String, file: File) {
        val loading = ProgressDialog(this)
        loading.setTitle("Updating ...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
            .addMultipartFile("image", file)
            .addMultipartParameter("folder", "img_${tempat}")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                loading.setMessage("Uploading image (" + ((bytesUploaded / totalBytes) * 100).toString() + "/100)")
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                        AndroidNetworking.post(ApiEndPoint.UpdatePP)
                            .addBodyParameter("id", ID)
                            .addBodyParameter("tmp", tempat)
                            .addBodyParameter("nama", txtPPTitle.text.toString())
                            .addBodyParameter("harga", txtPPText.text.toString())
                            .addBodyParameter("picture", response)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(object : JSONObjectRequestListener {
                                override fun onResponse(response: JSONObject?) {
                                    if (response?.getString("message")?.contains("berhasil")!!) {
                                        loading.setMessage("Saving data ...")

                                        Load(response.getString("id"), tempat)
                                    }
                                    loading.dismiss()
                                    Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
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
                    Toast.makeText(applicationContext, "Error : " + anError.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}