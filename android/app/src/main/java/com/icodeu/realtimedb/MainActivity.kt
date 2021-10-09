package com.icodeu.realtimedb

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.icodeu.realtimedb.databinding.ActivityMainBinding
import com.icodeu.realtimedb.dto.CarListResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class MainActivity : AppCompatActivity(), CarAdapter.Interaction {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CarAdapter
    private val compositeDisposable = CompositeDisposable()
    private val vm: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = CarAdapter(this)
        setUpObserver()
    }

    private fun setUpObserver() {
        compositeDisposable.add(vm.getCars()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                setupRecyclerView(it)
            })
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setupRecyclerView(list: List<Car>) {
        binding.apply {
            adapter.submitList(list)
            rvMain.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!compositeDisposable.isDisposed){
            compositeDisposable.dispose()
        }
    }

    override fun onItemSelected(position: Int, item: Car) {

    }
}
