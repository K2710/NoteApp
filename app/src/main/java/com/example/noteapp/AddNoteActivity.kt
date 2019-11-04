package com.example.noteapp

import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlin.Exception

class AddNoteActivity : AppCompatActivity() {

    val dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        try {
            id = intent.getIntExtra("ID", 0)
            if(id!=0){
                //update note
                //change actionbar title
                supportActionBar!!.title = "Update Note"
                //change button text
                addBtn.text = "Update"
                titleEdit.setText(intent.getStringExtra("name"))
                descEdit.setText(intent.getStringExtra("des"))
            }

        }catch (ex:Exception){}
    }

    fun addFunc(view: View) {
        var dbManager = DbManager (this)

        var values = ContentValues()
        values.put("Title", titleEdit.text.toString())
        values.put("Description", descEdit.text.toString())

        if (id == 0){
            val ID = dbManager.insert(values)
            if (ID>0){
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Error adding note...", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID>0){
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Error adding note...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
