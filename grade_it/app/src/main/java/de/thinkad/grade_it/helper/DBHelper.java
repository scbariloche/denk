package de.thinkad.grade_it.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.thinkad.grade_it.pojos.Category;
import de.thinkad.grade_it.pojos.Exam;
import de.thinkad.grade_it.pojos.Student;
import de.thinkad.grade_it.pojos.StudentExam;
import de.thinkad.grade_it.pojos.StudentSubject;
import de.thinkad.grade_it.pojos.Subject;
import de.thinkad.grade_it.pojos.Weight;
import de.thinkad.grade_it.pojos.WeightCategory;

/**
 * Created by andreas on 03.10.2017.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static final String INIT = "DB_VESRION";
    private static final String FAV_WEIGHT = "FAV_WEIGHT";
    public static String DATABASE_NAME = "grade_it";
    public static final String STUDENT_TABLE = "student";
    public static final String CATEGORY_TABLE = "category";
    public static final String EXAM_TABLE = "exam";
    public static final String STUDENTEXAM_TABLE = "studentexam";
    public static final String STUDENTSUBJECT_TABLE = "studentsubject";
    public static final String SUBJECT_TABLE = "subject";
    public static final String WEIGHT_CATEGORY_TABLE = "weightcategory";
    public static final String WEIGHT_TABLE = "weight";
    public static final String CURRENT_STUDENT_TABLE = "currentStudent";
    public static final String CURRENT_SUBJECT_TABLE = "currentSubject";
    public static final String STUD_ID = "stud_id";
    public static final String CATE_ID = "cate_id";
    public static final String EXAM_ID = "exam_id";
    public static final String SUBJ_ID = "subj_id";
    public static final String WEIG_ID = "weig_id";
    public static final String CONFIG_TABLE = "CONFIG_TABLE";
    public static String[] allTables = new String[11];

    public static String[] getAllTables() {
        allTables[0] = STUDENT_TABLE;
        allTables[1] = CATEGORY_TABLE;
        allTables[2] = EXAM_TABLE;
        allTables[3] = STUDENTEXAM_TABLE;
        allTables[4] = STUDENTSUBJECT_TABLE;
        allTables[5] = SUBJECT_TABLE;
        allTables[6] = WEIGHT_CATEGORY_TABLE;
        allTables[7] = WEIGHT_TABLE;
        allTables[8] = CURRENT_STUDENT_TABLE;
        allTables[9] = CURRENT_SUBJECT_TABLE;
        allTables[10] = CONFIG_TABLE;


        return allTables;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "create table  if not exists " + STUDENT_TABLE + " " +
                        "(" + STUD_ID +
                        " integer primary key, name text, gradesys integer)"
        );
        db.execSQL(
                "create table if not exists " + CATEGORY_TABLE + " " +
                        "(" +
                        CATE_ID + " integer primary key, name text)"
        );
        db.execSQL(
                "create table  if not exists " + EXAM_TABLE + " " +
                        "(" +
                        EXAM_ID + " integer primary key, date long, " +
                        CATE_ID + " integer, " +
                        SUBJ_ID + " integer)"
        );
        db.execSQL(
                "create table  if not exists " + STUDENTEXAM_TABLE + " " +
                        "( grade text, " +
                        STUD_ID + " integer NOT NULL ," +
                        EXAM_ID + " integer NOT NULL, primary key( " +
                        STUD_ID + ", " +
                        EXAM_ID + "))"
        );
        db.execSQL(
                "create table  if not exists " + STUDENTSUBJECT_TABLE + " " +
                        "(" +
                        STUD_ID + " integer NOT NULL ," +
                        SUBJ_ID + " integer NOT NULL, " +
                        "color integer, " + WEIG_ID + " integer, " +
                        " primary key( " + STUD_ID + ", " + SUBJ_ID + "))"
        );
        db.execSQL(
                "create table  if not exists " + SUBJECT_TABLE + " " +
                        "(" +
                        SUBJ_ID + " integer primary key, name text, admin integer)"
        );
        db.execSQL(
                "create table  if not exists " + WEIGHT_CATEGORY_TABLE + " " +
                        "(" +
                        WEIG_ID + " integer NOT NULL ," +
                        CATE_ID + " integer NOT NULL, " +
                        "value integer," +
                        " primary key( " + WEIG_ID + ", " + CATE_ID + "))"
        );
        db.execSQL(
                "create table  if not exists " + WEIGHT_TABLE + " " +
                        "(" +
                        WEIG_ID + " integer primary key, "
                        + WEIG_ID + WEIG_ID + " integer, value integer, name text)"
        );
        db.execSQL(
                "create table if not exists  " + CURRENT_STUDENT_TABLE + " " +
                        "(" +
                        STUD_ID + " integer primary key )"
        );
        db.execSQL(
                "create table if not exists  " + CURRENT_SUBJECT_TABLE + " " +
                        "(" +
                        SUBJ_ID + " integer primary key )"
        );
        db.execSQL(
                "create table if not exists  " + CONFIG_TABLE +
                        " (ID text primary key, VALUE integer )"
        );

        ContentValues cv = new ContentValues();
        cv.put("ID", INIT);
        cv.put("VALUE", 1);
        db.replace(CONFIG_TABLE, null, cv);

        insertStudent(db, new Student(1, "andenk", 0));

        insertCurrentSubjectId(db, 0);
        insertCurrentStudentId(db, 1);

        insertCategory(db, new Category(1, "Schulaufgabe"));
        insertCategory(db, new Category(2, "Extemporale"));
        insertCategory(db, new Category(3, "Referat"));
        insertCategory(db, new Category(4, "Ausfrage"));
        insertCategory(db, new Category(5, "Mitarbeit"));

        insertWeight(db, new Weight(2, 1, 2, "gr. LN"));
        insertWeight(db, new Weight(3, 1, 1, "kl. LN"));
        insertWeight(db, new Weight(1, -1, 1, "bay. Gymnasium"));


        insertWeigCategory(db, 2, 1, 1);
        insertWeigCategory(db, 3, 2, 1);
        insertWeigCategory(db, 3, 3, 1);
        insertWeigCategory(db, 3, 4, 1);
        insertWeigCategory(db, 3, 5, 1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


        onCreate(db);
    }

    public void upgradeDB(SQLiteDatabase db, int version) {
        switch (version) {
            case 0:
                for (String table : getAllTables()
                        ) {
                    db.execSQL("DROP TABLE IF EXISTS " + table);
                }
                onCreate(db);
            case 1:
                insertWeight(getWritableDatabase(), new Weight(4, -1, 1, "Berufschule"));
                insertWeigCategory(getWritableDatabase(), 4, 1, 2);
                insertWeigCategory(getWritableDatabase(), 4, 2, 1);
                insertWeigCategory(getWritableDatabase(), 4, 3, 1);
                insertWeigCategory(getWritableDatabase(), 4, 4, 1);
                insertWeigCategory(getWritableDatabase(), 4, 5, 1);

                setdbVersion(2);

            case 2:
                setFavouriteWeightId(1);
                setdbVersion(3);
            case 3:
                insertWeight(getWritableDatabase(), new Weight(5, -1, 1, "bay. Gymnasium Qualifikationsphase"));

                insertWeight(db, new Weight(6, 5, 1, "gr. LN"));
                insertWeight(db, new Weight(7, 5, 1, "kl. LN"));

                insertWeigCategory(db, 6, 1, 1);
                insertWeigCategory(db, 7, 2, 1);
                insertWeigCategory(db, 7, 3, 1);
                insertWeigCategory(db, 7, 4, 1);
                insertWeigCategory(db, 7, 5, 1);




                setdbVersion(4);
            case 4:
                insertCategory(new Category("Klausur"));
                insertCategory(new Category("Jahrgangsstufentest"));
                setdbVersion(5);


        }


    }



    public Integer delete(String tableName, String[] columns, String[] ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder columnString = new StringBuilder();
        for (String column : columns
                ) {
            columnString.append(column + " = ? and ");
        }
        return db.delete(tableName,
                columnString.toString().substring(0, columnString.length() - 4),
                ids);
    }

    public long insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        return insertStudent(db, student);
    }

    public long insertStudent(SQLiteDatabase db, Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", student.getName());
        contentValues.put("gradesys", student.getGradesys());
        if (student.getId() > 0) {
            contentValues.put(STUD_ID, student.getId());
        }
        return db.replace(STUDENT_TABLE, null, contentValues);
    }

    public long insertCurrentStudentId(int id) {
        return insertCurrentStudentId(this.getWritableDatabase(), id);
    }

    public long insertCurrentStudentId(SQLiteDatabase db, int id) {
        db.execSQL("delete from " + CURRENT_STUDENT_TABLE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUD_ID, id);
        return db.replace(CURRENT_STUDENT_TABLE, null, contentValues);
    }

    public long insertCurrentSubjectId(int id) {
        return insertCurrentSubjectId(this.getWritableDatabase(), id);
    }


    public long insertCurrentSubjectId(SQLiteDatabase db, int id) {
        db.execSQL("delete from " + CURRENT_SUBJECT_TABLE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJ_ID, id);
        long ret = db.replace(CURRENT_SUBJECT_TABLE, null, contentValues);
        return ret;

    }

    public long insertExam(Exam exam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", exam.getDate());
        contentValues.put(CATE_ID, exam.getCategory().getId());
        contentValues.put(SUBJ_ID, exam.getSubject().getId());
        if (exam.getId() > 0) {
            contentValues.put(EXAM_ID, exam.getId());
        }
        return db.replace(EXAM_TABLE, null, contentValues);
    }

    public long insertCategory(Category category) {

        return insertCategory(this.getWritableDatabase(), category);


    }

    public long insertCategory(SQLiteDatabase db, Category category) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", category.getName());
        if (category.getId() > 0) {
            contentValues.put(CATE_ID, category.getId());
        }
        return db.replace(CATEGORY_TABLE, null, contentValues);
    }

    public boolean insertStudentExam(StudentExam studentExam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUD_ID, studentExam.getStudent().getId());
        contentValues.put(EXAM_ID, studentExam.getExam().getId());
        contentValues.put("grade", studentExam.getGrade());
        db.replace(STUDENTEXAM_TABLE, null, contentValues);
        return true;
    }

    public boolean insertStudentExam(int stud_id, int exam_id, int grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUD_ID, stud_id);
        contentValues.put(EXAM_ID, exam_id);
        contentValues.put("grade", grade);
        db.replace(STUDENTEXAM_TABLE, null, contentValues);
        return true;
    }

    public boolean insertStudentSubject(StudentSubject studentSubject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUD_ID, studentSubject.getStudent().getId());
        contentValues.put(SUBJ_ID, studentSubject.getSubject().getId());
        contentValues.put("color", studentSubject.getColor());
        db.replace(STUDENTSUBJECT_TABLE, null, contentValues);
        return true;
    }

    public boolean insertStudentSubject(int stud_id, int subj_id, int color, int weig_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUD_ID, stud_id);
        contentValues.put(SUBJ_ID, subj_id);
        contentValues.put("color", color);
        contentValues.put(WEIG_ID, weig_id);
        db.replace(STUDENTSUBJECT_TABLE, null, contentValues);
        return true;
    }

    public long insertSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", subject.getName());
        contentValues.put("admin", subject.getAdmin());
        if (subject.getId() > 0) {
            contentValues.put(SUBJ_ID, subject.getId());
        }
        return db.replace(SUBJECT_TABLE, null, contentValues);
    }


    public long insertWeigCategory(SQLiteDatabase db, int weigId, int cateID, int value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIG_ID, weigId);
        contentValues.put(CATE_ID, cateID);
        contentValues.put("value", value);

        return db.replace(WEIGHT_CATEGORY_TABLE, null, contentValues);
    }

    public long insertWeigCategory(SQLiteDatabase db, WeightCategory weightCategory) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEIG_ID, weightCategory.getWeight().getId());
        contentValues.put(CATE_ID, weightCategory.getCategory().getId());
        contentValues.put("value", weightCategory.getValue());

        return db.replace(WEIGHT_CATEGORY_TABLE, null, contentValues);
    }

    public long insertWeigCategory(WeightCategory weightCategory) {
        return insertWeigCategory(this.getWritableDatabase(), weightCategory);
    }


    public long insertWeight(SQLiteDatabase db, Weight weight) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", weight.getName());
        contentValues.put(WEIG_ID + WEIG_ID, weight.getWeightId());
        contentValues.put("value", weight.getValue());
        if (weight.getId() > 0) {
            contentValues.put(WEIG_ID, weight.getId());
        }
        return db.replace(WEIGHT_TABLE, null, contentValues);
    }

    public long insertWeight(Weight weight) {
        return insertWeight(this.getWritableDatabase(), weight);
    }

    public Student getStudent(int id) {
        Student ret = new Student();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + STUDENT_TABLE + " where " +
                    STUD_ID + "=" + id + "", null);
            if (res.moveToFirst()) {
                ret.setId(res.getInt(res.getColumnIndex(STUD_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setGradesys(res.getInt(res.getColumnIndex("gradesys")));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public Student getCurrentStudent() {
        int stud_id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + CURRENT_STUDENT_TABLE, null);
            if (res.moveToFirst()) {
                stud_id = res.getInt(res.getColumnIndex(STUD_ID));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return getStudent(stud_id);
    }

    public Subject getCurrentSubject() {
        int subj_id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + CURRENT_SUBJECT_TABLE, null);
            if (res.moveToFirst()) {
                subj_id = res.getInt(res.getColumnIndex(SUBJ_ID));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return (subj_id == 0) ? null : getSubject(subj_id);
    }

    public Category getCategory(int id) {
        Category ret = new Category();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + CATEGORY_TABLE + " where " +
                    CATE_ID + "=" + id + "", null);
            if (res.moveToFirst()) {
                ret.setId(res.getInt(res.getColumnIndex(CATE_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public Exam getExam(int id) {
        Exam ret = new Exam();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + EXAM_TABLE + " where " +
                    EXAM_ID + "=" + id + "", null);
            if (res.moveToFirst()) {
                ret.setId(id);
                ret.setDate(res.getLong(res.getColumnIndex("date")));
                ret.setCategory(getCategory(res.getInt(res.getColumnIndex(CATE_ID))));
                ret.setSubject(getSubject(res.getInt(res.getColumnIndex(SUBJ_ID))));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public StudentExam getStudExam(int stud_id, int exam_id) {
        StudentExam ret = new StudentExam();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + STUDENTEXAM_TABLE + " where " +
                    STUD_ID + "=" + stud_id + " and " + EXAM_ID + " = " + exam_id, null);
            if (res.moveToFirst()) {
                ret.setStudent(getStudent(res.getInt(res.getColumnIndex(STUD_ID))));
                ret.setExam(getExam(res.getInt(res.getColumnIndex(EXAM_ID))));

                ret.setGrade(res.getString(res.getColumnIndex("grade")));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public StudentSubject getStudSubj(int stud_id, int subj_id) {
        StudentSubject ret = new StudentSubject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + STUDENTSUBJECT_TABLE + " where " +
                    STUD_ID + "=" + stud_id + " and " + SUBJ_ID + " = " + subj_id, null);
            if (res.moveToFirst()) {
                ret.setStudent(getStudent(res.getInt(res.getColumnIndex(STUD_ID))));
                ret.setSubject(getSubject(res.getInt(res.getColumnIndex(SUBJ_ID))));
                int color = res.getInt(res.getColumnIndex("color"));

                ret.setColor(color);
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public Subject getSubject(int id) {
        Subject ret = new Subject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + SUBJECT_TABLE + " where " +
                    SUBJ_ID + "=" + id + " order by 2", null);
            if (res.moveToFirst()) {
                ret.setId(res.getInt(res.getColumnIndex(SUBJ_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setAdmin(res.getInt(res.getColumnIndex("admin")));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public Weight getWeight(int id) {
        Weight ret = new Weight();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + WEIGHT_TABLE + " where " +
                    WEIG_ID + "=" + id + "", null);
            if (res.moveToFirst()) {
                ret.setId(res.getInt(res.getColumnIndex(WEIG_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setWeightId(res.getInt(res.getColumnIndex((WEIG_ID + WEIG_ID))));
                ret.setValue(res.getInt(res.getColumnIndex("value")));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }


    public int numberOfRows(String tablename) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, tablename);
        return numRows;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> retArray = new ArrayList<Student>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + STUDENT_TABLE, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Student ret = new Student();
                ret.setId((res.getInt(res.getColumnIndex(STUD_ID))));
                ret.setName(res.getString(res.getColumnIndex("name")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> retArray = new ArrayList<Category>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + CATEGORY_TABLE, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Category ret = new Category();
                ret.setId(res.getInt(res.getColumnIndex(CATE_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public List<Category> getAllCategoriesBySubjectID(int subjId) {

        Weight w = Helper.buildWeight(this, getStudSubj
                (getCurrentStudent().getId(), subjId).getId());

        return w.getCategories();
    }

    public ArrayList<Exam> getAllExams() {
        ArrayList<Exam> retArray = new ArrayList<Exam>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + EXAM_TABLE, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Exam ret = new Exam();
                ret.setId(res.getInt(res.getColumnIndex(EXAM_ID)));
                ret.setDate(res.getLong(res.getColumnIndex("date")));
                ret.setCategory(getCategory(res.getInt(res.getColumnIndex(CATE_ID))));
                ret.setSubject(getSubject(res.getInt(res.getColumnIndex(SUBJ_ID))));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }


    public ArrayList<StudentExam> getAllStudentExamsBySubjId(int subj_id) {
        ArrayList<StudentExam> retArray = new ArrayList<StudentExam>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select se." +
                    STUD_ID + ", se." + EXAM_ID + ", se.grade, e.date, e." +
                    SUBJ_ID + " from " + STUDENTEXAM_TABLE +
                    " se left join " + EXAM_TABLE + " e on se." + EXAM_ID + " = e." + EXAM_ID +
                    " where e." + SUBJ_ID + "=" + subj_id + " order by 4", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                StudentExam ret = new StudentExam();
                ret.setStudent(getStudent(res.getInt(res.getColumnIndex(STUD_ID))));
                ret.setExam(getExam(res.getInt(res.getColumnIndex(EXAM_ID))));
                ret.setGrade(res.getString(res.getColumnIndex("grade")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public ArrayList<Exam> getAllExamsBySubjIdAndStudentId(int subj_id, int stud_id) {
        ArrayList<Exam> retArray = new ArrayList<Exam>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select ss." +
                    STUD_ID + ", ss." + SUBJ_ID + ", e.date, from " + STUDENTSUBJECT_TABLE +
                    " ss left join " + EXAM_TABLE + " e on ss." + SUBJ_ID + " = e." + SUBJ_ID +
                    " where e." + SUBJ_ID + "=" + subj_id + " and ss." + STUD_ID + " = " + stud_id + " order by 3", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Exam ret = new Exam();
                ret = (getExam(res.getInt(res.getColumnIndex(EXAM_ID))));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }


    public ArrayList<StudentExam> getAllStudentExams() {
        ArrayList<StudentExam> retArray = new ArrayList<StudentExam>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select se." +
                    STUD_ID + ", se." + EXAM_ID + ", se.grade, e.date from " + STUDENTEXAM_TABLE + " se left join " + EXAM_TABLE + " e on se." + EXAM_ID + " = e." + EXAM_ID + " order by 4", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                StudentExam ret = new StudentExam();
                ret.setStudent(getStudent(res.getInt(res.getColumnIndex(STUD_ID))));
                ret.setExam(getExam(res.getInt(res.getColumnIndex(EXAM_ID))));
                ret.setGrade(res.getString(res.getColumnIndex("grade")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public ArrayList<StudentSubject> getStudentSubjectsByStudentId(int id) {
        ArrayList<StudentSubject> retArray = new ArrayList<StudentSubject>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select stsu.color, stsu." + WEIG_ID +
                    ", stsu." + SUBJ_ID + ", su.name from "
                    + STUDENTSUBJECT_TABLE + " stsu left join "
                    + SUBJECT_TABLE + " su" +
                    " on su." + SUBJ_ID + " = stsu."
                    + SUBJ_ID + " where stsu." + STUD_ID
                    + " = " + id + " order by name", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Subject ret = new Subject();
                ret.setId(res.getInt(res.getColumnIndex(SUBJ_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                Weight weight = getWeight(res.getInt(res.getColumnIndex(WEIG_ID)));
                retArray.add(new StudentSubject(getStudent(id), ret, res.getInt(res.getColumnIndex("color")), weight));
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public StudentSubject getStudentSubjectBySubjectId(int id) {
        StudentSubject ret = new StudentSubject();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + STUDENTSUBJECT_TABLE + " where " + SUBJ_ID + " = " + id, null);


//            res = db.rawQuery("select stsu.color, stsu." + WEIG_ID +
//                    ", stsu." + SUBJ_ID + ", su.name from "
//                    + STUDENTSUBJECT_TABLE + " stsu left join "
//                    + SUBJECT_TABLE + " su" +
//                    " on su." + SUBJ_ID + " = stsu."
//                    + SUBJ_ID + " where stsu." + STUD_ID
//                    + " = " + id + " order by name", null);
            if (res.moveToFirst()) {

                Weight weight = getWeight(res.getInt(res.getColumnIndex(WEIG_ID)));
                ret = (new StudentSubject(getStudent(id), getSubject(res.getInt(res.getColumnIndex(SUBJ_ID)))
                        , res.getInt(res.getColumnIndex("color")), weight));
            }
        } finally {
            if (res != null)
                res.close();
        }
        return ret;
    }

    public ArrayList<Subject> getAllSubjects() {
        ArrayList<Subject> retArray = new ArrayList<Subject>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + SUBJECT_TABLE, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Subject ret = new Subject();
                ret.setId(res.getInt(res.getColumnIndex(STUD_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setAdmin(res.getInt(res.getColumnIndex("admin")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public ArrayList<Weight> getAllWeights() {
        ArrayList<Weight> retArray = new ArrayList<Weight>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + WEIGHT_TABLE, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Weight ret = new Weight();
                ret.setId(res.getInt(res.getColumnIndex(WEIG_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setWeightId(res.getInt(res.getColumnIndex((WEIG_ID + WEIG_ID))));
                ret.setValue(res.getInt(res.getColumnIndex("value")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public ArrayList<Weight> getAllTopWeights() {
        ArrayList<Weight> retArray = new ArrayList<Weight>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + WEIGHT_TABLE + " where " + WEIG_ID + WEIG_ID + " = -1", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Weight ret = new Weight();
                ret.setId(res.getInt(res.getColumnIndex(WEIG_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setWeightId(res.getInt(res.getColumnIndex((WEIG_ID + WEIG_ID))));
                ret.setValue(res.getInt(res.getColumnIndex("value")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public ArrayList<Weight> getAllWeightsByWeigWeigId(int weigWeigId) {
        ArrayList<Weight> retArray = new ArrayList<Weight>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + WEIGHT_TABLE + " where " + WEIG_ID + WEIG_ID + " = " + weigWeigId, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                Weight ret = new Weight();
                ret.setId(res.getInt(res.getColumnIndex(WEIG_ID)));
                ret.setName(res.getString(res.getColumnIndex("name")));
                ret.setWeightId(res.getInt(res.getColumnIndex((WEIG_ID + WEIG_ID))));
                ret.setValue(res.getInt(res.getColumnIndex("value")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }


    public ArrayList<WeightCategory> getAllWeigCateByWeigId(int weigId) {
        ArrayList<WeightCategory> retArray = new ArrayList<WeightCategory>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + WEIGHT_CATEGORY_TABLE + " where "
                    + WEIG_ID + " = " + weigId, null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                WeightCategory ret = new WeightCategory();
                ret.setCategory(getCategory(res.getInt(res.getColumnIndex(CATE_ID))));
                ret.setWeight(getWeight(res.getInt(res.getColumnIndex(WEIG_ID))));
                ret.setValue(res.getInt(res.getColumnIndex("value")));
                retArray.add(ret);
                res.moveToNext();
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retArray;
    }

    public void displayTables() {
        StringBuilder ret = new StringBuilder();
        for (String s : getAllTables()) {
            String tableString = String.format("Table %s:\n", s);


            Cursor allRows = this.getWritableDatabase().rawQuery("SELECT * FROM " + s, null);
            if (allRows.moveToFirst()) {
                String[] columnNames = allRows.getColumnNames();
                do {
                    for (String name : columnNames) {
                        StringBuilder sb = new StringBuilder();
                        for (int c = 0; c < 14 - name.length(); c++) {
                            sb.append(" ");
                        }
                        tableString += String.format("%s:" + sb.toString() + " %s\n", name,
                                allRows.getString(allRows.getColumnIndex(name)));
                    }
                    tableString += "\n";

                } while (allRows.moveToNext());
            }

            ret.append(tableString);
            ret.append("\n");
        }
        Log.i("displayDB", ret.toString());
    }


    public ArrayList<Exam> getAllExamsBySubjID(int id) {
        ArrayList<Exam> retlist = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;

        try {
            res = db.rawQuery("select * from " + EXAM_TABLE + " where " +
                    SUBJ_ID + " = " + id, null);
            if (res.moveToFirst()) {
                while (res.isAfterLast() == false) {
                    Exam ret = new Exam();
                    ret.setId(res.getInt(res.getColumnIndex(CATE_ID)));
                    ret.setDate(res.getLong(res.getColumnIndex("date")));
                    ret.setCategory(getCategory(res.getInt(res.getColumnIndex(CATE_ID))));
                    ret.setSubject(getSubject(res.getInt(res.getColumnIndex(SUBJ_ID))));
                    retlist.add(ret);
                    res.moveToNext();
                }
            }
        } finally {
            if (res != null)
                res.close();
        }
        return retlist;
    }


    public int getdbVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONFIG_TABLE
                        + " where ID = '" + INIT + "'"
                , null);
        res.moveToFirst();
        int ret = res.getCount();
        if (ret > 0) {
            return res.getInt(res.getColumnIndex("VALUE"));
        } else {
            Cursor resOld = db.rawQuery("select * from " + CONFIG_TABLE
                            + " where ID = 1"
                    , null);
            resOld.moveToFirst();
            if (resOld.getCount() > 0) {
                setdbVersion(1);
                String[] where = new String[1];
                where[0] = "ID";
                String[] ids = new String[1];
                ids[0] = "1";
                delete(CONFIG_TABLE, where, ids);
                return getdbVersion();
            } else {
                return 0;
            }
        }
    }


    public void setdbVersion(int version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", INIT);
        cv.put("VALUE", version);
        db.replace(CONFIG_TABLE, null, cv);

    }

    public void setFavouriteWeightId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", FAV_WEIGHT);
        cv.put("VALUE", id);
        db.replace(CONFIG_TABLE, null, cv);

    }

    public int getFavouriteWeightId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONFIG_TABLE
                        + " where ID = '" + FAV_WEIGHT + "'"
                , null);
        res.moveToFirst();
        return res.getInt(res.getColumnIndex("VALUE"));
    }

    public long testDB(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        contentValues.put("VALUE", 1);
//        String[] where= new String[1];
//        where[0]="ID";
//        String[] ids= new String[1];
//        ids[0]=INIT;
//        delete(CONFIG_TABLE,where,ids);

        return db.replace(CONFIG_TABLE, null, contentValues);
    }

}
