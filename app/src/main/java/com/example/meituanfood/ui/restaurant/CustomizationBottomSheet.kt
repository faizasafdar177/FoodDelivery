package com.example.meituanfood.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meituanfood.data.model.MenuItem
import com.example.meituanfood.databinding.DialogCustomizationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomizationBottomSheet(
    private val menuItem: MenuItem,
    private val onConfirm: (MenuItem, String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: DialogCustomizationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogCustomizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.tvItemName.text = menuItem.name
        binding.tvDescription.text = menuItem.description
        binding.tvItemPrice.text = "¥${menuItem.price}"
        
        binding.btnAddToCart.setOnClickListener {
            val selectedOption = when (binding.rgSpiciness.checkedRadioButtonId) {
                com.example.meituanfood.R.id.rbMild -> "Mild"
                com.example.meituanfood.R.id.rbExtraSpicy -> "Extra Spicy"
                else -> "No Spicy"
            }
            onConfirm(menuItem, selectedOption)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CustomizationBottomSheet"
    }
}
