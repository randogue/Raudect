package com.example.raudect

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import java.io.File
import com.example.raudect.model.profile.ProfileViewModel
import com.example.raudect.model.profile.ProfileViewModelFactory
import com.example.raudect.model.repository.FirebaseDatabaseRepository
import com.example.raudect.model.repository.ProfileRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(FirebaseDatabaseRepository(), ProfileRepository(requireContext()))
    }

    //image view holder early declaration
    private lateinit var imageView: ImageView

    private lateinit var currentPhotoFile: File
    private lateinit var currentPhotoUri: Uri

    //launcher for camera intent
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                profileViewModel.onPhotoTaken(currentPhotoFile)
            }
        }

    //launcher for permission request
    private lateinit var permissionRequestLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        //view holding for database related stuff
        val greeting = view.findViewById<TextView>(R.id.profileFragment_textView_greeting_id)
        val editUsername = view.findViewById<TextInputEditText>(R.id.profileFragment_textInput_editUsername_id)

        //view holding for photo related stuff
        imageView = view.findViewById<ImageView>(R.id.profileFragment_profilePicture)

        //check if there is already a picture, then load it
        profileViewModel.loadProfilePicture()


        //to catch message from vm
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                profileViewModel.toast.collect { message->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }


        profileViewModel.profilePictureFile.observe(viewLifecycleOwner) { file ->
            file?.let {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    it
                )
                imageView.setImageURI(uri)
            }
        }

        //form retention
        profileViewModel.usernameInput.observe(viewLifecycleOwner){ username->
            if (editUsername.text.toString() != username){
                editUsername.setText(username)
            }
        }
        editUsername.addTextChangedListener { username->
            profileViewModel.setUsernameInput(username.toString())
        }

        //greetings
        profileViewModel.username.observe(viewLifecycleOwner){username->
            greeting.text =
                resources.getString(
                    R.string.profileFragment_textView_greeting_string,
                    username
                )
        }

        //set for submitting changes
        view.findViewById<Button>(R.id.profileFragment_button_submit)
            .setOnClickListener {
                profileViewModel.changeUsername()
            }

        //setting launcher for permission request
        permissionRequestLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) { //permission is or already granted, take photo
                    takePhoto()
                } else { //if not, show rationale dialog
                    showPermissionRationale {
                        //if user clicked ok in showPermissionRationale, ask system for permission.
                        permissionRequestLauncher.launch(Manifest.permission.CAMERA) //ask system
                        //else
                    }
                }
            }


        //set on click btn for setting profile picture
        view.findViewById<Button>(R.id.profileFragment_button_editPicture_id)
            .setOnClickListener {
                //check for permission
                when {
                    checkPermissionCamera() -> takePhoto() //take photo if permission was granted
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> { //shouldShowRequestPermissionRationale(permission) is used to check whether you must show rationale before asking system for permission
                        showPermissionRationale {
                            permissionRequestLauncher.launch(Manifest.permission.CAMERA) //ask system for camera permission and input it to manifest
                        }
                    }

                    else -> permissionRequestLauncher.launch(Manifest.permission.CAMERA) //ask for permission
                }
            }
    }


    //function for calling camera
    private fun takePhoto() {
        currentPhotoFile = profileViewModel
            .let { (it as ProfileViewModel) }
            .let {
                // delegate creation to repository via ViewModel
                ProfileRepository(requireContext().applicationContext)
                    .createProfilePictureFile()
            }

        currentPhotoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            currentPhotoFile
        )

        takePicture.launch(currentPhotoUri)
    }

    //function to show permission rationale for camera permission
    private fun showPermissionRationale(positiveAction: () -> Unit) {
        //make pop up alert for asking required permission from user
        AlertDialog.Builder(requireContext())
            .setTitle("Camera Permission Required")
            .setMessage("camera is required for this app to add a profile picture. Profile picture will be uploaded to Firebase Database.")
            .setPositiveButton(android.R.string.ok) { _, _ -> positiveAction() }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    //function for checking camera permission
    private fun checkPermissionCamera() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

}