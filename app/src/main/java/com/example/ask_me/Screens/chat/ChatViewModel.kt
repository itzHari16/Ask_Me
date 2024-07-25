package com.example.ask_me.Screens.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ask_me.Constants
import com.example.ask_me.model.messageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val apikey = "AIzaSyDCUCiiMj_sfwqv2gzdqWhSjJZO7wQR47g"

    val messageList by lazy { mutableStateListOf<messageModel>() }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apikey
    )

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map { content(it.role) { text(it.message) } }.toList()
                )
                messageList.add(messageModel(question, "user"))
                messageList.add(messageModel("Typig...", "model"))
                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(messageModel(response.text.toString(), "model"))
            } catch (e: Exception) {
                messageList.removeLast()
                messageList.add(messageModel("Error" + e.message.toString(), "model"))
            }
        }
    }
}
