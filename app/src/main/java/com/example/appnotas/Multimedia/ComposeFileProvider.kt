package com.example.appnotas.Multimedia

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.appnotas.R
import java.io.File

class ComposeFileProvider: FileProvider(R.xml.filepaths) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()

            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )

            val authority = context.packageName + ".fileprovider"

            return FileProvider.getUriForFile(
                context,
                authority,
                file
            )
        }
    }
}