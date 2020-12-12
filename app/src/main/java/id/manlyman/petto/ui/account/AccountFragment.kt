package id.manlyman.petto.ui.account

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.androidnetworking.widget.ANImageView
import com.google.android.material.navigation.NavigationView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.manlyman.petto.ApiEndPoint
import id.manlyman.petto.FConfig
import id.manlyman.petto.R
import kotlinx.android.synthetic.main.fragment_akun.view.*
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileNotFoundException


class AccountFragment : Fragment() {
    private var imageFile: File? = null
    var piew: View? = null
    var pict = 0

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_akun, container, false)
        piew = root
        Data()

        var error = 0
        val edit = root.btnAcEdit
        val floatButt = root.changePict
        val cancel = root.btnAcCancel
        val nama = root.acNama
        val email = root.acEmail
        val alamat = root.acAlamat
        val telp = root.acNoTelp

        edit.setOnClickListener {
            if (edit.text == "Edit") {
                pict = 0

                edit.text = "Save"
                floatButt.visibility = View.VISIBLE
                cancel.visibility = View.VISIBLE

                nama.isFocusable = true
                nama.isFocusableInTouchMode = true
                email.isFocusable = true
                email.isFocusableInTouchMode = true
                alamat.isFocusable = true
                alamat.isFocusableInTouchMode = true
                telp.isFocusable = true
                telp.isFocusableInTouchMode = true
            } else {
                error = 0

                if (nama.text.toString().length < 3) {
                    nama.setError("Mohon masukan nama dengan benar")
                    error++
                }

                if (email.text.toString().length == 0) {
                    email.setError("Mohon masukan email")
                    error++
                } else if (!email.text.toString().trim().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.com+"))) {
                    email.setError("Mohon masukkan email dengan benar")
                    error++
                }

                if (alamat.text.toString().length == 0) {
                    alamat.setError("Mohon masukan alamat")
                    error++
                }

                if (telp.text.toString().length != 12) {
                    telp.setError("Mohon masukan nomor telfon dengan benar")
                    error++
                }

                if (error == 0) {
                    if (pict == 0) {
//                        Toast.makeText(requireContext(), "no image", Toast.LENGTH_SHORT).show()
                        UpdateWoImage()
                    } else {
//                        Toast.makeText(requireContext(), "wi image", Toast.LENGTH_SHORT).show()
                        UpdateWiImage(imageFile!!)
                        pict = 0
                    }
                } else {
                    Toast.makeText(requireContext(), "Form ada yang salah", Toast.LENGTH_SHORT).show()
                }
            }

        }

        cancel.setOnClickListener {
            piew?.acProfile?.setImageUrl("")
            Data()

            edit.text = "Edit"
            floatButt.visibility = View.INVISIBLE
            cancel.visibility = View.INVISIBLE

            nama.isFocusable = false
            nama.isFocusableInTouchMode = false
            nama.setError(null)
            email.isFocusable = false
            email.isFocusableInTouchMode = false
            email.setError(null)
            alamat.isFocusable = false
            alamat.isFocusableInTouchMode = false
            alamat.setError(null)
            telp.isFocusable = false
            telp.isFocusableInTouchMode = false
            telp.setError(null)
        }

        floatButt.setOnClickListener {
            if (EasyPermissions.hasPermissions(
                            requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) && EasyPermissions.hasPermissions(
                            requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) && EasyPermissions.hasPermissions(
                            requireContext(), android.Manifest.permission.CAMERA
                    ))
            {
                Log.d("DEBUG", "Masuk Crop")
                val picName = Uri.fromFile(
                        File(
                                requireContext().externalCacheDir, "tmp_"
                                + System.currentTimeMillis().toString() + ".jpg"
                        )
                )
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setOutputUri(picName)
                        .start(requireContext(), this)
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

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && data != null) {

            val result = CropImage.getActivityResult(data)
            try {
                val selectedImageUri = result.uri
                if (selectedImageUri != null) {
                    piew?.acProfile?.setImageURI(selectedImageUri)
                    imageFile = File(selectedImageUri.getPath().toString())
                    pict = 1
                }
                Log.d("DEBUG", "Keluar Crop")

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun Data(){
        val loading = ProgressDialog(requireContext())
        loading.setMessage("Loading ...")
        loading.show()

        val Config = FConfig(requireContext())
        Log.d("DEBUG", "Masuk Data")
        piew?.acProfile?.setDefaultImageResId(R.drawable.ic_launcher_background)
        piew?.acProfile?.setErrorImageResId(R.drawable.ic_launcher_background)
        piew?.acProfile?.setImageUrl(ApiEndPoint.Pictures + Config.getCustom("foto", ""))

        piew?.acNama?.setText(Config.getCustom("nama", ""))
        piew?.acEmail?.setText(Config.getCustom("email", ""))
        piew?.acAlamat?.setText(Config.getCustom("alamat", ""))
        piew?.acNoTelp?.setText(Config.getCustom("notelp", ""))

        loading.dismiss()
    }

    private fun changeNavHead(){
        val navView: NavigationView? = activity?.findViewById(R.id.nav_view)

        val Config = FConfig(requireContext())

        val header: View = navView!!.getHeaderView(0)
        val userProfilePic: ANImageView = header.findViewById(R.id.userProfilePic)
        val userName: TextView = header.findViewById(R.id.userName)
        val userEmail: TextView = header.findViewById(R.id.userEmail)

        userProfilePic.setDefaultImageResId(R.mipmap.ic_launcher)
        userProfilePic.setErrorImageResId(R.mipmap.ic_launcher)
        userProfilePic.setImageUrl(ApiEndPoint.Pictures + Config.getCustom("foto", ""))

        userName.text = Config.getCustom("nama", "")
        userEmail.text = Config.getCustom("email", "")
    }

    private fun UpdateWoImage(){
        val Config = FConfig(requireContext())
        val loading = ProgressDialog(requireContext())
        loading.setTitle("Updating...")
        loading.setMessage("Saving data (0/100)")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Update)
                .addBodyParameter("id", Config.getCustom("id", ""))
                .addBodyParameter("nama", piew?.acNama?.text.toString())
                .addBodyParameter("email", piew?.acEmail?.text.toString())
                .addBodyParameter("pass", Config.getCustom("pass", ""))
                .addBodyParameter("alamat", piew?.acAlamat?.text.toString())
                .addBodyParameter("notelp", piew?.acNoTelp?.text.toString())
                .addBodyParameter("foto", Config.getCustom("foto", ""))
                .addBodyParameter("nip", Config.getCustom("nip", ""))
                .addBodyParameter("spesialis", Config.getCustom("spesialis", ""))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        if (response?.getString("message")?.contains("berhasil")!!) {
                            loading.setMessage("Saving data (100/100)")

                            piew?.btnAcEdit?.text = "Edit"
                            piew?.changePict?.visibility = View.INVISIBLE
                            piew?.btnAcCancel?.visibility = View.INVISIBLE

                            piew?.acNama?.isFocusable = false
                            piew?.acNama?.isFocusableInTouchMode = false
                            piew?.acEmail?.isFocusable = false
                            piew?.acEmail?.isFocusableInTouchMode = false
                            piew?.acAlamat?.isFocusable = false
                            piew?.acAlamat?.isFocusableInTouchMode = false
                            piew?.acNoTelp?.isFocusable = false
                            piew?.acNoTelp?.isFocusableInTouchMode = false

                            Config.setCustom("nama", response?.getString("nama_pengguna").toString())
                            Config.setCustom("email", response?.getString("email_pengguna").toString())
                            Config.setCustom("pass", response?.getString("password_pengguna").toString())
                            Config.setCustom("alamat", response?.getString("alamat").toString())
                            Config.setCustom("notelp", response?.getString("no_telp").toString())
                            Config.setCustom("foto", response?.getString("foto").toString())
                            Config.setCustom("nip", response?.getString("nip").toString())
                            Config.setCustom("spesialis", response?.getString("spesialis").toString())

                            piew?.acProfile?.setImageUrl("")
                            Data()
                            changeNavHead()
                        }
                        loading.dismiss()
                        Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_LONG).show()
                    }

                    override fun onError(anError: ANError?) {
                        loading.dismiss()
                        Log.d("OnError", anError?.errorDetail?.toString()!!)
                        Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG).show()
                    }

                })
    }

    private fun UpdateWiImage(file: File) {
        val Config = FConfig(requireContext())
        val loading = ProgressDialog(requireContext())
        loading.setTitle("Updating...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
                .addMultipartFile("image", file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                    loading.setMessage("Uploading image (" + (((bytesUploaded / totalBytes) * 100) - 20).toString() + "/100)")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                            AndroidNetworking.post(ApiEndPoint.Update)
                                    .addBodyParameter("id", Config.getCustom("id", ""))
                                    .addBodyParameter("nama", piew?.acNama?.text.toString())
                                    .addBodyParameter("email", piew?.acEmail?.text.toString())
                                    .addBodyParameter("pass", Config.getCustom("pass", ""))
                                    .addBodyParameter("alamat", piew?.acAlamat?.text.toString())
                                    .addBodyParameter("notelp", piew?.acNoTelp?.text.toString())
                                    .addBodyParameter("foto", response)
                                    .addBodyParameter("nip", Config.getCustom("nip", ""))
                                    .addBodyParameter("spesialis", Config.getCustom("spesialis", ""))
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject?) {
                                            if (response?.getString("message")?.contains("berhasil")!!) {
                                                loading.setMessage("Saving data (100/100)")

                                                piew?.btnAcEdit?.text = "Edit"
                                                piew?.changePict?.visibility = View.INVISIBLE
                                                piew?.btnAcCancel?.visibility = View.INVISIBLE

                                                piew?.acNama?.isFocusable = false
                                                piew?.acNama?.isFocusableInTouchMode = false
                                                piew?.acEmail?.isFocusable = false
                                                piew?.acEmail?.isFocusableInTouchMode = false
                                                piew?.acAlamat?.isFocusable = false
                                                piew?.acAlamat?.isFocusableInTouchMode = false
                                                piew?.acNoTelp?.isFocusable = false
                                                piew?.acNoTelp?.isFocusableInTouchMode = false

                                                Config.setCustom("nama", response?.getString("nama_pengguna").toString())
                                                Config.setCustom("email", response?.getString("email_pengguna").toString())
                                                Config.setCustom("pass", response?.getString("password_pengguna").toString())
                                                Config.setCustom("alamat", response?.getString("alamat").toString())
                                                Config.setCustom("notelp", response?.getString("no_telp").toString())
                                                Config.setCustom("foto", response?.getString("foto").toString())
                                                Config.setCustom("nip", response?.getString("nip").toString())
                                                Config.setCustom("spesialis", response?.getString("spesialis").toString())

                                                piew?.acProfile?.setImageUrl("")
                                                Data()
                                                changeNavHead()
                                            }
                                            loading.dismiss()
                                            Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_LONG).show()
                                        }

                                        override fun onError(anError: ANError?) {
                                            loading.dismiss()
                                            Log.d("OnError", anError?.errorDetail?.toString()!!)
                                            Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG).show()
                                        }

                                    })
                        } else {
                            loading.dismiss()
                            Toast.makeText(requireContext(), response, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        loading.dismiss()
                        Toast.makeText(requireContext(), "Error : " + anError.message, Toast.LENGTH_LONG).show()
                    }
                })
    }
}
