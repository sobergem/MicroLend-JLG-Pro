package com.neomfi.microlend.di

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // We tell SQLite to add the column and set the default text to match your Enum's starting state
        database.execSQL(
            "ALTER TABLE leads ADD COLUMN assignmentStatus TEXT NOT NULL DEFAULT 'UNASSIGNED'"
        )

        // Note: If you also added the CenterEntity in this PR, you must tell Room to create that table too!
        // database.execSQL("CREATE TABLE IF NOT EXISTS `centers` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))")
    }
}