package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_deleted_notes.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class DeletedNotesActivity : AppCompatActivity() {

    private lateinit var noteRepository: NoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleted_notes)

        noteRepository = NoteRepository(this)
        retrieveNotes()
        title = "Basurero"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun retrieveNotes(){
        val view = findViewById<View>(android.R.id.content) as ViewGroup
        noteRepository.getAllDeleted().observe(this, Observer {
            rvNote.layoutManager = LinearLayoutManager(this)
            rvNote.adapter = NoteAdapter(this,view, it, true)
        })
    }
    fun delete(note:Note){
            alert("Esta nota sera eliminada definitivamente") {
                title = "Estas seguro?"
                yesButton {
                    noteRepository.delete(note)
                    Toast.makeText(this@DeletedNotesActivity, "Se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
                }
                noButton {  }
            }.show()
    }
    fun restoreNote(note:Note, estado:Int){
        alert("Esta nota sera restaurada") {
            title = "Estas seguro?"
            yesButton {
                noteRepository.updateNote(note.nodeID, note.nodeName, note.nodeDes, estado, note.idCategory)
                Toast.makeText(this@DeletedNotesActivity, "Se ha restaurado correctamente", Toast.LENGTH_SHORT).show()
            }
            noButton {  }
        }.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
