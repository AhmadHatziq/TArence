package seedu.tarence.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.tarence.commons.exceptions.IllegalValueException;
import seedu.tarence.commons.util.JsonUtil;
import seedu.tarence.logic.parser.ParserUtil;
import seedu.tarence.logic.parser.exceptions.ParseException;
import seedu.tarence.model.module.ModCode;
import seedu.tarence.model.module.Module;
import seedu.tarence.model.student.Student;
import seedu.tarence.model.tutorial.Assignment;
import seedu.tarence.model.tutorial.Tutorial;

/**
 * Jackson friendly version of a Module.
 */
@JsonRootName(value = "modules")
public class JsonAdaptedModule {

    // Identifiers to store the fields
    public static final String TUTORIAL_NAME = "tutorialName";
    public static final String TUTORIAL_DAY = "tutorialDayOfWeek";
    public static final String TUTORIAL_START_TIME = "tutorialStartTime";
    public static final String TUTORIAL_WEEKS = "tutorialWeeks";
    public static final String TUTORIAL_DURATION = "tutorialDuration";
    public static final String TUTORIAL_MODULE_CODE = "tutorialModuleCode";
    public static final String TUTORIAL_STUDENT_LIST = "tutorialStudentList";
    public static final String TUTORIAL_ATTENDANCE_LIST = "tutorialAttendanceList";
    public static final String TUTORIAL_ASSIGNMENT_LIST = "tutorialAssignmentList";

    public static final String STUDENT_NAME = "studentName";
    public static final String STUDENT_EMAIL = "studentEmail";
    public static final String STUDENT_MATRIC_NUMBER = "studentMatricNumber";
    public static final String STUDENT_NUSNET_ID = "studentNusnetId";
    public static final String STUDENT_MODULE_CODE = "studentModuleCode";
    public static final String STUDENT_TUTORIAL_NAME = "studentTutorialName";
    public static final String STUDENT_ATTENDANCE_STATUS = "studentAttendance";
    public static final String STUDENT_ASSIGNMENT_SCORE = "studentAssignmentScore";

    public static final String ASSIGNMENT_NAME = "assignmentName";
    public static final String ASSIGNMENT_MAX_SCORE = "assignmentMaxScore";
    public static final String ASSIGNMENT_START_DATE = "assignmentStartDate";
    public static final String ASSIGNMENT_END_DATE = "assignmentEndDate";

    // Error message Strings
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Tutorial's %s field is missing!";
    public static final String INVALID_FIELD_MESSAGE_FORMAT = "Tutorial's %s field is invalid!";
    public static final String MISSING_GENERIC_FIELD = "Error in reading field! ";
    public static final String INVALID_FIELD = "Invalid field in %s";

    // Json fields
    private String moduleCode;
    private LinkedHashMap<String, String> mapOfDifferentTutorials; // Implemented LinkedHashMap to preserve ordering.
    private LinkedHashMap<String, LinkedHashMap<String, String>> testMap;

