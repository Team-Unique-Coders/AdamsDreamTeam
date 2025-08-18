package com.example.chat.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.chat.domain.model.Message
import com.example.chat.domain.repository.ChatRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ChatDetailUiState(
    val chatId: String,
    val messages: List<Message> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
    val isPeerTyping: Boolean = false,
    val title: String = "Conversation"
)

class ChatDetailViewModel(
    private val repo: ChatRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    private val chatId: String = checkNotNull(savedState["chatId"])

    private val _state = MutableStateFlow(ChatDetailUiState(chatId))
    val state: StateFlow<ChatDetailUiState> = _state.asStateFlow()

    // Debounced bot reply job; restarted on every user send
    private var peerReplyJob: Job? = null

    // Track burst + last message to craft a smarter reply
    private var pendingUserCount: Int = 0
    private var lastUserText: String = ""
    private var lastBotReply: String? = null

    init {
        viewModelScope.launch {
            repo.messages(chatId).collect { msgs ->
                _state.update { it.copy(messages = msgs) }
            }
        }
        viewModelScope.launch {
            repo.chats().collect { list ->
                val name = list.firstOrNull { it.id == chatId }?.title ?: "Conversation"
                _state.update { it.copy(title = name) }
            }
        }
        viewModelScope.launch { repo.markRead(chatId) }
    }

    fun onInputChange(value: String) = _state.update { it.copy(input = value) }

    fun send() {
        val text = state.value.input.trim()
        if (text.isEmpty()) return

        _state.update { it.copy(isSending = true) }

        viewModelScope.launch {
            repo.sendMessage(chatId, fromUserId = "me", text = text)
            _state.update { it.copy(input = "", isSending = false) }

            // Track for the debounced bot reply
            pendingUserCount += 1
            lastUserText = text

            schedulePeerReply()
        }
    }

    /**
     * Debounced "bot is typing..." + single reply for a burst.
     * If user sends multiple messages quickly, we cancel and restart the timer.
     */
    private fun schedulePeerReply() {
        peerReplyJob?.cancel()
        peerReplyJob = viewModelScope.launch {
            _state.update { it.copy(isPeerTyping = true) }

            // Wait for quiet time; new user messages will cancel & restart this
            val quietMillis = 3_000L
            delay(quietMillis)

            _state.update { it.copy(isPeerTyping = false) }

            // Build one varied reply based on the last text and burst size
            val burst = pendingUserCount.coerceAtLeast(1)
            val reply = buildBotReply(lastUserText, burst, lastBotReply)

            // Reset burst tracking AFTER we computed the reply
            pendingUserCount = 0
            lastBotReply = reply

            repo.sendMessage(chatId, fromUserId = "bot", text = reply)
        }
    }

    // ----------------- Reply brain (simple heuristics + variety) -----------------

    private fun buildBotReply(userText: String, burst: Int, prev: String?): String {
        val t = userText.trim().lowercase()

        // helpers
        fun containsAny(hay: String, vararg needles: String) =
            needles.any { hay.contains(it) }

        // --- reply pools ---
        val romanticReplies = listOf(
            "Aww, you’re the sweetest 🥰",
            "Love you more ❤️",
            "Thinking of you too 💫",
            "You make my day 💖",
            "Right back at you 😘",
            "Miss you already 💕"
        )
        val thanksReplies = listOf(
            "Any time! 🙌",
            "You’re welcome! 😊",
            "Glad to help!",
            "No problem!"
        )
        val questionReplies = listOf(
            "Good question! 👍",
            "Interesting — let me think on that 🤔",
            "Got it. I’ll look into it.",
            "Noted — thanks for asking!"
        )
        val greetReplies = listOf(
            "Hey there! 👋",
            "Hi! 🙂",
            "Hello! 👋",
            "Hey! What’s up?"
        )
        val longReplies = listOf(
            "Thanks for the details — noted! 📝",
            "Appreciate the thorough info!",
            "That helps a lot, thanks.",
            "Got it, I’ll keep that in mind."
        )
        val burstReplies = listOf(
            "Got your last $burst messages 👌",
            "Read all $burst — thanks!",
            "I saw all $burst, appreciated.",
            "All $burst received ✅"
        )
        val genericReplies = listOf(
            "Got it! 👍",
            "Okay, noted.",
            "Sounds good!",
            "Makes sense.",
            "Cool — thanks!"
        )

        // --- detectors ---
        val romantic = containsAny(
            t,
            "love you", "luv u", "miss you", "miss u", "my love",
            "sweetheart", "darling", "babe", "baby"
        ) || containsAny(
            userText, // keep emojis case-sensitive
            "❤️", "💖", "💕", "😍", "😘"
        )

        // --- choose bucket (order matters) ---
        val bucket = when {
            romantic -> romanticReplies
            containsAny(t, "thank", "thx", "thanks a lot") -> thanksReplies
            t.endsWith("?") -> questionReplies
            containsAny(t, "hello", "hi ", "hi!", "hey") -> greetReplies
            t.length >= 60 -> longReplies
            burst > 1 -> burstReplies
            else -> genericReplies
        }

        // pick a reply different from the previous when possible
        return bucket.randomDifferentFrom(prev)
    }


    private fun List<String>.randomDifferentFrom(prev: String?): String {
        if (isEmpty()) return "Got it!"
        if (size == 1) return first()
        // try up to a few times to avoid repeating prev
        repeat(3) {
            val choice = this[Random.nextInt(size)]
            if (choice != prev) return choice
        }
        return this.first { it != prev } // fallback
    }

    companion object {
        fun factory(repo: ChatRepository) = viewModelFactory {
            initializer { ChatDetailViewModel(repo, this.createSavedStateHandle()) }
        }
    }
}
