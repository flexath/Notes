package com.flexath.notes.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.flexath.notes.R
import com.flexath.notes.data.ColorNote
import com.flexath.notes.data.model.NoteDatabase
import com.flexath.notes.data.model.NoteEntity
import com.flexath.notes.data.repository.RoomNoteRepository
import com.flexath.notes.viewmodels.INoteViewModel
import com.flexath.notes.viewmodels.RoomNoteViewModel
import com.flexath.notes.viewmodels.RoomNoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.dialog_save_note.*

class EditorActivity : AppCompatActivity(), java.io.Serializable {

    private var isEyeVisible: Boolean = true
    private lateinit var mNoteViewModel: INoteViewModel
    private lateinit var mNoteEntity: NoteEntity
    private lateinit var note: NoteEntity
    private var noteType: String? = null

    companion object {
        private const val EXTRA_NOTE_TYPE = "EXTRA NOTE TYPE"
        private const val EXTRA_STRING_TYPE = "EXTRA STRING TYPE"
        fun newIntentEditor(context: Context, noteType: String?, note: NoteEntity?): Intent {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra(EXTRA_STRING_TYPE, noteType)
            intent.putExtra(EXTRA_NOTE_TYPE, note)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        setUpListeners()
        setUpViewModelAndEditTexts()

        note = intent?.getSerializableExtra(EXTRA_NOTE_TYPE) as NoteEntity
        noteType = intent?.getStringExtra(EXTRA_STRING_TYPE)

        setUpEditTextsOfNoteType()
    }

    private fun setUpEditTextsOfNoteType() {
        if (noteType == "insert") {
            etTitleHome.setText("")
            etDescriptionHome.setText("")
        } else {
            etTitleHome.setText(note.title)
            etDescriptionHome.setText(note.description)
            mNoteEntity.id = note.id
        }
    }

    private fun setUpListeners() {
        btnBackEditor.setOnClickListener {
            finish()
        }

        btnEyeEditor.setOnClickListener {
            if (isEyeVisible) {
                etTitleHome.visibility = View.INVISIBLE
                etDescriptionHome.visibility = View.INVISIBLE
                ivEyeEditor.setImageResource(R.drawable.ic_baseline_visibility_off_white_24dp)
                isEyeVisible = false
            } else {
                etTitleHome.visibility = View.VISIBLE
                etDescriptionHome.visibility = View.VISIBLE
                ivEyeEditor.setImageResource(R.drawable.ic_baseline_remove_red_eye_white_24dp)
                isEyeVisible = true
            }
        }

        btnSaveEditor.setOnClickListener {
            setUpSaveNoteAlertDialog()
        }
    }

    private fun setUpViewModelAndEditTexts() {
        setUpViewModel()
        setUpEditTexts()
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        if (noteType == "insert") {
            insertNote()
        } else {
            if(note.title != etTitleHome.text.toString() && note.description != etDescriptionHome.text.toString()){
                updateNote()
            }
        }
    }

    private fun insertNote() {
        setUpEditTexts()
        if (mNoteEntity.title!!.isNotEmpty() || mNoteEntity.description!!.isNotEmpty()) {
            mNoteViewModel.insertNote(note = mNoteEntity)
        }
    }

    private fun updateNote() {
        mNoteEntity.title = etTitleHome.text.toString()
        mNoteEntity.description = etDescriptionHome.text.toString()
        mNoteViewModel.updateNote(mNoteEntity)
    }

    private fun setUpEditTexts() {
        val title = etTitleHome?.text?.toString()
        val description = etDescriptionHome?.text?.toString()
        val color = ColorNote.colorList.random()
        mNoteEntity = NoteEntity(title, description, color)
    }

    private fun setUpViewModel() {
        val dao = NoteDatabase.getInstance(this).noteDao
        val repository = RoomNoteRepository(dao)
        val factory = RoomNoteViewModelFactory(repository)
        mNoteViewModel = ViewModelProvider(this, factory)[RoomNoteViewModel::class.java]
    }

    private fun setUpSaveNoteAlertDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.dialog_save_note)
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.btnDiscardDialogEditor.setOnClickListener {
            dialog.dismiss()
        }

        if (noteType == "insert") {
            val dialogText = "Save Changes ?"
            dialog.tvSaveChangesEditor.text = dialogText
            dialog.btnSaveDialogEditor.setOnClickListener {
                insertNote()
                finish()
            }
        } else {
            val dialogText = "Are you sure you want to discard your changes ?"
            dialog.tvSaveChangesEditor.text = dialogText
            dialog.btnSaveDialogEditor.setOnClickListener {
                updateNote()
                finish()
            }
        }


    }
}