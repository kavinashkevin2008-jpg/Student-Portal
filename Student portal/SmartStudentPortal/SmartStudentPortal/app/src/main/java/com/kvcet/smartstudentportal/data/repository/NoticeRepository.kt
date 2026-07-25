package com.kvcet.smartstudentportal.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kvcet.smartstudentportal.data.model.NoticeItem
import kotlinx.coroutines.tasks.await

class NoticeRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val notices = db.collection("notices")

    suspend fun getAllNotices(): List<NoticeItem> {
        return notices.orderBy("postedOn", Query.Direction.DESCENDING).get().await()
            .documents.mapNotNull { it.toObject(NoticeItem::class.java)?.copy(id = it.id) }
    }

    suspend fun postNotice(notice: NoticeItem): Result<Unit> {
        return try {
            val ref = notices.document()
            ref.set(notice.copy(id = ref.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNotice(noticeId: String): Result<Unit> {
        return try {
            notices.document(noticeId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
