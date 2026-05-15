package com.nammayantra.utils

import com.google.firebase.database.FirebaseDatabase

object FirebaseOptimizer {
    fun enablePersistence() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    fun keepSynced(path: String) {
        FirebaseDatabase.getInstance().getReference(path).keepSynced(true)
    }
}