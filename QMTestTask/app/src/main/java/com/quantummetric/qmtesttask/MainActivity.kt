package com.quantummetric.qmtesttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.quantummetric.qmtesttask.ui.theme.QMTestTaskTheme

class MainActivity : ComponentActivity() {

    lateinit var textExtractor: TextExtractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * We instantiate our second class in the onCreate() function in order to cache messages in the setContent block.
        * We pass the `messages` variable to cache the Messages.
         */
        textExtractor = TextExtractor()

        setContent {
            QMTestTaskTheme {
                Conversation(messages = SampleData.conversationSample, cachedMessages = textExtractor.messages)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        /*
         * TODO:
         *  1. Create a separate class with a method that takes an Activity as a parameter.
         *  2. Find a way to access the displayed text using the Activity instance.
         *
         * Hints:
         * Use Jetpack Compose source code and debugger.
         * Try to explore what may take a View role in Jetpack Compose and find a way to access this
         * information.
         */

        textExtractor.getDisplayedText(this)

    }
}

@Composable
fun Conversation(messages: List<Message>, cachedMessages: MutableList<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message = message, cachedMessages = cachedMessages)
        }
    }
}

@Composable
fun MessageCard(message: Message, cachedMessages: MutableList<Message>) {

    // Here we cache the messages so that we can access them in our second class
    cachedMessages.add(message)

    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = message.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = message.body,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

data class Message(val author: String, val body: String)