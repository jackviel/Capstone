package com.example.captstone.cacheModels;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("SELECT * FROM ReviewCache ORDER BY createdAt LIMIT 25")
    List<ReviewCache> recentItems();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(ReviewCache review);
}
