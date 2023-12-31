package com.example.medical.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.medical.R
import com.example.medical.databinding.FragmentSignUpBinding
import com.example.medical.model.User
import com.example.medical.model.UserToken
import com.example.medical.networking.ApiClient
import com.example.medical.networking.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
const val TAG = "TAG"
class SignUpFragment : Fragment() {
//    private var userList = mutableListOf<User>()
    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        val shared = requireContext().getSharedPreferences("shared", AppCompatActivity.MODE_PRIVATE)
        val edit = shared.edit()
        val gson = Gson()
        val convert = object : TypeToken<List<User>>() {}.type

        val dialogBinding = layoutInflater.inflate(R.layout.my_custom_dialog, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        binding.signUpRegBtn.setOnClickListener {
            var users = shared.getString("users", "")
//            if (users == "") {
//                userList = mutableListOf()
//            } else {
//                userList = gson.fromJson(users, convert)
//            }

            var user = User(
                binding.userNameR.text.toString(),
                binding.userNameR.text.toString(),
                binding.passwordR.text.toString(),
                binding.emailInputR.text.toString(),
            )

            if (validate()) {
//                userList.add(user)

//                val str = gson.toJson(userList)
//                edit.putString("users", str).apply()

                myDialog.show()
                val api = ApiClient.getInstance().create(ApiService::class.java)

                api.register(user).enqueue(object :Callback<UserToken>{
                    override fun onResponse(call: Call<UserToken>, response: Response<UserToken>) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            findNavController().navigate(R.id.mainFragment)
                            shared.edit().putString("active_user", gson.toJson(user)).apply()
                            myDialog.dismiss()
                        }, 1500)
                    }

                    override fun onFailure(call: Call<UserToken>, t: Throwable) {
                        Log.d(TAG, "onFailure: $t"
                        )
                    }

                })



            }
        }

        return binding.root
    }

    private fun validate(): Boolean {
//        if (binding.userNameR.text.toString().isEmpty() || binding.passwordR.text.toString().isEmpty() || binding.emailInputR.text.toString().isEmpty() || binding.confirmPasswordR.text.toString().isEmpty()
//        ) {
//            Toast.makeText(requireContext(), "Fill the gaps", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if (binding.passwordR.text.toString() != binding.confirmPasswordR.text.toString()) {
//            Toast.makeText(requireContext(), "Your password does not matched", Toast.LENGTH_SHORT).show()
//            return false
//        }

//        for (i in userList.indices) {
//            if (binding.userNameR.text.toString() == userList[i].username) {
//                Toast.makeText(requireContext(), "User with this username already registered", Toast.LENGTH_SHORT).show()
//                return false
//            }
//        }

        return true
    }
}