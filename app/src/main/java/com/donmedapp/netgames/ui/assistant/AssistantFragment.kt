package com.donmedapp.netgames.ui.assistant

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.donmedapp.netgames.R
import com.donmedapp.netgames.data.entity.Page
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_assistant.*

/**
 * A simple [Fragment] subclass.
 */
class AssistantFragment : Fragment(R.layout.fragment_assistant) {


    private lateinit var assistantAdapter: AssistantFragmentAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAdapter()
        setupViewPager()
        setupTabLayout()
        setupAppBar()
    }

    private fun setupTabLayout() {
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

    private fun setupAdapter() {
        val pageList: ArrayList<Page> = arrayListOf()
        pageList.add(
            Page(
                R.drawable.iconotoolbar,
                getString(R.string.page1),
                R.color.blueLight
            )
        )
        pageList.add(
            Page(
                R.drawable.ic_people_black_24dp,
                getString(R.string.page2),
                R.color.colorPrimary
            )
        )
        pageList.add(
            Page(
                R.drawable.ic_home_black_24dp,
                getString(R.string.page3),
                R.color.message
            )
        )
        pageList.add(
            Page(
                R.drawable.ic_search_black_24dp,
                getString(R.string.page4),
                R.color.orange
            )
        )

        pageList.add(
            Page(
                R.drawable.ic_person_black_24dp,
                getString(R.string.page5),
                R.color.marron
            )
        )

        pageList.add(
            Page(
                R.drawable.ic_sentiment_very_satisfied_black_24dp,
                getString(R.string.page6),
                R.color.violet
            )
        )

        assistantAdapter = AssistantFragmentAdapter(pageList).also { it.onItemClickListener = { activity?.onBackPressed()}}
    }


    private fun setupViewPager() {
        viewPager.run {
            adapter = assistantAdapter
        }

    }


    private fun setupAppBar() {


    }
}
