package com.ilnur.cardsnew.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.raiflatbutton.RaiflatButton
import com.github.rubensousa.raiflatbutton.RaiflatImageButton
import com.google.android.material.appbar.AppBarLayout
import com.ilnur.cardsnew.R
import com.ilnur.cardsnew.viewmodel.MainViewModel
import com.ilnur.cardsnew.viewmodel.TopicsViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout

@AndroidEntryPoint
class TopicsFragment : Fragment() {

    val sharedModel: MainViewModel by activityViewModels()
    val viewModel: TopicsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subject, null)

        viewModel.getTopics(sharedModel.currentSubj.value!!.href) //get list of topics

        initViews()

        val rv: RecyclerView = view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(view.context)
        }
        setAdapter(rv)




        return view
    }

    fun setAdapter(rv: RecyclerView) {

    }

    fun initViews() {
        val collTool: CollapsingToolbarLayout =
            requireActivity().findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).apply {
                title = sharedModel.currentSubj.value?.title
                expandedTitleMarginBottom = resources.getDimension(R.dimen.margin_title_col).toInt()
                setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
                setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
            }
        val learn =
            collTool.findViewById<RaiflatButton>(R.id.learn).apply { visibility = View.GONE }
        val watch =
            collTool.findViewById<RaiflatButton>(R.id.watch).apply { visibility = View.GONE }
        val rever =
            collTool.findViewById<RaiflatImageButton>(R.id.rever).apply { visibility = View.GONE }

        val appBar = requireActivity().findViewById<AppBarLayout>(R.id.appbar).apply {
            setExpanded(false)
            isClickable = false
        }
    }
}