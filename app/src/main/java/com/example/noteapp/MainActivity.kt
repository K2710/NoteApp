package com.example.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class MainActivity : AppCompatActivity() {


    private lateinit var noteRepository:NoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteRepository = NoteRepository(this)
        retrieveNotes()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addNote -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
                true
            }
            R.id.deleted -> {
                startActivity(Intent(this, DeletedNotesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //searchView
        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm = getSystemService (Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        });

        return super.onCreateOptionsMenu(menu)
    }
    private fun retrieveNotes(){
        val view = findViewById<View>(android.R.id.content) as ViewGroup
        noteRepository.getAll().observe(this, Observer {
            rvNote.layoutManager = LinearLayoutManager(this)
            rvNote.adapter = NoteAdapter(this,view, it)
        })
    }
    fun deleteNote(note:Note, estado:Int){
        alert("Esta nota sera enviada  al basurero!") {
            title="Estas seguro?"
            yesButton {
                noteRepository.updateNote(note.nodeID, note.nodeName, note.nodeDes, estado, note.idCategory)
            }
            noButton {  }
        }.show()
    }




}