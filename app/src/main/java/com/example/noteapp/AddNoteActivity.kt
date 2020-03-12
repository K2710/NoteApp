package com.example.noteapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_add_note.*
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.customView

class AddNoteActivity : AppCompatActivity() {

    private lateinit var noteRepository:NoteRepository
    private lateinit var categoryRepository: CategoryRepository
    private var id = 0
    private var mensage = "creada"
    private var categories = arrayListOf<Category>()
    private var idCategory = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        noteRepository = NoteRepository(this)
        categoryRepository = CategoryRepository(this)
        if(intent.hasExtra("id")){
            id = intent.getIntExtra("id", 0)
            retrieveNote(intent.getIntExtra("id",0))
            addBtn.text = "Update"
            mensage = "actualizada"
            title = "Editar Nota"
        }
        addBtn.setOnClickListener {
            saveNote()
        }
        spinner.setOnItemSelectedListener { _, position, _, _ ->
            idCategory = categories?.get(position)?.id
            Log.d("categoria", idCategory.toString())
        }
        retrieveCategories()
        btnAdd.setOnClickListener {
           modal()
        }
    }
    fun modal(){
        alert {
            title = "Crear categoria: "
            customView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    val edit = editText {

                    }.lparams(width = matchParent)
                    yesButton {
                        categoryRepository.saveCategory(edit.text.toString())
                        retrieveCategories()
                    }
                    noButton {  }
                }
            }
        }.show()
    }
    fun saveNote(){
        when{
            titleEdit.text.isEmpty() ->
                Toast.makeText(this,"Tienes que poner un titulo", Toast.LENGTH_SHORT).show()

            descEdit.text.isEmpty() ->
                Toast.makeText(this, "Tienes que poner una descripcion",Toast.LENGTH_SHORT).show()

            else -> {
                if(id == 0){
                    noteRepository.insertNote(titleEdit.text.toString(), descEdit.text.toString(),idCategory)
                }else{
                    noteRepository.updateNote(id,titleEdit.text.toString(), descEdit.text.toString(),0, idCategory)
                }
                Toast.makeText(this, "Nota $mensage satisfactoriamente.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retrieveNote(id:Int){
        noteRepository.getNote(id).observe(this, Observer {
            titleEdit.setText(it.nodeName)
            descEdit.setText(it.nodeDes)
            idCategory = it.idCategory
            spinner.selectedIndex = idCategory
        })
    }
    private fun retrieveCategories(){
        categoryRepository.getAll().observe(this, Observer {
            spinner.setItems(*it.map {category ->  category.nombre }.toTypedArray())
            Log.d("items", it.size.toString())
            categories.addAll(it)
        })
    }

}
