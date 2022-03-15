package com.example.android.politicalpreparedness.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, elections: LiveData<List<Election>>) {
    view.visibility = if (elections.value?.isNotEmpty() == true) View.GONE else View.VISIBLE
}