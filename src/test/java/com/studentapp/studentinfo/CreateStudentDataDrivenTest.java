package com.studentapp.studentinfo;

import com.studentapp.testbase.TestBase;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay
 */
/*@Concurrent(threads = "2x")
@UseTestDataFrom("src/test/java/resources/testdata/studentinfo.csv")
@RunWith(SerenityParameterizedRunner.class)*/
public class CreateStudentDataDrivenTest extends TestBase {
    private String  firstName, lastName, email, programme, course1, course2;
    @Steps
    StudentSteps studentSteps;

    @Title("Data Driven Test for adding multiple students to the application")
    @Test
    public void createMultipleStudents() {
        List<String> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        studentSteps.createStudent(firstName, lastName, email, programme, courses).statusCode(201);
    }
}


