package com.flexath.notes.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import java.text.SimpleDateFormat
import java.util.*

class EditorActivity : AppCompatActivity(), java.io.Serializable {

    private var isEyeVisible: Boolean = true
    private lateinit var mNoteViewModel: INoteViewModel
    private lateinit var mNoteEntity: NoteEntity
    private var note: NoteEntity? = null
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

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        setUpListeners()
        setUpViewModelAndEditTexts()

        note = intent?.getSerializableExtra(EXTRA_NOTE_TYPE) as? NoteEntity
        noteType = intent?.getStringExtra(EXTRA_STRING_TYPE)

        setUpEditTextsOfNoteType()
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        super.onBackPressed()
        if (noteType == "insert") {
            insertNote()
        } else {
            Toast.makeText(this,"Note unsaved",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpEditTextsOfNoteType() {
        if (noteType == "insert") {
            etTitleHome.setText("")
            etDescriptionHome.setText("")
        } else {
            etTitleHome.setText(note?.title)
            etDescriptionHome.setText(note?.description)
            mNoteEntity.id = note?.id ?: 0
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

    private fun setUpEditTexts() {
        val title = etTitleHome?.text?.toString()
        val description = etDescriptionHome?.text?.toString()
        val color = ColorNote.colorList.random()
        val date = getNoteDate()
        mNoteEntity = NoteEntity(title, description, color, date)
    }

    private fun setUpViewModel() {
        val dao = NoteDatabase.getInstance(this).noteDao
        val repository = RoomNoteRepository(dao)
        val factory = RoomNoteViewModelFactory(repository)
        mNoteViewModel = ViewModelProvider(this, factory)[RoomNoteViewModel::class.java]
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDataTime(): Date {
        return Calendar.getInstance().time
    }

    private fun getNoteDate(): String {
        val date = getCurrentDataTime()
//        return SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault()).format(date)
        return date.toString("yyyy/MM/dd HH:mm:ss")
    }

    private fun insertNote() {
        setUpEditTexts()
        if (mNoteEntity.title!!.isNotEmpty() || mNoteEntity.description!!.isNotEmpty()) {
            mNoteViewModel.insertNote(note = mNoteEntity)
            Toast.makeText(this,"Note added",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNote() {
        mNoteEntity.title = etTitleHome.text.toString()
        mNoteEntity.description = etDescriptionHome.text.toString()
        mNoteEntity.date = getNoteDate()
        mNoteViewModel.updateNote(mNoteEntity)
    }

    private fun setUpSaveDialog(dialog: AlertDialog) {
        if (noteType == "insert") {
            val dialogText = "Save Note ?"
            dialog.tvSaveChangesEditor.text = dialogText
            dialog.btnSaveDialogEditor.setOnClickListener {
                insertNote()
                Toast.makeText(this,"Note saved",Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            val dialogText = "Are you sure you want to change ?"
            dialog.tvSaveChangesEditor.text = dialogText
            dialog.btnSaveDialogEditor.setOnClickListener {
                updateNote()
                Toast.makeText(this,"Note updated",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
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
        setUpSaveDialog(dialog)
    }
}