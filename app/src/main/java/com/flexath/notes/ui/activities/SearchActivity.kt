package com.flexath.notes.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flexath.notes.R

class SearchActivity : AppCompatActivity() {

    companion object {
        fun newIntentSearch(context: Context) : Intent {
            return Intent(context,SearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}