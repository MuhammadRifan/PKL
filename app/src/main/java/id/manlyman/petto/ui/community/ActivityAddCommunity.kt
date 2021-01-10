package id.manlyman.petto.ui.community

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.activity_add_community.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException

class ActivityAddCommunity : AppCompatActivity() {
    private var imageFile: File? = null
    var pict = 0

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_community)

        var sessionId = ""
        sessionId = intent.getStringExtra("ID").toString()

        if (sessionId == "null") {
            supportActionBar?.title = "Tambah Komunitas"
            btnHapusKomunitas.visibility = View.GONE
            btnTambahKomunitas.text = "Tambah"
        } else {
            Load(sessionId)
            supportActionBar?.title = "Edit Komunitas"
            btnHapusKomunitas.visibility = View.VISIBLE
            btnTambahKomunitas.text = "Simpan"
        }

        btnTambahKomunitas.setOnClickListener {
            var error = 0

            if (txtNamaKomunitas.text.toString().length < 5) {
                txtNamaKomunitas.error = "Mohon masukkan nama komunitas dengan benar"
                error++
            }

            if (txtDeskripsiKomunitas.text.toString().length < 10) {
                txtDeskripsiKomunitas.error = "Mohon masukkan deskripsi dengan benar"
                error++
            }

            if (txtAlamatKomunitas.text.toString().length < 10) {
                txtAlamatKomunitas.error = "Mohon masukkan alamat dengan benar"
                error++
            }

            if (txtKotaKomunitas.text.toString().isEmpty()) {
                txtKotaKomunitas.error = "Mohon masukkan kota komunitas dengan benar"
                error++
            }

            if (txtNomorKomunitas.text.toString().length != 12) {
                txtNomorKomunitas.error = "Mohon masukkan nomor dengan benar"
                error++
            }

            if (imageFile == null && sessionId == "null") {
                Toast.makeText(this, "Mohon masukkan foto", Toast.LENGTH_LONG).show()
                error++
            }

            if (error == 0) {
                if (sessionId == "null") {
                    addKomu(imageFile!!)
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

        btnHapusKomunitas.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("Ok" ) { dialog, id ->
                    delete(sessionId)
                }
                setNegativeButton("Cancel" ) { dialog, id -> }
            }
            // Set other dialog properties
            builder.setMessage("Apa anda yakin ?")
                    .setTitle("Hapus Komunitas")

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
                    komunitasPict.setImageURI(selectedImageUri)
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

    private fun addKomu(file: File) {
        val Config = FConfig(this)
        val loading = ProgressDialog(this)
        loading.setTitle("Adding Community ...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
                .addMultipartFile("image", file)
                .addMultipartParameter("folder","img_komunitas")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                    loading.setMessage("Uploading image (" + ((bytesUploaded / totalBytes) * 100).toString() + "/100)")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                            AndroidNetworking.post(ApiEndPoint.AddCommunity)
                                .addBodyParameter("owner", Config.getCustom("uname", ""))
                                .addBodyParameter("nama", txtNamaKomunitas.text.toString())
                                .addBodyParameter("deskripsi", txtDeskripsiKomunitas.text.toString())
                                .addBodyParameter("alamat", txtAlamatKomunitas.text.toString())
                                .addBodyParameter("kota", txtKotaKomunitas.text.toString())
                                .addBodyParameter("phone", txtNomorKomunitas.text.toString())
                                .addBodyParameter("picture", response)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject?) {
                                        if (response?.getString("message")?.contains("berhasil")!!) {
                                            loading.setMessage("Saving data ...")
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
                        Toast.makeText(applicationContext,"Error : " + anError.message,Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun Load(ID: String){
        val loading = ProgressDialog(this)
        loading.setMessage("Loading data...")
        loading.show()
        pict = 0

        AndroidNetworking.post(ApiEndPoint.ReadByID)
            .addBodyParameter("table", "komunitas")
            .addBodyParameter("id", ID)
            .addBodyParameter("idname", "id")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()

                    txtNamaKomunitas.setText(response?.getString("nama").toString())
                    picture(response?.getString("picture").toString(), komunitasPict)
                    txtDeskripsiKomunitas.setText(response?.getString("description").toString())
                    txtAlamatKomunitas.setText(response?.getString("address").toString())
                    txtKotaKomunitas.setText(response?.getString("kota").toString())
                    txtNomorKomunitas.setText(response?.getString("phone").toString())
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
            .addBodyParameter("table", "komunitas")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    }
                    loading.dismiss()
                    Toast.makeText(this@ActivityAddCommunity, response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(this@ActivityAddCommunity, "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun UpdateWoImage(ID: String){
        val loading = ProgressDialog(this)
        loading.setTitle("Updating ...")
        loading.setMessage("Saving data ...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.UpdateCommunity)
            .addBodyParameter("id", ID)
            .addBodyParameter("nama", txtNamaKomunitas.text.toString())
            .addBodyParameter("deskripsi", txtDeskripsiKomunitas.text.toString())
            .addBodyParameter("alamat", txtAlamatKomunitas.text.toString())
            .addBodyParameter("kota", txtKotaKomunitas.text.toString())
            .addBodyParameter("phone", txtNomorKomunitas.text.toString())
            .addBodyParameter("picture", "")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        loading.setMessage("Saving data ...")
                        Load(response.getString("id"))
                    }
                    loading.dismiss()
                    Toast.makeText(this@ActivityAddCommunity, response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Toast.makeText(this@ActivityAddCommunity, "Connection Error", Toast.LENGTH_LONG).show()
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
            .addMultipartParameter("folder", "img_komunitas")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                loading.setMessage("Uploading image (" + ((bytesUploaded / totalBytes) * 100).toString() + "/100)")
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                        AndroidNetworking.post(ApiEndPoint.UpdateCommunity)
                            .addBodyParameter("id", ID)
                            .addBodyParameter("nama", txtNamaKomunitas.text.toString())
                            .addBodyParameter("deskripsi", txtDeskripsiKomunitas.text.toString())
                            .addBodyParameter("alamat", txtAlamatKomunitas.text.toString())
                            .addBodyParameter("kota", txtKotaKomunitas.text.toString())
                            .addBodyParameter("phone", txtNomorKomunitas.text.toString())
                            .addBodyParameter("picture", response)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(object : JSONObjectRequestListener {
                                override fun onResponse(response: JSONObject?) {
                                    if (response?.getString("message")?.contains("berhasil")!!) {
                                        loading.setMessage("Saving data ...")

                                        Load(response.getString("id"))
                                    }
                                    loading.dismiss()
                                    Toast.makeText(this@ActivityAddCommunity, response.getString("message"), Toast.LENGTH_LONG).show()
                                }

                                override fun onError(anError: ANError?) {
                                    loading.dismiss()
                                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                                    Toast.makeText(this@ActivityAddCommunity, "Connection Error", Toast.LENGTH_LONG).show()
                                }
                            })
                    } else {
                        loading.dismiss()
                        Toast.makeText(this@ActivityAddCommunity, response, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(anError: ANError) {
                    loading.dismiss()
                    Toast.makeText(this@ActivityAddCommunity, "Error : " + anError.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}