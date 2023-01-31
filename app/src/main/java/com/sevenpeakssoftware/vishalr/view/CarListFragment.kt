package com.sevenpeakssoftware.vishalr.view

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sevenpeakssoftware.vishalr.CarApplication
import com.sevenpeakssoftware.vishalr.R
import com.sevenpeakssoftware.vishalr.databinding.FragmentCarListBinding
import com.sevenpeakssoftware.vishalr.model.CarUiModel
import com.sevenpeakssoftware.vishalr.utils.NetworkChangeReceiver
import com.sevenpeakssoftware.vishalr.viewmodel.CarListViewModel
import javax.inject.Inject

class CarListFragment : Fragment(), NetworkChangeReceiver.ConnectivityReceiverListener {
    private lateinit var binding: FragmentCarListBinding
    private lateinit var snackBar: Snackbar
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        viewModelFactory.create(
            CarListViewModel::class.java
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as CarApplication).appComponent.inject(this)
    }

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
        observeData()
    }

    private fun registerNetworkingBroadcast() {
        requireContext().registerReceiver(
            NetworkChangeReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onDetach() {
        super.onDetach()
        requireContext().unregisterReceiver(NetworkChangeReceiver())
    }

    private fun observeData() {
        // View model observing data
        viewModel.carsLiveData.observe(viewLifecycleOwner) {
            var data: List<CarUiModel>? = null
            data = if (it == null) {
                Log.d("TAG", "observeData: local car list empty")
                listOf()
            } else {
                Log.d("TAG", "observeData: local cars available ${it.size}")
                it
            }
            setupRecyclerView(data)
            viewModel.setProgressVisibility(false)
        }

        viewModel.progressLiveData.observe(viewLifecycleOwner) {
            progressVisibility(it)
        }

        viewModel.isCarsUpdatedLiveData.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.car_updated_txt_msg),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.car_updated_fail_txt_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.getAllCarsLocally()
            viewModel.setProgressVisibility(false)
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
            viewModel.fetchCarsRemotely()
            viewModel.setProgressVisibility(true)
        } else {
            viewModel.getAllCarsLocally()
            viewModel.setProgressVisibility(false)
            showNetworkMessage(isConnected)
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

    private fun showNetworkMessage(isConnected: Boolean) {
        //here root is parent layout of this fragment
        if (!isConnected) {
            snackBar = Snackbar.make(
                binding.root,
                resources.getString(R.string.offline_txt_msg),
                Snackbar.LENGTH_LONG
            )
            snackBar.setBackgroundTint(resources.getColor(R.color.red_200))
            snackBar.show()
        }
    }
}