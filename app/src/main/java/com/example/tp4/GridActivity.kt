package com.example.tp4
import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tp4.databinding.ActivityCitiesBinding
import com.example.tp4.databinding.ActivityGridBinding

class GridActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGridBinding
    private val cities = arrayOf("Tunis", "Nabeul", "Sousse", "Sfax", "Tozeur")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGridBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, cities)
        binding.grid.adapter = adapter

        binding.grid.setOnItemClickListener { parent, view, position, id ->
            val selectedCity = cities[position]
            val searchUrl = "http://www.google.fr/search?q=$selectedCity"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl))

            startActivity(intent)

        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.grid) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}