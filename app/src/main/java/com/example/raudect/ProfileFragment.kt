package com.example.raudect

import android.Manifest
import android.content.pm.PackageManager
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    //to contain uri for photo
    private var photoUri: Uri? = null

    //image binder
    private lateinit var profileBinder: ImageView

    //init db & auth
    private lateinit var userRef: DatabaseReference
    private lateinit var transactionRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    //launcher for camera intent
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                profileBinder.setImageURI(photoUri)
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

        //database & auth instantiation
        userRef = FirebaseDatabase.getInstance().getReference("users")
        transactionRef = FirebaseDatabase.getInstance().getReference("transaction_info")
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        //get username from database
        var username = "User";
        userRef.child(user?.uid.toString()).get()
            .addOnSuccessListener { user->
                username = user.child("username").getValue().toString()
                greeting.text =
                    resources.getString(
                        R.string.profileFragment_textView_greeting_string,
                        username
                    )
                editUsername.setText(username)
            }

        //set for submitting changes
        view.findViewById<Button>(R.id.profileFragment_button_submit)
            .setOnClickListener {
                val inputUsername = editUsername.text.toString()
                userRef.child(user?.uid.toString()).child("username")
                    .setValue(inputUsername)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Successfully updated username!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e->
                        Toast.makeText(context, "Failed to update data: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }

        //set for deleting everything related to user
        view.findViewById<Button>(R.id.profileFragment_button_delete)
            .setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Deleting User Data")
                builder.setMessage("Are you sure you want to delete data related to you in this app?")
                builder.setPositiveButton("Yes"){ dialog, _->
                    val query = transactionRef.orderByChild("uid").equalTo(user?.uid)
                    query.get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                val updates = mutableMapOf<String, Any?>()
                                //action on all row with the same uid as user
                                for (transaction in snapshot.children) {
                                    updates[transaction.key!!] = null
                                }
                                transactionRef.updateChildren(updates)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "data related to user deleted", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e->
                                        Toast.makeText(context, "Failed to delete data: ${e.message}!", Toast.LENGTH_LONG).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e->
                            Toast.makeText(context, "Failed to query: ${e.message}!", Toast.LENGTH_LONG).show()
                        }
                    dialog.dismiss()
                }
                builder.setNegativeButton("No"){dialog, _->
                    dialog.dismiss()
                }

                builder.show()
            }

        //setting image binder
        profileBinder = view.findViewById<ImageView>(R.id.profileFragment_profilePicture)

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


        //get img on loading up fragment
        val fileDir = File(requireContext().filesDir, "images/profile_picture.jpg")

        //if exist, get uri and bind image
        if (fileDir.exists()) {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                fileDir
            )
            profileBinder.setImageURI(uri)
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
        val photoDir = makeFileStorage() //this is in File class type
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoDir
        )
        takePicture.launch(photoUri) //save camera result to the uri
        profileBinder.invalidate()
        Glide.with(requireContext())
            .load(photoUri)
            .into(profileBinder)
    }

    //create/accessing/finding the internal storage for images with profile_picture name
    private fun makeFileStorage(): File {
        val name = "profile_picture.jpg"
        val fileDir = File(requireContext().filesDir, "images")

        //if does not exist, create profile_picture.jpg
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        Log.d("ProfileFrag", "makeFileStorage()")
        return File(fileDir, name)
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