    /**
     * Invoked during reading of the Json file.
     *
     * @param moduleName Json string representing the Module Name/Code.
     * @param map Json string representing the Tutorial objects present in the Module.
     */
    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleCode") String moduleName,
                             @JsonProperty("tutorialMap") LinkedHashMap<String, String> map) {
        this.moduleCode = moduleName;
        this.mapOfDifferentTutorials = map;
    }

    /**
     * Invoked when saving the application.
     *
     * @param source Module object of the application.
     */
    public JsonAdaptedModule(Module source) {
        moduleCode = source.getModCode().toString();

        mapOfDifferentTutorials = new LinkedHashMap<String, String>();

        for (Tutorial t : source.getTutorials()) {
            LinkedHashMap<String, String> singleTutorialMap = new LinkedHashMap<String, String>();

            // Trying to get string representation of  Map<Assignment, Map<Student, Integer>>
            // Needed to create a Tutorial Object

            Map<Assignment, Map<Student, Integer>> assignmentsList = t.getAssignments();
            LinkedHashMap<String, String> tutorialAssignmentMap = new LinkedHashMap<String, String>();

            for (Assignment a : assignmentsList.keySet() ) {
                Map<Student, Integer> studentScoreList = assignmentsList.get(a);

                // Converts the students and their respective scores into String representation.
                // Each student is encompassed in "[]"
                String studentsWithAssignmentString = "[";
                for (Student s : studentScoreList.keySet()) {
                    LinkedHashMap<String, String> studentMap = new LinkedHashMap<String, String>();
                    int studentScore = studentScoreList.get(s);

                    studentMap.put(JsonAdaptedModule.STUDENT_NAME, s.getName().toString());
                    studentMap.put(JsonAdaptedModule.STUDENT_EMAIL, s.getEmail().toString());
                    studentMap.put(JsonAdaptedModule.STUDENT_MATRIC_NUMBER, s.getMatricNum().toString());
                    studentMap.put(JsonAdaptedModule.STUDENT_NUSNET_ID, s.getNusnetId().toString());
                    studentMap.put(JsonAdaptedModule.STUDENT_MODULE_CODE, s.getModCode().toString());
                    studentMap.put(JsonAdaptedModule.STUDENT_TUTORIAL_NAME, s.getTutName().toString());
                    studentMap.put(JsonAdaptedModule.STUDENT_ASSIGNMENT_SCORE, Integer.toString(studentScore));
                    studentsWithAssignmentString = studentsWithAssignmentString + studentMap.toString() + "],[";
                }

                // Case when there are no students.
                if (studentsWithAssignmentString.equals("[")) {
                    studentsWithAssignmentString += "]";
                } else {
                    // Remove the last square bracket
                    studentsWithAssignmentString = studentsWithAssignmentString.substring(0, (studentsWithAssignmentString.length() - 2));
                }

                // Convert the assignment object into a String representation
                LinkedHashMap<String, String> assignmentStringMap = new LinkedHashMap<String, String>();
                String assignmentName = a.getAssignName();
                String assignmentStartDate = a.getAssignmentStartDate().toString();
                String assignmentEndDate = a.getAssignmentEndDate().toString();
                String assignmentMaxScore = Integer.toString(a.getAssignmentMaxScore());

                assignmentStringMap.put(JsonAdaptedModule.ASSIGNMENT_NAME, assignmentName);
                assignmentStringMap.put(JsonAdaptedModule.ASSIGNMENT_MAX_SCORE, assignmentMaxScore);
                assignmentStringMap.put(JsonAdaptedModule.ASSIGNMENT_START_DATE, assignmentStartDate);
                assignmentStringMap.put(JsonAdaptedModule.ASSIGNMENT_END_DATE, assignmentEndDate);

                String singleAssignmentString = assignmentStringMap.toString();

                // Creates the final map of Assignment - StudentList(with scores)
                tutorialAssignmentMap.put(singleAssignmentString, studentsWithAssignmentString);
            }

            //System.out.println("Module " + moduleCode + "'s assignment string: ");
            //System.out.println(tutorialAssignmentMap.toString());
            // End of obtaining Tutorial's Assignment String

            LinkedHashMap<String, String> innerMapOne = new LinkedHashMap<>();
            innerMapOne.put("Element 1", "Value 1");
            innerMapOne.put("Element 2","Value 2");

            LinkedHashMap<String, String> innerMapTwo = new LinkedHashMap<>();
            innerMapTwo.put("Element 3", "Value 3");
            innerMapTwo.put("Element 4","Value 4");

            testMap = new LinkedHashMap<>();
            testMap.put("Inner Map One", innerMapOne);
            testMap.put("Inner Map Two", innerMapTwo);

            // Obtain all the fields that defines a single Tutorial object.
            String tutorialName = t.getTutName().toString();
            String tutorialDayOfWeek = t.getTimeTable().getDay().toString();
            String tutorialStartTime = t.getTimeTable().getStartTime().toString();
            String tutorialWeeks = t.getTimeTable().getWeeks().toString();
            String tutorialDuration = t.getTimeTable().getDuration().toString();
            String studentListString = JsonUtil.studentListToString(t.getStudents());
            String tutorialModuleCode = t.getModCode().toString();
            String tutorialAttendanceString = JsonUtil.attendanceListToString(t.getAttendance());

            // Add into LinkedHashMap<String,String>, singleTutorialMap. Reading is order dependant
            singleTutorialMap.put(TUTORIAL_NAME, tutorialName);
            singleTutorialMap.put(TUTORIAL_DAY, tutorialDayOfWeek);
            singleTutorialMap.put(TUTORIAL_START_TIME, tutorialStartTime);
            singleTutorialMap.put(TUTORIAL_WEEKS, tutorialWeeks);
            singleTutorialMap.put(TUTORIAL_DURATION, tutorialDuration);
            singleTutorialMap.put(TUTORIAL_STUDENT_LIST, studentListString);
            singleTutorialMap.put(TUTORIAL_ATTENDANCE_LIST, tutorialAttendanceString);
            singleTutorialMap.put(TUTORIAL_MODULE_CODE, tutorialModuleCode);

            mapOfDifferentTutorials.put(tutorialName, singleTutorialMap.toString());
        }
    }

    /**
     * Invoked during reading of Json file.
     * Converts JsonAdaptedModule into a Module object.
     *
     * @return Module object.
     * @throws IllegalValueException when there is an error in reading one of the fields.
     */
    public Module toModelType() throws IllegalValueException {
        List<Tutorial> tutorials = new ArrayList<Tutorial>();

        for (String tutorialName : mapOfDifferentTutorials.keySet()) {
            // Parses the tutorialString into a LinkedHashMap of the Tutorial's components.
            LinkedHashMap<String, String> mapOfSingleTutorial = JsonUtil
                    .tutorialStringToMap(mapOfDifferentTutorials.get(tutorialName));

            // Creates a Tutorial Object from the tutorialString
            Tutorial tutorialFromJson = JsonUtil.tutorialMapToTutorial(mapOfSingleTutorial);

            // Adds the Tutorial into the List.
            tutorials.add(tutorialFromJson);
        }

        try {
            ModCode modCodeFromJson = ParserUtil.parseModCode(moduleCode);
            return new Module(modCodeFromJson, tutorials);
        } catch (ParseException e) {
            throw new IllegalValueException(String.format(INVALID_FIELD, Module.class.getSimpleName()));
        }
    }

    /**
     * Getter function for String moduleCode.
     *
     * @return String module code.
     */
    public String getModuleCode() {
        return moduleCode;
    }

    /**
     * Getter function for Linked Hashmap tutorialMap.
     *
     * @return Tutorial Linked HashMap.
     */
    public LinkedHashMap<String, String> getMapOfDifferentTutorials() {
        return mapOfDifferentTutorials;
    }
}

