package com.sevenpeakssoftware.vishalr.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.sevenpeakssoftware.vishalr.R
import com.sevenpeakssoftware.vishalr.databinding.FragmentCarListBinding
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.utils.Constant.NETWORK_TAG
import com.sevenpeakssoftware.vishalr.utils.NetworkChangeReceiver
import com.sevenpeakssoftware.vishalr.utils.NetworkUtilWorker
import com.sevenpeakssoftware.vishalr.viewmodel.CarListViewModel

class CarListFragment : Fragment(), NetworkChangeReceiver.ConnectivityReceiverListener {
    private lateinit var binding: FragmentCarListBinding
    private lateinit var viewModel: CarListViewModel
    private lateinit var snackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkChangeReceiver.connectivityReceiverListener = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentCarListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerNetworkingBroadcast()
        viewModel = ViewModelProvider(this).get(CarListViewModel::class.java)
        // setupRecyclerView()
        // viewModel.getAllCarsLocally(requireContext())
        // viewModel.downloadLatestCars(requireContext())
        // if (CarsUtils.isOnline(requireContext())) {
        //     setupWorkManagerForNetworkCall()
        // } else {
        //     viewModel.getAllCarsLocally(requireContext())
        //     viewModel.setProgressVisibility(false)
        // }
        observeData()
    }

    private fun registerNetworkingBroadcast() {
        requireContext().registerReceiver(NetworkChangeReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(NetworkChangeReceiver())
    }

    private fun setupWorkManagerForNetworkCall() {
        val wm = WorkManager.getInstance(requireContext())
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val fetchCarsFromApi = OneTimeWorkRequestBuilder<NetworkUtilWorker>()
            .setConstraints(constraint)
            .addTag(NETWORK_TAG)
            .build()
        wm.enqueue(fetchCarsFromApi)
        wm.getWorkInfosByTagLiveData(NETWORK_TAG).observe(viewLifecycleOwner) { workInfo ->
            val finish = workInfo[0].state.isFinished
            if (finish) {
                // progress bar off
                viewModel.getAllCarsLocally(requireContext())
                viewModel.setProgressVisibility(false)
            } else {
                // Show progress bar
                viewModel.setProgressVisibility(true)
            }
        }
    }

    private fun observeData() {
        // View model observing data
        viewModel.carsLiveData.observe(viewLifecycleOwner) {
            var data: List<CarUiModel>? = null
            data = if (it == null) {
                Log.d("TAG", "observeData: local car list empty")
                listOf()
            } else {
                Log.d("TAG", "observeData: local cars available")
                it
            }
            setupRecyclerView(data)
            viewModel.setProgressVisibility(false)
        }

        viewModel.progressLiveData.observe(viewLifecycleOwner) {
            progressVisibility(it)
        }
    }

    private fun progressVisibility(isProgress: Boolean) {
        if (isProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.carRecyclerView.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.carRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(data: List<CarUiModel>) {
        binding.carRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CarAdapter(data)
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            setupWorkManagerForNetworkCall()
            viewModel.setProgressVisibility(true)
        } else {
            viewModel.getAllCarsLocally(requireContext())
            viewModel.setProgressVisibility(false)
            showNetworkMessage(isConnected)
        }
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        //here root is parent layout of this fragment
        if (!isConnected) {
            snackBar = Snackbar.make(binding.root, resources.getString(R.string.offline_txt_msg), Snackbar.LENGTH_LONG)
            snackBar.setBackgroundTint(resources.getColor(R.color.red_200))
            snackBar.show()
        }
    }
}