package com.quantummetric.qmtesttask

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.children

class TextExtractor {

    companion object {
        val tag: String = TextExtractor::class.java.simpleName
    }

    /*
    * My three strategies:
    * 1. Use 'decorView' after Composables are drawn out (using ViewTreeObserver) to access generated Views from ComposeView.
    * 2. Since Jetpack Compose is declarative and stateful, cache a list of Messages in a mutableState variable
    * 3. Regenerate the Composables somehow in our second class.
     */

    /*
    * I've decided to go primarily with strategy #2, as I was unable to get the Composables from the ComposeView.
    * I created a variable to cache the Messages that MainActivity displays on the screen.
     */
    var messages by mutableStateOf(mutableListOf<Message>())

    fun getDisplayedText(activity: MainActivity) {
        /*
        * We use composeView here to make sure the screen is finished rendering before looking at our `messages` variable.
        * If we call the `messages` without viewTreeObserver, messages will be empty since Messages have not been finished rendering.
         */
        val composeView = activity.window.decorView
            .findViewById<ViewGroup>(android.R.id.content)
            .getChildAt(0) as ComposeView

        val viewTreeObserver = composeView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to avoid multiple callbacks
                if (viewTreeObserver.isAlive) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

                /*
                * Here we call the text in each message that we cached.
                * To view the messages, search for the TextExtractor class in Logcat.
                 */
                messages.forEach {
                    Log.d(tag, "author: ${it.author}\nbody: ${it.body}")
                }

                /*
                * Try grabbing the Jetpack Compose nodes here by recursively traversing through the ComposeView.
                * This didn't work for me :(
                */
//                val nodes = composeView.children
//                getNodes(nodes)
            }
        })
    }

    /*
    * I really wanted to figure out a way to traverse the composeView using the getNodes() function.
    * But everytime I traverse the composeView, I get a RippleHostView or a ViewLayout, which both return null.
    * I believe this might have been the correct way to extract text from the Composables.
    * I just couldn't figure out how to get the Composables as a View, since Jetpack Compose does not
    * use a traditional View hierarchy.
     */
    fun getNodes(nodes: Sequence<View>) {
        for (child in nodes) {
            if (child is TextView) {
                Log.d(tag, "${child.text}")
            } else if (child is ViewGroup) {
                val children = child.children
                Log.d(tag, "${child.childCount}")
                getNodes(children)
            }
        }
    }
}