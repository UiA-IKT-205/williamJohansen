package com.example.piano


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.data.Note
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.File
import java.io.FileOutputStream

class PianoLayout : Fragment() {

    private var _binding: FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("B3","C4","D4","E4","F4","G4","A4","B4","C5")
    private val halfTones = listOf("C4#","D4#","F4#","G4#","A4#")

    private var score:MutableList<Note> = mutableListOf<Note>()

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

        var startPlay:Long = 0
        var endPlay:Long = 0

        fullTones.forEach {
            val fullToneKey = FullToneKeyFragment.newInstance(it)

            fullToneKey.onKeyDown = {
                startPlay = System.currentTimeMillis()


                if(!score.isEmpty()){
                    val pause = Note("p", endPlay, startPlay)
                    score.add(pause)
                }
                println("Piano key down $it")

            }

            fullToneKey.onKeyUp = {
                endPlay = System.currentTimeMillis()

                val note = Note(it, startPlay, endPlay)
                score.add(note)

                println("Piano key up $note")

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

        view.saveScoreBt.setOnClickListener{
            var fileName = view.fileNameTextEdit.text.toString()
            val path = this.activity?.getExternalFilesDir(null)
            if(fileName.isNotEmpty() && path != null){
                fileName = "$fileName.sheet"
                var file = File(path, fileName)
                if(!file.exists()){
                    FileOutputStream(file, true).bufferedWriter().use{ writer ->
                        score.forEach{
                            writer.write("${it.toString()}\n")
                        }
                    }
                    println("Saved file!")
                    score.clear()
                } else {
                    println("File: $fileName already exists!")
                }
            }else{
                if(fileName.isNotEmpty()){
                    println("Invalid filename")
                }
                if(path != null){
                    println("path does not exist")
                }
            }
        }

        return view
    }
}