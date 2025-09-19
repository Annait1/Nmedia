package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.AcEditPostBinding

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AcEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val oldContent = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (oldContent != null) {
            binding.edit.setText(oldContent)
            binding.edit.setSelection(oldContent.length)
        }

        binding.save.setOnClickListener {
            val content = binding.edit.text.toString()

            if (content.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, Intent())
            } else {
                val resultIntent = Intent()
                resultIntent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, resultIntent)
            }
            finish()
        }
    }
}