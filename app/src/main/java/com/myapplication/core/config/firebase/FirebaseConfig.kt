package com.myapplication.core.config.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val FAVORITE_FILMS_COLLECTION = "favorite films"

class FirebaseConfig {
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun getCollectionReference(collection: String = FAVORITE_FILMS_COLLECTION): CollectionReference =
        firestore.collection(collection)
}
