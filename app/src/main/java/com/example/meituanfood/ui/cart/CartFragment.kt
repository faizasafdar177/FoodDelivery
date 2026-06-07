package com.example.meituanfood.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meituanfood.adapter.CartAdapter
import com.example.meituanfood.databinding.FragmentCartBinding
import com.example.meituanfood.viewmodel.CartViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupObservers()
        setupClickListeners()
    }

    private fun setupAdapters() {
        cartAdapter = CartAdapter(
            onIncrease = { item -> viewModel.increase(item) },
            onDecrease = { item -> viewModel.decrease(item) },
            onRemove = { item -> viewModel.remove(item) }
        )
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun setupObservers() {
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
            if (items.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutCart.visibility = View.GONE
                binding.checkoutBar.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutCart.visibility = View.VISIBLE
                binding.checkoutBar.visibility = View.VISIBLE
                
                val subTotal = viewModel.getSubTotal(items)
                val deliveryFee = viewModel.getDeliveryFee(subTotal)
                val total = viewModel.getTotal(items)
                val count = viewModel.getTotalCount(items)
                
                binding.tvSubTotal.text = "Subtotal ¥${String.format("%.2f", subTotal)}"
                binding.tvDeliveryFee.text = "Delivery Fee ¥${String.format("%.2f", deliveryFee)}"
                binding.tvTotal.text = "¥${String.format("%.2f", total)}"
                binding.tvItemCount.text = "$count items"
                binding.btnPlaceOrder.text = "Checkout ¥${String.format("%.2f", total)}"
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.btnPlaceOrder.isEnabled = !loading
            if (loading) binding.btnPlaceOrder.text = "Processing..."
        }

        viewModel.orderPlaced.observe(viewLifecycleOwner) { placed ->
            if (placed) {
                showOrderSuccessDialog()
                viewModel.resetOrderPlaced()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnPlaceOrder.setOnClickListener {
            val paymentMethod = if (binding.rbWeChat.isChecked) "WeChat Pay" else "Alipay"
            showConfirmOrderDialog(paymentMethod)
        }

        binding.btnClearCart.setOnClickListener {
            showClearCartDialog()
        }

        binding.btnContinueShopping.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                com.example.meituanfood.R.id.bottomNavigation
            ).selectedItemId = com.example.meituanfood.R.id.homeFragment
        }
    }

    private fun showConfirmOrderDialog(paymentMethod: String) {
        val isSplit = binding.cbSplitBill.isChecked
        val message = "Payment: $paymentMethod" + if (isSplit) "\nOption: Split bill with friends" else ""
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Order")
            .setMessage(message)
            .setPositiveButton("Pay Now") { _, _ ->
                viewModel.placeOrder()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showClearCartDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear Cart")
            .setMessage("Are you sure you want to clear your cart?")
            .setPositiveButton("Clear") { _, _ ->
                viewModel.clearCart()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showOrderSuccessDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("🎉 Order Success!")
            .setMessage("Your order has been placed. You can track it in real-time now.")
            .setPositiveButton("Track Order") { _, _ ->
                requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                    com.example.meituanfood.R.id.bottomNavigation
                ).selectedItemId = com.example.meituanfood.R.id.ordersFragment
            }
            .setNegativeButton("Dismiss", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
