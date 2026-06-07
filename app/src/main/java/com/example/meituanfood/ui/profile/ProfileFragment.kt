package com.example.meituanfood.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.meituanfood.R
import com.example.meituanfood.databinding.FragmentProfileBinding
import com.example.meituanfood.ui.profile.AboutUsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfile()
        setupClickListeners()
    }

    private fun setupProfile() {
        binding.tvUserName.text = "Danny"
        binding.tvUserPhone.text = "03260764834"
        
        // Setup titles for menu items
        binding.layoutOrders.tvMenuTitle.text = "My Orders"
        binding.layoutOrders.ivMenuIcon.setImageResource(android.R.drawable.ic_menu_agenda)
        
        binding.layoutAddress.tvMenuTitle.text = "My Addresses"
        binding.layoutAddress.ivMenuIcon.setImageResource(android.R.drawable.ic_menu_mylocation)
        
        binding.layoutBecomeRider.tvMenuTitle.text = "Become a Rider"
        binding.layoutBecomeRider.ivMenuIcon.setImageResource(android.R.drawable.ic_menu_directions)
        
        binding.layoutHelp.tvMenuTitle.text = "Help Center"
        binding.layoutHelp.ivMenuIcon.setImageResource(android.R.drawable.ic_menu_help)
        
        binding.layoutAbout.tvMenuTitle.text = "About Us"
        binding.layoutAbout.ivMenuIcon.setImageResource(android.R.drawable.ic_menu_info_details)
    }

    private fun setupClickListeners() {
        binding.layoutOrders.root.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                com.example.meituanfood.R.id.bottomNavigation
            ).selectedItemId = com.example.meituanfood.R.id.ordersFragment
        }

        binding.layoutAddress.root.setOnClickListener {
            // Implementation for address management
            showComingSoon("Addresses")
        }

        binding.layoutBecomeRider.root.setOnClickListener {
            // Navigate to Become Rider Page
            startActivity(Intent(requireContext(), BecomeRiderActivity::class.java))
        }

        binding.layoutHelp.root.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:03260764834")
            }
            startActivity(intent)
        }

        binding.layoutAbout.root.setOnClickListener {
            startActivity(Intent(requireContext(), AboutUsActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Confirm") { _, _ -> /* Auth logout logic */ }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showComingSoon(feature: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(feature)
            .setMessage("This feature is being updated. Stay tuned!")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
