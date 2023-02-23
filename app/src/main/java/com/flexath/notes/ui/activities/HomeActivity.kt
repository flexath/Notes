package com.flexath.notes.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flexath.notes.R
import com.flexath.notes.data.model.NoteDatabase
import com.flexath.notes.data.model.NoteEntity
import com.flexath.notes.data.repository.RoomNoteRepository
import com.flexath.notes.ui.adapters.NoteAdapter
import com.flexath.notes.ui.delegates.INoteDelegate
import com.flexath.notes.viewmodels.INoteViewModel
import com.flexath.notes.viewmodels.RoomNoteViewModel
import com.flexath.notes.viewmodels.RoomNoteViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_dialog_info.*

class HomeActivity : AppCompatActivity(), INoteDelegate {

    private lateinit var mNoteAdapter: NoteAdapter
    private lateinit var mNoteViewModel: INoteViewModel
    private var isVisibleSearchView = false
    private lateinit var noteListBeforeSearching:List<NoteEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpListeners()
        getAllNotesFromDatabase()
    }

    private fun setUpSearchView() {
        if(!isVisibleSearchView) {
            searchViewHome.visibility = View.VISIBLE
            ivSearchIconHome.setImageResource(R.drawable.ic_baseline_search_off_white_24dp)
            isVisibleSearchView = true
        } else {
            searchViewHome.visibility = View.GONE
            ivSearchIconHome.setImageResource(R.drawable.icon_search)
            isVisibleSearchView = false
        }
    }

    private fun setUpListeners() {
        btnSearchHome.setOnClickListener {
            setUpSearchView()
        }

        searchViewHome.setOnQueryTextListener(object: OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                searchNote(newText)
                return true
            }
        })

        fabAddNoteHome.setOnClickListener {
            startActivity(EditorActivity.newIntentEditor(this, "insert",mNoteAdapter.noteEntity))
        }

        btnInfoHome.setOnClickListener {
            setUpInfoBottomSheetDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun setUpInfoBottomSheetDialog() {
        val bottomDialog = BottomSheetDialog(this,R.style.ModalBottomDialog)
        bottomDialog.setContentView(R.layout.layout_bottom_sheet_dialog_info)
        bottomDialog.setCancelable(true)

        bottomDialog.btnCloseInfoHome.setOnClickListener {
            bottomDialog.dismiss()
        }
        isVisibleContactInfo(bottomDialog)
        bottomDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpNoteRecyclerView(noteList: List<NoteEntity>) {
        mNoteAdapter = NoteAdapter(this, noteList)
        rvHome.adapter = mNoteAdapter
        rvHome.layoutManager = LinearLayoutManager(this)
        if (mNoteAdapter.itemCount != 0) ivEmptyNotesHome.visibility = View.GONE
        if (mNoteAdapter.itemCount == 0) ivEmptyNotesHome.visibility = View.VISIBLE
        setUpSwipeLeftToDeleteNote(rvHome)
        mNoteAdapter.notifyDataSetChanged()
    }

    private fun setUpSwipeLeftToDeleteNote(recyclerView: RecyclerView) {
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(this@HomeActivity,"Don't worry! You can cancel",Toast.LENGTH_SHORT).show()
                setUpDeleteConfirmationDialog(mNoteAdapter.getNoteAt(viewHolder.adapterPosition))
            }

        }).attachToRecyclerView(recyclerView)
    }

    private fun isVisibleContactInfo(dialog: BottomSheetDialog) {
        var isVisibleContact = false
        dialog.rlContactBoxInfoHome.setOnClickListener {
            if(!isVisibleContact) {
                dialog.ivArrowContactInfoHome.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_white_24dp)
                dialog.llContactInfoHome.visibility = View.VISIBLE
                isVisibleContact = true
            } else {
                dialog.ivArrowContactInfoHome.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_white_24dp)
                dialog.llContactInfoHome.visibility = View.GONE
                isVisibleContact = false
            }
        }
    }

    private fun setUpViewModel() {
        val dao = NoteDatabase.getInstance(this).noteDao
        val repository = RoomNoteRepository(dao)
        val factory = RoomNoteViewModelFactory(repository)
        mNoteViewModel = ViewModelProvider(this, factory)[RoomNoteViewModel::class.java]
    }

    private fun getAllNotesFromDatabase() {
        setUpViewModel()
        mNoteViewModel.getAllNotes()?.observe(this) {
            noteListBeforeSearching = it
            setUpNoteRecyclerView(it)
        }
    }

    private fun searchNote(newText:String?) {
        val noteListAfterSearching = ArrayList<NoteEntity>()
        for(note in noteListBeforeSearching){
            if(note.title?.contains(newText!!) == true || note.description?.contains(newText!!) == true){
                noteListAfterSearching.add(note)
            }
        }
        mNoteAdapter.setSearchNoteList(noteListAfterSearching)
    }

    fun setUpDeleteConfirmationDialog(note: NoteEntity) {
        val dialog = MaterialAlertDialogBuilder(this, R.style.RoundedAlertDialog)
            .setCancelable(true)
            .setTitle("Delete Note !")
            .setIcon(R.drawable.ic_baseline_delete_forever_red_18dp)
            .setMessage("Are you sure ?")
            .setPositiveButton("Delete") { _, _ ->
                mNoteViewModel.deleteNote(note)
                Toast.makeText(this,"Note deleted",Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                mNoteViewModel.getAllNotes()?.observe(this){
                    setUpNoteRecyclerView(it)
                }
            }.create()
        dialog.show()
    }

    override fun onClickNote(note: NoteEntity) {
        startActivity(EditorActivity.newIntentEditor(this, "update",note))
    }

    override fun onClickDeleteButton(note: NoteEntity) {
        setUpDeleteConfirmationDialog(note)
    }
}