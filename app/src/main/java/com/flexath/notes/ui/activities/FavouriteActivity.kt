package com.flexath.notes.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flexath.notes.R

class FavouriteActivity : AppCompatActivity() {

    companion object {
        fun newIntentSearch(context: Context) : Intent {
            return Intent(context,FavouriteActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
    }
}