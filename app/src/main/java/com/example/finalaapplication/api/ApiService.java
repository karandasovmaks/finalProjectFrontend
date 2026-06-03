package com.example.finalaapplication.api;

import com.example.finalaapplication.api.model.AssignmentDto;
import com.example.finalaapplication.api.model.ClassDto;
import com.example.finalaapplication.api.model.ClassMemberDto;
import com.example.finalaapplication.api.model.SubmissionsDto;
import com.example.finalaapplication.api.model.TestDto;
import com.example.finalaapplication.api.model.TaskDto;
import com.example.finalaapplication.api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("user")
    Call<User> createUser(@Body User user);

    @GET("user/exists/{username}")
    Call<Boolean> checkUser(@Path("username") String username);

    @GET("user/byusername/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("tests")
    Call<List<TestDto>> getAllTests();

    @GET("tests/{id}")
    Call<TestDto> getTestById(@Path("id") long id);

    @POST("tests")
    Call<TestDto> createTest(@Body TestDto test);

    @GET("tasks")
    Call<List<TaskDto>> getTasksByTestId(@Query("test_id") long testId);

    @POST("tasks")
    Call<TaskDto> createTask(@Body TaskDto task);

    @POST("submissions")
    Call<SubmissionsDto> createSubmission(@Body SubmissionsDto submission);

    @POST("classes")
    Call<ClassDto> createClass(@Body Object body);

    @GET("classes/teacher/{teacherId}")
    Call<List<ClassDto>> getTeacherClasses(@Path("teacherId") long teacherId);

    @GET("classes/student/{studentId}")
    Call<List<ClassDto>> getStudentClasses(@Path("studentId") long studentId);

    @DELETE("classes/{id}")
    Call<Void> deleteClass(@Path("id") long id);

    @POST("class-members/join")
    Call<ClassMemberDto> joinClass(@Body Object body);

    @DELETE("class-members")
    Call<Void> leaveClass(@Query("class_id") long classId, @Query("student_id") long studentId);

    @POST("assignments")
    Call<AssignmentDto> createAssignment(@Body Object body);

    @GET("assignments/teacher/{teacherId}")
    Call<List<AssignmentDto>> getAssignmentsForTeacher(@Path("teacherId") long teacherId);

    @GET("assignments/student/{studentId}")
    Call<List<AssignmentDto>> getAssignmentsForStudent(@Path("studentId") long studentId);
}
