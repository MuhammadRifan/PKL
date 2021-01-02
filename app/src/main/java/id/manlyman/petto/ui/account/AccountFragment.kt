package id.manlyman.petto.ui.account

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
        var error = 0
        piew = root
        Data()

        val Config = FConfig(requireContext())
        val edit = root.btnAcEdit
        val floatButt = root.changePict
        val cancel = root.btnAcCancel
        val nama = root.acNama
        val email = root.acEmail
        var pass = ""
        val nPass = root.acNewPass
        val cPass = root.acConfirmPass
        val telp = root.acNoTelp

        val Lsrtv = root.acSRTVLayout
        val srtv = root.acSRTV
        var Nsrtv = ""

        val level = Config.getCustom("level", "")

        if (level == "1") {
            Lsrtv.visibility = View.VISIBLE
        } else {
            Lsrtv.visibility = View.GONE
        }

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
                nPass.isFocusable = true
                nPass.isFocusableInTouchMode = true
                cPass.isFocusable = true
                cPass.isFocusableInTouchMode = true
                telp.isFocusable = true
                telp.isFocusableInTouchMode = true

                if (level == "1") {
                    srtv.isFocusable = true
                    srtv.isFocusableInTouchMode = true
                }
            } else {
                error = 0
                pass = ""

                if (nama.text.toString().length < 3) {
                    nama.error = "Mohon masukan nama dengan benar"
                    error++
                }

                if (email.text.toString().isEmpty()) {
                    email.error = "Mohon masukan email"
                    error++
                } else if (!email.text.toString().trim().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.com+"))) {
                    email.error = "Mohon masukkan email dengan benar"
                    error++
                }

                if (nPass.text.toString().isNotEmpty() || cPass.text.toString().isNotEmpty()) {
                    if (nPass.text.toString().length < 5) {
                        nPass.error = "Mohon masukan password dengan benar"
                        error++
                    } else if (cPass.text.toString().length < 5) {
                        cPass.error = "Mohon masukan password dengan benar"
                        error++
                    } else if (!nPass.text.toString().equals(cPass.text.toString())) {
                        cPass.error = "Password baru tidak sama"
                        error++
                    } else {
                        pass = nPass.text.toString()
                    }
                } else {
                    pass = Config.getCustom("pass", "")
                }

                if (telp.text.toString().length != 12) {
                    telp.error = "Mohon masukan nomor telfon dengan benar"
                    error++
                }

                if (level == "1") {
                    if (srtv.text.toString().length < 3) {
                        srtv.error = "Mohon masukan STRV dengan benar"
                        error++
                    } else {
                        Nsrtv = srtv.text.toString()
                    }
                } else {
                    Nsrtv = Config.getCustom("srtv", "")
                }

                if (error == 0) {
                    if (pict == 0) {
                        UpdateWoImage(pass, Nsrtv)
                    } else {
                        UpdateWiImage(pass, Nsrtv, imageFile!!)
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
            nama.error = null
            email.isFocusable = false
            email.isFocusableInTouchMode = false
            email.error = null
            nPass.isFocusable = false
            nPass.isFocusableInTouchMode = false
            nPass.error = null
            nPass.text = null
            cPass.isFocusable = false
            cPass.isFocusableInTouchMode = false
            cPass.error = null
            cPass.text = null
            telp.isFocusable = false
            telp.isFocusableInTouchMode = false
            telp.error = null

            if (level == "1") {
                srtv.isFocusable = false
                srtv.isFocusableInTouchMode = false
                srtv.error = null
            }
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

        piew?.acProfile?.setDefaultImageResId(R.drawable.ic_launcher_background)
        piew?.acProfile?.setErrorImageResId(R.drawable.ic_launcher_background)
        piew?.acProfile?.setImageUrl(ApiEndPoint.Pictures + Config.getCustom("picture", ""))

        piew?.acNama?.setText(Config.getCustom("uname", ""))
        piew?.acEmail?.setText(Config.getCustom("email", ""))
        piew?.acNoTelp?.setText(Config.getCustom("phone", ""))

        if (Config.getCustom("level", "") == "1") {
            if (Config.getCustom("srtv", "") == "null") {
                piew?.acSRTV?.setText("0")
            } else {
                piew?.acSRTV?.setText(Config.getCustom("srtv", ""))
            }
        }

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
        userProfilePic.setImageUrl(ApiEndPoint.Pictures + Config.getCustom("picture", ""))

        userName.text = Config.getCustom("uname", "")
        userEmail.text = Config.getCustom("email", "")
    }

    private fun restoreBack(response: JSONObject){
        val Config = FConfig(requireContext())
        piew?.btnAcEdit?.text = "Edit"
        piew?.changePict?.visibility = View.INVISIBLE
        piew?.btnAcCancel?.visibility = View.INVISIBLE

        piew?.acNama?.isFocusable = false
        piew?.acNama?.isFocusableInTouchMode = false
        piew?.acEmail?.isFocusable = false
        piew?.acEmail?.isFocusableInTouchMode = false
        piew?.acNewPass?.isFocusable = false
        piew?.acNewPass?.isFocusableInTouchMode = false
        piew?.acNewPass?.text = null
        piew?.acConfirmPass?.isFocusable = false
        piew?.acConfirmPass?.isFocusableInTouchMode = false
        piew?.acConfirmPass?.text = null
        piew?.acNoTelp?.isFocusable = false
        piew?.acNoTelp?.isFocusableInTouchMode = false

        if (Config.getCustom("level", "") == "1") {
            piew?.acSRTV?.isFocusable = false
            piew?.acSRTV?.isFocusableInTouchMode = false

            Config.setCustom("srtv", response.getString("srtv").toString())
        }

        Config.setCustom("uname", response.getString("username").toString())
        Config.setCustom("email", response.getString("email").toString())
        Config.setCustom("pass", response.getString("pass").toString())
        Config.setCustom("phone", response.getString("phone").toString())
        Config.setCustom("picture", response.getString("picture").toString())

        piew?.acProfile?.setImageUrl("")
        Data()
        changeNavHead()
    }

    private fun UpdateWoImage(pass: String, Nsrtv: String){
        val Config = FConfig(requireContext())
        val loading = ProgressDialog(requireContext())
        loading.setTitle("Updating ...")
        loading.setMessage("Saving data ...")
        loading.show()

        AndroidNetworking.post(ApiEndPoint.Update)
            .addBodyParameter("id", Config.getCustom("uname", ""))
            .addBodyParameter("nama", piew?.acNama?.text.toString())
            .addBodyParameter("email", piew?.acEmail?.text.toString())
            .addBodyParameter("pass", pass)
            .addBodyParameter("notelp", piew?.acNoTelp?.text.toString())
            .addBodyParameter("foto", Config.getCustom("picture", ""))
            .addBodyParameter("strv", Nsrtv)
            .addBodyParameter("level", Config.getCustom("level", ""))
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    if (response?.getString("message")?.contains("berhasil")!!) {
                        loading.setMessage("Saving data ...")

                        restoreBack(response)
                    }
                    loading.dismiss()
                    Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_LONG).show()
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                    Log.d("OnError","Bromance")
                    Toast.makeText(requireContext(), "Connection Error", Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun UpdateWiImage(pass: String, Nsrtv: String, file: File) {
        val Config = FConfig(requireContext())
        val loading = ProgressDialog(requireContext())
        loading.setTitle("Updating ...")
        loading.setMessage("Uploading image (0/100)")
        loading.show()

        AndroidNetworking.upload(ApiEndPoint.Upload)
            .addMultipartFile("image", file)
            .addMultipartParameter("folder", "img_user")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes -> // do anything with progress
                loading.setMessage("Uploading image (" + ((bytesUploaded / totalBytes) * 100).toString() + "/100)")
            }
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    if (!response.contains("Error") && !response.contains("Gagal") && !response.contains("File")) {
                        AndroidNetworking.post(ApiEndPoint.Update)
                            .addBodyParameter("id", Config.getCustom("uname", ""))
                            .addBodyParameter("nama", piew?.acNama?.text.toString())
                            .addBodyParameter("email", piew?.acEmail?.text.toString())
                            .addBodyParameter("pass", pass)
                            .addBodyParameter("notelp", piew?.acNoTelp?.text.toString())
                            .addBodyParameter("foto", response)
                            .addBodyParameter("strv", Nsrtv)
                            .addBodyParameter("level", Config.getCustom("level", ""))
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(object : JSONObjectRequestListener {
                                override fun onResponse(response: JSONObject?) {
                                    if (response?.getString("message")?.contains("berhasil")!!) {
                                        loading.setMessage("Saving data ...")

                                        restoreBack(response)
                                    }
                                    loading.dismiss()
                                    Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_LONG).show()
                                }

                                override fun onError(anError: ANError?) {
                                    loading.dismiss()
                                    Log.d("OnError", anError?.errorDetail?.toString()!!)
                                    Log.d("OnError","Brodor")
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
