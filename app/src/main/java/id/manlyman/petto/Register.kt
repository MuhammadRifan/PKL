package id.manlyman.petto

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException

class Register : AppCompatActivity() {
    private var imageFile: File? = null

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        btnRegisterPengguna.setOnClickListener {
            var error = 0

            if (txtNama.text.toString().length < 3) {
                txtNama.error = "Mohon masukan nama dengan benar"
                error++
            }

            if (txtRegistEmail.text.toString().isEmpty()) {
                txtRegistEmail.error = "Mohon masukan email"
                error++
            } else if (!txtRegistEmail.text.toString().trim().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.com+"))) {
                txtRegistEmail.error = "Mohon masukkan email dengan benar"
                error++
            }

            if (txtRegistPass.text.toString().length < 5) {
                txtRegistPass.error = "Mohon masukan password dengan benar"
                error++
            } else if (txtConfirmPass.text.toString().length < 5) {
                txtConfirmPass.error = "Mohon masukan password dengan benar"
                error++
            } else if (txtRegistPass.text.toString() != txtConfirmPass.text.toString()) {
                txtConfirmPass.error = "Password tidak sama"
                error++
            }

            if (txtNoTelp.text.toString().length != 12) {
                txtNoTelp.error = "Mohon masukan nomor telfon dengan benar"
                error++
            }

            if (imageFile == null) {
                Toast.makeText(this, "Mohon masukan foto", Toast.LENGTH_LONG).show()
                error++
            }

            if (error == 0) {
                register(imageFile!!)
            } else {
                Toast.makeText(this, "Form tidak lengkap", Toast.LENGTH_SHORT).show()
            }

        }

        txtLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
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
                    IMAGE_PICK_CODE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                EasyPermissions.requestPermissions(
                    this,
                    "This application need your permission to access photo gallery.",
                    IMAGE_PICK_CODE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                EasyPermissions.requestPermissions(
                    this,
                    "This application need your permission to access photo gallery.",
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
                    profilePict.setImageURI(selectedImageUri)
                    imageFile = File(selectedImageUri.path.toString())
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun register(file: File) {
        val loading = ProgressDialog(this)
        loading.setTitle("Registering...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
            .addMultipartFile("image", file)
            .addMultipartParameter("folder","img_user")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                loading.setMessage("Uploading image (" + (((bytesUploaded / totalBytes) * 100)-20).toString() + "/100)")
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                        AndroidNetworking.post(ApiEndPoint.Register)
                                .addBodyParameter("nama", txtNama.text.toString())
                                .addBodyParameter("email", txtRegistEmail.text.toString())
                                .addBodyParameter("pass", txtRegistPass.text.toString())
                                .addBodyParameter("notelp", txtNoTelp.text.toString())
                                .addBodyParameter("foto", response)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener {
                                    override fun onResponse(response: JSONObject?) {
                                        if (response?.getString("message")?.contains("berhasil")!!) {
                                            loading.setMessage("Saving data (100/100)")
                                            val intent = Intent(applicationContext, Areyou::class.java)
                                            intent.putExtra("uname", txtNama.text.toString())
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_LONG).show()
                                        }
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
