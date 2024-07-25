package com.example.ask_me.Screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ask_me.model.messageModel
import com.example.ask_me.ui.theme.ModelMessage
import com.example.ask_me.ui.theme.UserMessage

@Composable
fun ChatScreen(navController: NavController,chatViewModel: ChatViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppBar()
            MessageList(messageList = chatViewModel.messageList, modifier = Modifier.weight(1f))
            Input(onMessageSent = {
                chatViewModel.sendMessage(it)
            })
        }
    }

}

@Composable
fun Input(onMessageSent: (String) -> Unit) {
    var message by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), contentAlignment = Alignment.BottomEnd
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = message,
                onValueChange = { message = it },
                placeholder = { Text(text = "Type Your Question.......") }
            )
            IconButton(onClick = {
                if (message.isNotEmpty()) onMessageSent(message)
                message = ""
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
            }

        }
    }

}

@Composable
fun MessageList(messageList: List<messageModel>, modifier: Modifier=Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth(), reverseLayout = true) {
        items(messageList.reversed()) {

            MessageItem(messageModel = it)
        }

    }

}

@Composable
fun MessageItem(messageModel: messageModel) {
    val isModel = messageModel.role == "model"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth())
        {
            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isModel) ModelMessage else UserMessage)
                    .padding(16.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }
            }

        }

    }
}

@Composable
fun AppBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    )
    {
        Text(text = "Ask_Me", color = Color.Black, fontSize = 30.sp)
    }

}
