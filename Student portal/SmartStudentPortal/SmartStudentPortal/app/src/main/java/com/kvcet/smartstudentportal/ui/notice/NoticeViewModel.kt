package com.kvcet.smartstudentportal.ui.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvcet.smartstudentportal.data.model.NoticeItem
import com.kvcet.smartstudentportal.data.repository.NoticeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoticeViewModel(
    private val repository: NoticeRepository = NoticeRepository()
) : ViewModel() {

    private val _notices = MutableStateFlow<List<NoticeItem>>(emptyList())
    val notices: StateFlow<List<NoticeItem>> = _notices

    fun load() {
        viewModelScope.launch {
            _notices.value = repository.getAllNotices()
        }
    }

    fun post(title: String, content: String, postedBy: String) {
        if (title.isBlank()) return
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            repository.postNotice(
                NoticeItem(title = title, content = content, postedOn = dateStr, postedBy = postedBy)
            )
            load()
        }
    }
}
