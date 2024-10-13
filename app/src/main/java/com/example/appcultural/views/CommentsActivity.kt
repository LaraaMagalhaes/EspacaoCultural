package com.example.appcultural.views

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcultural.R
import com.example.appcultural.adapters.CommentListAdapter
import com.example.appcultural.data.MockCommentRepository
import com.example.appcultural.databinding.ActivityCommentsBinding

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = MockCommentRepository()
        val data = repository.list()
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = CommentListAdapter(data)

        setSupportActionBar(binding.topAppBar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.home -> {
            finish()
            true
        }

        else -> {
            finish()
            true
        }
    }
}