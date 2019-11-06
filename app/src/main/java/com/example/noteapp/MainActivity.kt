package com.example.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load from DB
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun LoadQuery (title:String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }

        //Adapter
        var myNotesAdapter = MyNotesAdapter (this, listNotes)
        //set adapter
        notelistview.adapter = myNotesAdapter

        //get total number of tasks from ListView
        val total = notelistview.count
        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null){
            //set to actionbar as subtitile of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
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
                LoadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQuery("%" + newText + "%")
                return false
            }
        });

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null){
            when(item.itemId){
                R.id.addNote ->{
                    startActivity(Intent(this, AddNoteActivity::class.java))
                }
                R.id.action_setting ->{
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter(context: Context, listNotesArray: ArrayList<Note>) : BaseAdapter() {

        var listNotesAdapter = ArrayList<Note>()
        var context:Context? = context

        init {
            this.listNotesAdapter = listNotesAdapter
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            var myView = layoutInflater.inflate(R.layout.row, null)
            var myNote = listNotesAdapter[position]
            myView.titleTextView.text = myNote.nodeName
            myView.descTextView.text = myNote.nodeDes

            //delete button click
            myView.deleteBtn.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeID.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }
            //Edit//Udate button click
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            //copy button click
            myView.copyBtn.setOnClickListener {
                //get title
                val title = myView.titleTextView.text.toString()
                //get description
                val desc = myView.descTextView.text.toString()
                //concatinate
                val s = title + "\n" + desc
                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s //add to clipboard
                Toast.makeText(this@MainActivity, "Copied...", Toast.LENGTH_SHORT).show()
            }

            sharedBtn.setOnClickListener {
                val title = myView.titleTextView.text.toString()
                val desc = myView.descTextView.text.toString()
                val s = title + "\n"+desc
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }
            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    private fun GoToUpdateFun(myNote: Note) {
        var intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("ID", myNote.nodeID) //put id
        intent.putExtra("name", myNote.nodeName) //put name
        intent.putExtra("des", myNote.nodeDes) //put description
        startActivity(intent) //start activity
    }
}