package id.manlyman.petto.ui.facility

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import id.manlyman.petto.ui.facility.animalcare.AnimalCareFragment
import id.manlyman.petto.ui.facility.health.HealthFragment
import id.manlyman.petto.ui.facility.shop.ShopFragment

class FacilitiesAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        HealthFragment(),
        ShopFragment(),
        AnimalCareFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // nama untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Health"
            1 -> "Shop"
            else -> "Animal Care"
        }
    }
}