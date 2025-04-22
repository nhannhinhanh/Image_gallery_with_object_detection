package com.example.imagedetection

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagedetection.adapter.DetectionAdapter
import com.example.imagedetection.databinding.ActivityMainBinding
import com.example.imagedetection.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ViewModel by viewModels()
    private val detectionAdapter by lazy { DetectionAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeSearch()
        observeData()
        observeLoadStates()
    }

    private fun initViews() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = detectionAdapter
        }
    }

    private fun observeSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchClick()
                true
            } else false
        }
        binding.btnSearch.setOnClickListener { onSearchClick() }
    }

    private fun onSearchClick() {
        val query = binding.etSearch.text.toString().trim()
        if (query.isBlank()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.searchImages(query)
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.imageFlow.collectLatest { data ->
                detectionAdapter.submitData(data)
            }
        }
    }

    private fun observeLoadStates() {
        lifecycleScope.launch {
            detectionAdapter.loadStateFlow.collectLatest { loadState ->
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading

                (loadState.refresh as? LoadState.Error)?.let {
                    showError("Error loading images", it.error)
                }

                (loadState.append as? LoadState.Error)?.let {
                    showError("Error loading more images", it.error)
                }
            }
        }
    }

    private fun showError(message: String, error: Throwable) {
        Toast.makeText(this, "$message: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}
