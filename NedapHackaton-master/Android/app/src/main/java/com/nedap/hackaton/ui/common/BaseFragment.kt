package com.nedap.hackaton.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nedap.hackaton.Hackaton
import com.nedap.hackaton.model.Model

abstract class BaseFragment : Fragment() {
    lateinit var model: Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = (activity?.application as Hackaton).model
    }
}