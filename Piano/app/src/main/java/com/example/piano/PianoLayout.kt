package com.example.piano


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*

class PianoLayout : Fragment() {

    private var _binding: FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("B3","C4","D4","E4","F4","G4","A4","B4","C5")
    private val halfTones = listOf("C4#","D4#","F4#","G4#","A4#")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val childFragmentManager = childFragmentManager
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val fragmentManager = getChildFragmentManager()

        fullTones.forEach {
            val fullToneKey = FullToneKeyFragment.newInstance(it)

            fullToneKey.onKeyDown = {
                println("Piano key down $it")

            }

            fullToneKey.onKeyUp = {
                println("Piano key up $it")

            }
            fragmentTransaction.add(view.fullToneKeyLayout.id, fullToneKey, "note_$it")
        }

        // TODO: Fix halfTones position
        halfTones.forEach {
            val halfToneKey = HalfToneKeyFragment.newInstance(it)

            halfToneKey.onKeyDown = {
                println("Piano key down $it")

            }

            halfToneKey.onKeyUp = {
                println("Piano key up $it")

            }
            fragmentTransaction.add(view.halfToneKeyLayout.id, halfToneKey, "note_$it")

        }
        fragmentTransaction.commit()

        return view
    }
}