//package com.mtechsoft.bookapp.data.local;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//
//import com.mtechsoft.bookapp.data.local.model.Chapter;
//
//
//@Database(entities = {Chapter.class}, version = 1, exportSchema = false)
//public abstract class AppRoomDatabase extends RoomDatabase {
//
////    public abstract DepartmentDao departmentDao();
//
//    private static volatile AppRoomDatabase INSTANCE;
//
//    public static AppRoomDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (AppRoomDatabase.class) {
//                if (INSTANCE == null) {
//                    // Create database here
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            AppRoomDatabase.class, "app_database")
//                            .addCallback(sRoomDatabaseCallback)
//                            .addMigrations()
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            // Add any data in database here
//        }
//    };
//
//}