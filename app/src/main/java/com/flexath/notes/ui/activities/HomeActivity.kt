package com.flexath.notes.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.layout_bottom_sheet_dialog_info.*

class HomeActivity : AppCompatActivity(), INoteDelegate {

    private lateinit var mNoteAdapter: NoteAdapter
    private lateinit var mNoteViewModel: INoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpListeners()
        getAllNotesFromDatabase()
    }

    private fun setUpListeners() {
//        btnSearchHome.setOnClickListener {
//            startActivity(SearchActivity.newIntentSearch(this))
//        }

        fabAddNoteHome.setOnClickListener {
            startActivity(EditorActivity.newIntentEditor(this, "insert",mNoteAdapter.noteEntity))
        }

        btnInfoHome.setOnClickListener {
            setUpInfoBottomSheetDialog()
        }
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

    private fun getAllNotesFromDatabase() {
        setUpViewModel()
        mNoteViewModel.getAllNotes()?.observe(this) {
            setUpNoteRecyclerView(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpNoteRecyclerView(noteList: List<NoteEntity>) {
        mNoteAdapter = NoteAdapter(this, noteList, mNoteViewModel)
        rvHome.adapter = mNoteAdapter
        rvHome.layoutManager = LinearLayoutManager(this)
        if (mNoteAdapter.itemCount != 0) ivEmptyNotesHome.visibility = View.GONE
        if (mNoteAdapter.itemCount == 0) ivEmptyNotesHome.visibility = View.VISIBLE
        mNoteAdapter.notifyDataSetChanged()
    }

    private fun setUpViewModel() {
        val dao = NoteDatabase.getInstance(this).noteDao
        val repository = RoomNoteRepository(dao)
        val factory = RoomNoteViewModelFactory(repository)
        mNoteViewModel = ViewModelProvider(this, factory)[RoomNoteViewModel::class.java]
    }

    override fun onClickNote(note: NoteEntity) {
        startActivity(EditorActivity.newIntentEditor(this, "update",note))
    }

    override fun onClickDeleteButton(note: NoteEntity) {
        val dialog = MaterialAlertDialogBuilder(this, R.style.RoundedAlertDialog)
            .setCancelable(true)
            .setTitle("Delete Note !")
            .setIcon(R.drawable.ic_baseline_delete_forever_red_18dp)
            .setMessage("Are you sure ?")
            .setPositiveButton("Delete") { _, _ ->
                mNoteAdapter.deleteNoteDialog(note)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create()
        dialog.show()
    }

}