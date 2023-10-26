package com.example.medical.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medical.R
import com.example.medical.adapters.BookAdapter
import com.example.medical.adapters.GenreAdapter
import com.example.medical.books.BookApi
import com.example.medical.databinding.FragmentHomeBinding
import com.example.medical.model.Book
import com.example.medical.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var books: ArrayList<Book>
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val shared = requireContext().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val gson = Gson()

        if (shared.getString("books", null) == null) {
            BookApi(requireContext()).saveAllBooksToShared()
        }

        binding.imageView8.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_personalInfoFragment)
        }

        var booksJson = shared.getString("books", null)
        books = gson.fromJson(booksJson, object : TypeToken<ArrayList<Book>>() {}.type)

        setGenresUi()

        val mainColor = ContextCompat.getColor(requireContext(), R.color.mainColor)
        val blackColor = ContextCompat.getColor(requireContext(), R.color.black)
        binding.filter.setColorFilter(blackColor, PorterDuff.Mode.SRC_ATOP)

        changeSavedBooksVisibility()
        changeWishBooksVisibility()

        binding.savedRecycler.adapter = BookAdapter(books.filter { it.isSaved } as ArrayList<Book>, R.layout.book_item, requireContext())
        binding.savedRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.wishlistRecycler.adapter = BookAdapter(books.filter { it.isWish } as ArrayList<Book>, R.layout.book_item, requireContext())

            binding.editText.addTextChangedListener {
                if (binding.editText.text.toString().isNotEmpty()) {
                    binding.others.visibility = View.GONE
                    var filterBooks: ArrayList<Book> = ArrayList()
                    for (i in books) {
                        if (i.name.lowercase().trim()
                                .contains(binding.editText.text.toString().lowercase().trim())
                        ) {
                            filterBooks.add(i)
                        }
                    }

                } else {
                    binding.others.visibility = View.VISIBLE
                }
            }

        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.editText.clearFocus()
            }
            false
        }

        binding.filter.setOnClickListener {
            findNavController().navigate(R.id.filterFragment)
        }

        var checkedString = shared.getString("radioCheckedText", null)
        var isFilter = shared.getBoolean("isFilter", false)

        var romance = shared.getBoolean("romance", false)
        var thriller = shared.getBoolean("thriller", false)
        var action = shared.getBoolean("action", false)

        if (isFilter) binding.filter.setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP)
        else binding.filter.setColorFilter(blackColor, PorterDuff.Mode.SRC_ATOP)

        if (isFilter) {
            var filterBooks = books
            if (checkedString == "4.5+") filterBooks = books.filter { it.rating >= 4.5 } as ArrayList<Book>
            if (checkedString == "4.0+") filterBooks = books.filter { it.rating >= 4.0 } as ArrayList<Book>
            if (romance || thriller || action) {
                if (!romance) filterBooks.removeAll { it.genreName == "Romance" }
                if (!thriller) filterBooks.removeAll { it.genreName == "Thriller" }
                if (!action) filterBooks.removeAll { it.genreName == "Action" }
            }


        } else {
            binding.others.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun getGenres(): ArrayList<Genre> {
        var genres = ArrayList<Genre>()
        genres.add(Genre("Romance", R.drawable.img_2))
        genres.add(Genre("Thriller", R.drawable.thriller))
        genres.add(Genre("Action", R.drawable.action))
        return genres
    }

    private fun setGenresUi() {
        binding.genreRecycler.adapter =
            GenreAdapter(getGenres(), object : GenreAdapter.MyInterface {
                override fun onItemTap(index: Int) {
                    val bundle = bundleOf("index" to index)
                    findNavController().navigate(R.id.genreBooksFragment, bundle)
                }
            })
        binding.genreRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }



    private fun changeSavedBooksVisibility() {
        if ((books.filter { it.isSaved } as ArrayList<Book>).isEmpty()) {
            binding.box1.visibility = View.VISIBLE
            binding.savedRecycler.visibility = View.GONE
        } else {
            binding.box1.visibility = View.GONE
            binding.savedRecycler.visibility = View.VISIBLE
        }
    }

    fun changeWishBooksVisibility() {
        if ((books.filter { it.isWish } as ArrayList<Book>).isEmpty()) {
            binding.box2.visibility = View.VISIBLE
            binding.wishlistRecycler.visibility = View.GONE
        } else {
            binding.box2.visibility = View.GONE
            binding.wishlistRecycler.visibility = View.VISIBLE
        }
    }
}