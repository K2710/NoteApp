package com.example.noteapp


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteAdapter(val context:Context,val viewGroup: ViewGroup, val list: List<NoteCategory>, val isDeleteMode:Boolean = false):RecyclerView.Adapter<NoteAdapter.ViewModel> (){
    class ViewModel(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)
        val lyOptions = itemView.findViewById<LinearLayout>(R.id.lyOptions)
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
        val editBtn = itemView.findViewById<ImageButton>(R.id.editBtn)
        val deleteBtn = itemView.findViewById<ImageButton>(R.id.deleteBtn)
        val lyOptions2 = itemView.findViewById<LinearLayout>(R.id.lyOptions2)
        val deleteBtn2 = itemView.findViewById<ImageButton>(R.id.deleteBtn2)
        val restoreBtn = itemView.findViewById<ImageButton>(R.id.restoreBtn)
        val tvCategory = itemView.findViewById<TextView>(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val v = LayoutInflater.from(parent.context!!).inflate(R.layout.note_layout_row, parent, false)
        return ViewModel(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val data = list[position]
        holder.tvTitle.text = data.note.nodeName
        holder.tvFecha.text = data.note.date
        holder.tvDesc.text = data.note.nodeDes
        holder.tvCategory.text = data.category.nombre

        var visible = false
        holder.editBtn.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            intent.putExtra("id", data.note.nodeID)
            context.startActivity(intent)
        }
        holder.deleteBtn.setOnClickListener {
            (context as MainActivity).deleteNote(data.note, 1)
        }
        holder.deleteBtn2.setOnClickListener {
            (context as DeletedNotesActivity).delete(data.note)
        }
        holder.restoreBtn.setOnClickListener {
            (context as DeletedNotesActivity).restoreNote(data.note, 0)
        }
        holder.itemView.setOnLongClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.image_click))
            visible = !visible

            val transition: Transition = Fade()
            transition.duration = 600

            TransitionManager.beginDelayedTransition(viewGroup, transition)
            if (!isDeleteMode){
                holder.lyOptions.visibility = if(visible) View.VISIBLE else View.GONE
            }else{
                holder.lyOptions2.visibility = if(visible) View.VISIBLE else View.GONE
            }
            true
        }
    }
}