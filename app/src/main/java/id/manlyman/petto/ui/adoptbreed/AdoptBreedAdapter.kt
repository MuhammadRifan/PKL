package id.manlyman.petto.ui.adoptbreed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import id.manlyman.petto.ui.adoptbreed.adopt.AdoptFragment
import id.manlyman.petto.ui.adoptbreed.breed.BreedFragment

class AdoptBreedAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
            AdoptFragment(),
            BreedFragment()
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
            0 -> "Adopt"
            else -> "Breed"
        }
    }
}