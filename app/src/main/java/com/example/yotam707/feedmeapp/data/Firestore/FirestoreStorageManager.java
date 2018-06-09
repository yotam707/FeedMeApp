package com.example.yotam707.feedmeapp.data.Firestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirestoreStorageManager {
    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");
}
