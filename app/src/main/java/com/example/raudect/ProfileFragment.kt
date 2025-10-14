package com.example.raudect

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.FileProvider
import java.io.File
import com.bumptech.glide.Glide

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //to contain uri for photo
    private var photoUri: Uri? = null

    //image binder
    private lateinit var profileBinder: ImageView

    //launcher for camera intent
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success && photoUri != null) {
            profileBinder.setImageURI(photoUri)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.Base_Theme_Raudect)
        val inflaterThemed = inflater.cloneInContext(contextThemeWrapper)
        return inflaterThemed.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.profileFragment_textView_greeting_id).text =
            resources.getString(R.string.profileFragment_textView_greeting_string,
                "Username")

        //setting image binder
        profileBinder = view.findViewById<ImageView>(R.id.profileFragment_profilePicture)

        //get img on loading up fragment
        val fileDir = File(requireContext().filesDir, "images/profile_picture.jpg")
        if (fileDir.exists()) {
            val uri =  FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                fileDir
            )
            profileBinder.setImageURI(uri)
        }

        //set on click btn for setting profile picture
        view.findViewById<Button>(R.id.profileFragment_button_editPicture_id)
            .setOnClickListener {
                takePhoto()
            }
    }











    //function for calling camera
    private fun takePhoto() {
        val photoDir = makeFileStorage() //this is in File class type
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoDir
        )
        takePicture.launch(photoUri) //save camera result to the uri
        profileBinder.setImageURI(null)
        Glide.with(requireContext())
            .load(photoUri)
            .into(profileBinder)
    }

    //create/accessing/finding the internal storage for images with profile_picture name
    private fun makeFileStorage(): File {
        val name = "profile_picture.jpg"
        val fileDir = File(requireContext().filesDir,"images")

        //if does not exist, create profile_picture.jpg
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        Log.d("ProfileFrag", "makeFileStorage()")
        return File(fileDir,name)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}