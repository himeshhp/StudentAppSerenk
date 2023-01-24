package com.studentapp.studentinfo;

import com.studentapp.constants.EndPoints;
import com.studentapp.model.StudentPojo;
import com.studentapp.testbase.TestBase;
import com.studentapp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

/**
 * Created by Jay
 */
//@RunWith(SerenityRunner.class)
public class StudentCURDTest extends TestBase {
    static String firstName = "PrimUser" + TestUtils.getRandomValue();
    static String lastName = "PrimeUser" + TestUtils.getRandomValue();
    static String programme = "Api Testing";
    static String email = TestUtils.getRandomValue() + "xyz@gmail.com";

    static int studentId;

    @Title("This will create new student")
    @Test
    public void test001() {
        List<String> courseList = new ArrayList<>();
        courseList.add("Scala");
        courseList.add("Java");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courseList);
        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(studentPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);
    }

    @Title("Verify if the student was added to the application")
    @Test
    public void test002() {
        String p1 = "findAll{it.firstName == '";
        String p2 = "'}.get(0)";

        HashMap<String, Object> studentMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENT)
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        Assert.assertThat(studentMap, hasValue(firstName));
        studentId = (int) studentMap.get("id");
    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test003() {
        firstName = firstName + "_updated";

        List<String> courseList = new ArrayList<>();
        courseList.add("Scala");
        courseList.add("Java");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courseList);
        SerenityRest.given().log().all()
                .header("Content-Type", "application/json")
                .pathParam("studentID", studentId)
                .body(studentPojo)
                .when()
                .put(EndPoints.UPDATE_STUDENT_BY_ID)
                .then().log().all().statusCode(200);

        String p1 = "findAll{it.firstName == '";
        String p2 = "'}.get(0)";
        HashMap<String, Object> studentMap = SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENT)
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        Assert.assertThat(studentMap, hasValue(firstName));
    }

    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test004() {
        SerenityRest.given().log().all()
                .pathParam("studentID", studentId)
                .when()
                .delete(EndPoints.DELETE_STUDENT_BY_ID)
                .then().statusCode(204);
        SerenityRest.given().log().all()
                .pathParam("studentID", studentId)
                .when()
                .get(EndPoints.GET_SINGLE_STUDENT_BY_ID)
                .then()
                .statusCode(404);
    }
}
