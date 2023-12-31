package com.example.medical.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.medical.R
import com.example.medical.databinding.FragmentSignInBinding
import com.example.medical.model.Login
import com.example.medical.model.User
import com.example.medical.model.UserToken
import com.example.medical.networking.ApiClient
import com.example.medical.networking.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment() {
//    private var userList = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        val shared = requireContext().getSharedPreferences("shared", AppCompatActivity.MODE_PRIVATE)
//        val gson = Gson()
//        val convert = object : TypeToken<List<User>>() {}.type
//        val users = shared.getString("users", "")

        binding.signInBtn.setOnClickListener {
//            if (users != "") {
//                userList = gson.fromJson(users, convert)
//            }

//            for (user in userList) {
//                if ((binding.usernameOrEmail.text.toString() == user.username || binding.usernameOrEmail.text.toString() == user.email) && binding.password.text.toString() == user.password) {
//                    Toast.makeText(requireContext(), "Successfully logged in", Toast.LENGTH_SHORT).show()
//
//                    findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
//
//                    shared.edit().putBoolean("isLoggedOut", false).apply()
//                    shared.edit().putString("active_user", gson.toJson(user)).apply()
//
//                    return@setOnClickListener
//                }
//            }

            val api = ApiClient.getInstance().create(ApiService::class.java)
            
            val login = Login(binding.usernameOrEmail.text.toString(),binding.password.text.toString())
            
            api.login(login).enqueue(object :Callback<UserToken>{
                override fun onResponse(call: Call<UserToken>, response: Response<UserToken>) {
                    findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
                }

                override fun onFailure(call: Call<UserToken>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }

            })

            Toast.makeText(
                requireContext(),
                "Username or password is incorrect",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }
}