package com.nedap.hackaton.ui.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nedap.hackaton.Hackaton
import com.nedap.hackaton.model.Model

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var model: Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = (application as Hackaton).model
    }
}
