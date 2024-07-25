package com.example.ask_me.Screens.imageInput

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_me.Constants
import com.example.ask_me.model.messageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ImageInputViewModel : ViewModel() {

    val messageList by lazy { mutableStateListOf<messageModel>() }
    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = Constants.apikey
    )

    fun sendImageAndText(image: Bitmap, text: String) {
        viewModelScope.launch {
            try {
                val imageBase64 = encodedImageToBase64(image)
                val chat = generativeModel.startChat(
                    history = messageList.map { content(it.role) { text(it.message) } }.toList()
                )
                messageList.add(messageModel(text, "user"))
                messageList.add(messageModel("Typig...", "model"))

                val content = content("user") {
                    image(image)
                    text(text)
                }
                val response = chat.sendMessage(content)

                messageList.removeLast()
                messageList.add(messageModel(response.text.toString(), "model"))
            } catch (e: Exception) {
                messageList.removeLast()
                messageList.add(messageModel("Error" + e.message.toString(), "model"))
            }

        }
    }

    private fun encodedImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}


