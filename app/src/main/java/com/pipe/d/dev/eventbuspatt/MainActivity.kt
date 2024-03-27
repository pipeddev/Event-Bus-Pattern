package com.pipe.d.dev.eventbuspatt

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pipe.d.dev.eventbuspatt.adapter.OnClickListener
import com.google.android.material.snackbar.Snackbar
import com.pipe.d.dev.eventbuspatt.adapter.ResultAdapter
import com.pipe.d.dev.eventbuspatt.dataAccess.getAdEventsInRealtime
import com.pipe.d.dev.eventbuspatt.dataAccess.getResultEventsInRealtime
import com.pipe.d.dev.eventbuspatt.dataAccess.someTime
import com.pipe.d.dev.eventbuspatt.databinding.ActivityMainBinding
import com.pipe.d.dev.eventbuspatt.eventBus.EventBus
import com.pipe.d.dev.eventbuspatt.services.SportService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupAdapter()
        setupRecyclerView()
        setupSwipeRefresh()
        setupClicks()
        setupSubscribers()
    }

    private fun setupAdapter() {
        adapter = ResultAdapter(this)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.srlResults.setOnRefreshListener {
            adapter.clear()
            getEvents()
            binding.btnAd.visibility = View.VISIBLE
        }
    }

    private fun setupClicks() {
        binding.btnAd.run {
            setOnClickListener {
                lifecycleScope.launch {
                    binding.srlResults.isRefreshing = true
                    val events = getAdEventsInRealtime()
                    EventBus.instance().publish(events.first())
                }

            }

            setOnLongClickListener { view ->
                lifecycleScope.launch {
                    binding.srlResults.isRefreshing = true
                    EventBus.instance().publish(SportEvent.CloseAdEvent)
                    view.visibility = View.GONE
                }
                true
            }
        }
    }

    private fun setupSubscribers() {
        lifecycleScope.launch {
            SportService.instance().setupSubscribers(this)
            EventBus.instance().subscribe<SportEvent> { event ->
                binding.srlResults.isRefreshing = false
                when(event) {
                    is SportEvent.ResultSuccess -> adapter.add(event)
                    is SportEvent.ResultError -> Snackbar.make(binding.root, "Code: ${event.msg}, Message: ${event.msg}", Snackbar.LENGTH_LONG).show()
                    is SportEvent.AdEvent -> {
                        Toast.makeText(this@MainActivity, "Ad Click, Send data to server", Toast.LENGTH_LONG).show()
                    }
                    is SportEvent.CloseAdEvent -> binding.btnAd.visibility = View.GONE
                    is SportEvent.SaveEvent -> Toast.makeText(this@MainActivity, "Guardado", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun getEvents() {

        lifecycleScope.launch {
            val events = getResultEventsInRealtime()
            events.forEach { event ->
                delay(someTime())
                EventBus.instance().publish(event)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.srlResults.isRefreshing = true
        getEvents()
    }

    /*
        OnclickListener
     */
    override fun onClick(result: SportEvent.ResultSuccess) {
        lifecycleScope.launch {
            binding.srlResults.isRefreshing = true
            //EventBus.instance().publish(SportEvent.SaveEvent)
            SportService.instance().saveResult(result)

        }
    }

}