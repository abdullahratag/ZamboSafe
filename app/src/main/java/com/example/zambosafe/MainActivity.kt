package com.example.zambosafe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zambosafe.data.AgencyRepository
import com.example.zambosafe.model.Agency
import com.example.zambosafe.ui.AgencyAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

/**
 * The single screen of ZamboSafe: search bar + category chips + agency list.
 *
 * Flow: load JSON -> build category chips -> applyFilter() whenever the
 * search text or selected chip changes.
 */
class MainActivity : AppCompatActivity() {

    private val adapter = AgencyAdapter()

    private var allAgencies: List<Agency> = emptyList()
    private var searchQuery: String = ""
    private var selectedCategory: String? = null // null = "All"

    private lateinit var chipGroup: ChipGroup
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        chipGroup = findViewById(R.id.categoryChips)
        emptyText = findViewById(R.id.emptyText)

        findViewById<RecyclerView>(R.id.agencyList).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        findViewById<TextInputEditText>(R.id.searchInput).doAfterTextChanged {
            searchQuery = it?.toString().orEmpty()
            applyFilter()
        }

        allAgencies = AgencyRepository.load(this)
        buildCategoryChips()
        applyFilter()
    }

    /** One "All" chip + one chip per category found in the data (in JSON order). */
    private fun buildCategoryChips() {
        val categories = allAgencies.map { it.category }.distinct()

        addChip(getString(R.string.category_all), null)
        categories.forEach { addChip(it, it) }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            selectedCategory = chip?.tag as? String
            applyFilter()
        }
        chipGroup.check(chipGroup.getChildAt(0).id) // select "All"
    }

    private fun addChip(label: String, category: String?) {
        val chip = LayoutInflater.from(this)
            .inflate(R.layout.item_category_chip, chipGroup, false) as Chip
        chip.id = View.generateViewId()
        chip.text = label
        chip.tag = category
        chipGroup.addView(chip)
    }

    /** Applies the selected category AND the search text, then refreshes the list. */
    private fun applyFilter() {
        val filtered = allAgencies.filter { agency ->
            (selectedCategory == null || agency.category == selectedCategory) &&
                agency.matches(searchQuery)
        }
        adapter.submitList(filtered)
        emptyText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }
}
