package com.example.meituanfood.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meituanfood.adapter.OrderAdapter
import com.example.meituanfood.databinding.FragmentOrdersBinding
import com.example.meituanfood.viewmodel.OrderViewModel
import com.google.android.material.tabs.TabLayout

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OrderViewModel by viewModels()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
        setupAdapters()
        setupObservers()
    }

    private fun setupTabs() {
        if (binding.tabLayout.tabCount == 0) {
            val tabs = listOf("All", "Pending", "Ongoing", "Completed", "Cancelled")
            tabs.forEach { tab ->
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tab))
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.selectTab(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupAdapters() {
        orderAdapter = OrderAdapter(
            getStatusText = { status -> viewModel.getStatusText(status) },
            getStatusColor = { status -> viewModel.getStatusColor(status) },
            onOrderClick = { order ->
                val intent = Intent(requireContext(), OrderTrackingActivity::class.java)
                intent.putExtra("order_id", order.id)
                startActivity(intent)
            }
        )
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun setupObservers() {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.submitList(orders)
            if (orders.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvOrders.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvOrders.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
