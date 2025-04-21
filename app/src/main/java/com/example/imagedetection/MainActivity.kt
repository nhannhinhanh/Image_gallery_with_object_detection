
package com.example.imagedetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagedetection.adapter.ImageAdapter
import com.example.imagedetection.databinding.ActivityMainBinding // Import ViewBinding class
import com.example.imagedetection.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate layout bằng ViewBinding
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        handleLoadingStates()
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter()
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = imageAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
        binding.btnSearch.setOnClickListener { performSearch() }
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.searchImages(query)
            binding.recyclerView.scrollToPosition(0)
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.imageFlow.collectLatest { pagingData ->
                imageAdapter.submitData(pagingData)
            }
        }
    }

    private fun handleLoadingStates() {
        lifecycleScope.launch {
            imageAdapter.loadStateFlow.collectLatest { loadStates ->
                val isLoading = loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                binding.progressBar.isVisible = isLoading

                loadStates.refresh.let { state ->
                    if (state is LoadState.Error) {
                        showError("Error loading images", state.error)
                    }
                }

                loadStates.append.let { state ->
                    if (state is LoadState.Error) {
                        showError("Error loading more images", state.error)
                    }
                }
            }
        }
    }

    private fun showError(message: String, error: Throwable) {
        Toast.makeText(this, "$message: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}
