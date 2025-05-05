package com.example.tp4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp4.databinding.ActivityWorldBinding

class WorldActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorldBinding
    private var selectedContinent: String = ""
    private var selectedCountry: String = ""

    // Flags to track user selections
    private var continentSelectedByUser = false
    private var countrySelectedByUser = false

    // Flag to ignore initial country spinner selection events
    private var isCountrySpinnerInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup continents spinner
        val continentsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.continents,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinner.adapter = continentsAdapter

        // Set initial countries for first continent without showing toast
        updateCountriesSpinner(0)
        selectedContinent = resources.getStringArray(R.array.continents)[0]

        // Handle continent selection changes
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedContinent = parent.getItemAtPosition(position).toString()
                continentSelectedByUser = true
                countrySelectedByUser = false // reset country selection flag because countries change
                isCountrySpinnerInitialized = false // reset country spinner initialization flag
                updateCountriesSpinner(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle no selection
            }
        }

        // Handle country selection changes
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedCountry = parent.getItemAtPosition(position).toString()

                if (!isCountrySpinnerInitialized) {
                    // Ignore the initial selection event triggered by adapter setup
                    isCountrySpinnerInitialized = true
                    return
                }

                countrySelectedByUser = true

                // Show toast only after both selections are user-made
                if (continentSelectedByUser && countrySelectedByUser) {
                    showSelectionToast()
                }

                // Encode country name for URL
                val countryEncoded = Uri.encode(selectedCountry.replace(" ", "_").lowercase())
                val url = "https://fr.wikipedia.org/wiki/$countryEncoded"

                // Open browser with the Wikipedia page
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle no selection
            }
        }
    }

    private fun updateCountriesSpinner(continentPosition: Int) {
        val countriesArrayRes = when (continentPosition) {
            0 -> R.array.pays_afr
            1 -> R.array.pays_eur
            2 -> R.array.pays_asie
            3 -> R.array.pays_oc
            4 -> R.array.pays_am
            else -> R.array.pays_afr // default
        }

        ArrayAdapter.createFromResource(
            this,
            countriesArrayRes,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner2.adapter = it
            // Set initial country selection when continent changes, but do NOT show toast here
            selectedCountry = resources.getStringArray(countriesArrayRes)[0]
        }
    }

    private fun showSelectionToast() {
        Toast.makeText(
            this,
            "Continent $selectedContinent; Pays $selectedCountry",
            Toast.LENGTH_SHORT
        ).show()
    }
}
