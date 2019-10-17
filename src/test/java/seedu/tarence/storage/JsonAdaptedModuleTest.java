package seedu.tarence.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.tarence.testutil.Assert.assertThrows;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import seedu.tarence.commons.exceptions.IllegalValueException;
import seedu.tarence.model.module.ModCode;
import seedu.tarence.model.util.SampleDataUtil;
import seedu.tarence.testutil.JsonUtil;


public class JsonAdaptedModuleTest {
    public static final String INVALID_STUDENT_STRING_MISSING_STUDENT_NAME = "[studentEmail=e0035152@u.nus.edu.sg, "
            + "studentMatricNumber=OptionalA0155413M, studentNusnetId=OptionalE0031550, "
            + "studentModuleCode=CS1010S, studentTutorialName=Lab Session}]";
    public static final String INVALID_STUDENT_STRING_WRONG_ORDER_OF_FIELDS = "[studentName=Alice, "
            + "studentEmail=e0035152@u.nus.edu.sg, "
            + "studentMatricNumber=OptionalA0155413M, studentNusnetId=OptionalE0031550, "
            + "studentTutorialName=Lab Session, studentModuleCode=CS1010S}]";
    public static final String INVALID_TUTORIAL_STRING_MISSING_TUTORIAL_NAME = "{tutorialDayOfWeek=MONDAY, "
            + "studentListString=[], tutorialModuleCode=CS1010S, tutorialStartTime=12:00, tutorialDuration=PT2H, "
            + "tutorialWeeks=[1, 4, 7]}";
    public static final String INVALID_TUTORIAL_STRING_WRONG_ORDER_OF_FIELDS = "{tutorialName=Sectional, "
            + "tutorialDayOfWeek=MONDAY, "
            + "studentListString=[], tutorialModuleCode=CS1010S, tutorialStartTime=12:00, tutorialDuration=PT2H, "
            + "tutorialWeeks=[1, 4, 7]}";
    public static final String INVALID_MODULE_CODE = "CS10101010AAA";
    public static final String VALID_MODULE_CODE = "CS1010E";
    public static final LinkedHashMap<String, String> VALID_TUTORIAL_MAP = JsonUtil.getValidMapOfDifferentTutorials();

    @Test
    public void isValidStudentString_studentStringWithoutNameField_returnsFalse (){
        JsonAdaptedModule module = new JsonAdaptedModule(SampleDataUtil.getSampleModule());
        assertEquals(false, module.isValidStudentString(INVALID_STUDENT_STRING_MISSING_STUDENT_NAME) );
    }

    @Test
    public void isValidStudentString_studentStringInWrongOrder_returnsFalse (){
        JsonAdaptedModule module = new JsonAdaptedModule(SampleDataUtil.getSampleModule());
        assertEquals(false, module.isValidStudentString(INVALID_STUDENT_STRING_WRONG_ORDER_OF_FIELDS) );
    }

    @Test
    public void isValidTutorialString_tutorialStringInWrongOrder_returnsFalse() {
        JsonAdaptedModule module = new JsonAdaptedModule(SampleDataUtil.getSampleModule());
        assertEquals(false, module.isValidTutorialString(INVALID_TUTORIAL_STRING_WRONG_ORDER_OF_FIELDS));
    }

    @Test
    public void isValidTutorialString_tutorialStringWithoutNameField_returnsFalse() {
        JsonAdaptedModule module = new JsonAdaptedModule(SampleDataUtil.getSampleModule());
        assertEquals(false, module.isValidTutorialString(INVALID_TUTORIAL_STRING_MISSING_TUTORIAL_NAME));
    }

    @Test
    public void toModelType_invalidModuleCode_throwsIllegalValueException() {
        JsonAdaptedModule module = new JsonAdaptedModule(INVALID_MODULE_CODE, VALID_TUTORIAL_MAP);
        String expectedMessage = "Invalid field in Module";
        assertThrows(IllegalValueException.class, expectedMessage, module::toModelType);
    }

    @Test
    public void toModelType_tutorialMapWithInvalidDay_throwsIllegalValueException() {
        JsonAdaptedModule module = new JsonAdaptedModule(VALID_MODULE_CODE,
                JsonUtil.getMapOfSingleTutorialWithInvalidTutorialDay());
        String expectedMessage = "Error in reading field! Invalid day entered";
        assertThrows(IllegalValueException.class, expectedMessage, module::toModelType);
    }

    @Test
    public void toModelType_TutorialMapWithInvalidDuration_throwsIllegalValueExcepion() {
        JsonAdaptedModule module = new JsonAdaptedModule(VALID_MODULE_CODE,
                JsonUtil.getMapOfSingleTutorialWithInvalidTutorialDuration());
        String expectedMessage = "Tutorial's Duration field is invalid! Or Tutorial's LocalTime field is invalid!";
        assertThrows(IllegalValueException.class, expectedMessage, module::toModelType);
    }

    @Test
    public void toModelType_TutorialMapWithInvalidStartTime_throwsIllegalValueExcepion() {
        JsonAdaptedModule module = new JsonAdaptedModule(VALID_MODULE_CODE,
                JsonUtil.getMapOfSingleTutorialWithInvalidTutorialStartTime());
        String expectedMessage = "Tutorial's Duration field is invalid! Or Tutorial's LocalTime field is invalid!";
        assertThrows(IllegalValueException.class, expectedMessage, module::toModelType);
    }

    @Test
    public void toModelType_TutorialMapWithInvalidWeeks_throwsIllegalValueExcepion() {
        JsonAdaptedModule module = new JsonAdaptedModule(VALID_MODULE_CODE,
                JsonUtil.getMapOfSingleTutorialWithInvalidTutorialWeeks());
        String expectedMessage = "Error in reading field! Invalid week number(s) entered. "
                + "Should contain only numbers from 1 to 13.";
        assertThrows(IllegalValueException.class, expectedMessage, module::toModelType);
    }

    @Test
    public void toModelType_TutorialMapWithInvalidModuleCode_throwsIllegalValueExcepion() {
        JsonAdaptedModule module = new JsonAdaptedModule(VALID_MODULE_CODE,
                JsonUtil.getMapOfSingleTutorialWithInvalidModuleCode());
        String expectedMessage = "Error in reading field! " + ModCode.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, module::toModelType);
    }

}
