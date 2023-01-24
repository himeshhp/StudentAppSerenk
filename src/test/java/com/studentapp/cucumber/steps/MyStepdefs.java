package com.studentapp.cucumber.steps;

import com.studentapp.studentinfo.StudentSteps;
import com.studentapp.utils.TestUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

public class MyStepdefs {
    static ValidatableResponse response;

    static String email = null;
    static String firstName = null;

    static int studentId;
    @Steps
    StudentSteps studentSteps;

    @When("^User sends a GET request to list endpoint$")
    public void userSendsAGETRequestToListEndpoint() {
        response = studentSteps.getAllStudentsInfo();
    }

    @Then("^User must get back a valid status code 200$")
    public void userMustGetBackAValidStatusCode() {
        response.statusCode(200);
    }

    @When("^I create a new student by providing the information firstName \"([^\"]*)\" lastName \"([^\"]*)\" email \"([^\"]*)\" programme \"([^\"]*)\" courses \"([^\"]*)\"$")
    public void iCreateANewStudentByProvidingTheInformationFirstNameLastNameEmailProgrammeCourses(String _firstName, String lastName, String _email, String programme, String course) {
        List<String> courseList = new ArrayList<>();
        courseList.add(course);
        email = TestUtils.getRandomValue() + _email;
        firstName = TestUtils.getRandomValue() + _firstName;
        response = studentSteps.createStudent(firstName, lastName, email, programme, courseList);
    }

    @Then("^I verify that the student with \"([^\"]*)\" is created$")
    public void iVerifyThatTheStudentWithIsCreated(String field) {
        response.statusCode(201);
        if (field.contains("@gmail.com")) {
            HashMap<String, Object> studentMap = studentSteps.getStudentInfoByEmail(email);
            studentId = (int) studentMap.get("id");
            Assert.assertThat(studentMap, hasValue(email));
        } else {
            HashMap<String, Object> studentMap = studentSteps.getStudentInfoByFirstName(firstName);
            studentId = (int) studentMap.get("id");
            Assert.assertThat(studentMap, hasValue(firstName));
        }
    }

    @Given("^Student application is running$")
    public void studentApplicationIsRunning() {
    }

    @And("^I update the student with information firstName \"([^\"]*)\" lastName \"([^\"]*)\" email \"([^\"]*)\" programme \"([^\"]*)\" courses \"([^\"]*)\"$")
    public void iUpdateTheStudentWithInformationFirstNameLastNameEmailProgrammeCourses(String _firstName, String lastName, String email, String programme, String courses) {
        List<String> courseList = new ArrayList<>();
        courseList.add(courses);
        firstName = firstName + "_updated";
        response = studentSteps.updateStudent(studentId, firstName, lastName, email, programme, courseList);
    }

    @And("^The student with firstName \"([^\"]*)\" is updated successfully$")
    public void theStudentWithFirstNameIsUpdatedSuccessfully(String _firstName) {
        HashMap<String, Object> studentMap = studentSteps.getStudentInfoByFirstName(firstName);
        Assert.assertThat(studentMap, hasValue(firstName));
    }

    @And("^I delete the student that created with firstName \"([^\"]*)\"$")
    public void iDeleteTheStudentThatCreatedWithFirstName(String firstName) {
        response = studentSteps.deleteStudent(studentId);
    }

    @Then("^The student deleted successfully from the application$")
    public void theStudentDeletedSuccessfullyFromTheApplication() {
        response.statusCode(204);
        studentSteps.getStudentById(studentId).statusCode(404);
    }
}
