package com.ilnur.cardsnew.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilnur.cardsnew.R
import com.ilnur.cardsnew.adapter.SubjectAdapter
import com.ilnur.cardsnew.database.Subject
import com.ilnur.cardsnew.viewmodel.MainViewModel
import com.ilnur.cardsnew.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()
    /*override fun onCreate(savedInstanceState: Bundle?) {
        //this.fragmentFactory = KoinFragmentFactory()
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subject, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.recycler_subj)
            .also { it.layoutManager = LinearLayoutManager(requireContext()) }
        val adater = SubjectAdapter(
            requireContext(),
            viewModel.subjs(),
            object : SubjectAdapter.ItemSelectedListener {
                override fun onItemSelected(item: Subject) {
                    if (viewModel.subjects.value!!.find { it.href == item.href}!!.isAdded) {
                        Log.d("itemSelect", item.toString() + "selected Recycler")
                        viewModel.selectSubject(item)
                    } else {
                        Toast.makeText(context, "Этот предмет еще не загружен", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        rv.adapter = adater
    }
}