package seedu.tarence.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.tarence.model.module.ModCode;
import seedu.tarence.model.module.Module;
import seedu.tarence.model.person.Email;
import seedu.tarence.model.person.Name;
import seedu.tarence.model.student.MatricNum;
import seedu.tarence.model.student.NusnetId;
import seedu.tarence.model.student.Student;
import seedu.tarence.model.tutorial.TutName;
import seedu.tarence.model.tutorial.Tutorial;
import seedu.tarence.model.util.SampleDataUtil;

/**
 * Jackson friendly version of a Module.
 */
@JsonRootName(value = "modules")
public class JsonAdaptedModule {

    // Json fields
    private String moduleCode;
    private LinkedHashMap<String, String> tutorialMap; //Implemented LinkedHashMap to preserve ordering.

    // Identifiers to store the fields
    private static final String TUTORIAL_NAME = "tutorialName";
    private static final String TUTORIAL_DAY = "tutorialDayOfWeek";
    private static final String TUTORIAL_START_TIME = "tutorialStartTime";
    private static final String TUTORIAL_WEEKS = "tutorialWeeks";
    private static final String TUTORIAL_DURATION = "tutorialDuration";
    private static final String TUTORIAL_MODULE_CODE = "tutorialModuleCode";
    private static final String TUTORIAL_STUDENT_LIST = "tutorialStudentList";
    private static final String STUDENT_NAME = "studentName";
    private static final String STUDENT_EMAIL = "studentEmail";
    private static final String STUDENT_MATRIC_NUMBER = "studentMatricNumber";
    private static final String STUDENT_NUSNET_ID = "studentNusnetId";
    private static final String STUDENT_MODULE_CODE = "studentModuleCode";
    private static final String STUDENT_TUTORIAL_NAME = "studentTutorialName";

    // Constructor from Json file.
    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleCode") String moduleName,
                             @JsonProperty("tutorialMap") LinkedHashMap<String, String> map)  {
        this.moduleCode = moduleName;
        this.tutorialMap = map;
    }

    // Constructor from Module object.
    public JsonAdaptedModule(Module source) {

        moduleCode = source.getModCode().toString();

        tutorialMap = new LinkedHashMap<String, String>();

        for (Tutorial t : source.getTutorials()) {
            LinkedHashMap<String, String> singleTutorialMap = new LinkedHashMap<String, String>();

            // Obtain all the fields that define a Tutorial object.
            String tutorialName = t.getTutName().toString();
            String tutorialDayOfWeek = t.getTimeTable().getDay().toString();
            String tutorialStartTime = t.getTimeTable().getStartTime().toString();
            String tutorialWeeks = t.getTimeTable().getWeeks().toString();
            String tutorialDuration = t.getTimeTable().getDuration().toString();
            String studentListString = studentListToString(t.getStudents());
            String tutorialModuleCode = t.getModCode().toString();

            // Add into LinkedHashMap<String,String>, singleTutorialMap
            singleTutorialMap.put(TUTORIAL_NAME, tutorialName);
            singleTutorialMap.put(TUTORIAL_DAY, tutorialDayOfWeek);
            singleTutorialMap.put(TUTORIAL_START_TIME, tutorialStartTime);
            singleTutorialMap.put(TUTORIAL_WEEKS, tutorialWeeks);
            singleTutorialMap.put(TUTORIAL_DURATION, tutorialDuration);
            singleTutorialMap.put(TUTORIAL_STUDENT_LIST, studentListString);
            singleTutorialMap.put(TUTORIAL_MODULE_CODE, tutorialModuleCode);

            tutorialMap.put(tutorialName, singleTutorialMap.toString());
        }
    }



    public Module toModelType() throws IllegalArgumentException {

        for (String tutorialName : tutorialMap.keySet()) {

            String tutorialString = tutorialMap.get(tutorialName);
            // tutorialString contains info about the whole tutorial ie name, day, list of students etc

            // Converts tutorialString to a LinkedhashMap<String,String> for easy parsing
            LinkedHashMap<String, String> tutorialMapFromJson = tutorialStringToMap(tutorialString);

        }

        return SampleDataUtil.getSampleModule();
    }

    /*
    Converts JsonAdoptedModule to a Module.
    Reads in the Json String, parses it and recreates Module
     */
    // Leftover code from previous session
    /*
    public Module toModelType() throws IllegalArgumentException {
        ModCode modcode = new ModCode(moduleName);

        ArrayList<Tutorial> tutorials = new ArrayList<Tutorial>();
        for (String tutorialString : tutorialListForModule) {
            String[] words = tutorialString.split("\\|");

            // Get String arguments to create the list of Tutorials needed for the Module object.
            String tutorialName = words[0].trim();
            String day = words[1].trim();
            String startTime = words[2].trim();
            String weekString = words[3].trim();
            String duration = words[4].trim();
            String students = words[5].trim();
            String moduleNameFromString = words[6].trim();

            // Create list of students. In a list of students, students are split based on '$'
            String[] studentStrings = students.split("\\$");
            List<Student> studentListForTutorial = new ArrayList<Student>();

            for (String studentString : studentStrings) {
                // Creates a Student object if the string is valid.
                if (isValidStudentString(studentString)) {
                    Student studentFromJson = jsonStringToStudent(studentString, modcode, tutorialName);

                    System.out.println("Student created from Json");
                    System.out.println("Student details: " + studentFromJson.toString());
                    studentListForTutorial.add(studentFromJson);

                }

            }

        }
        return SampleDataUtil.getSampleModule();
    }

     */

    public LinkedHashMap<String, String> tutorialStringToMap (String tutorialString) {
        LinkedHashMap<String, String> tutorialStringToMap = new LinkedHashMap<String, String>();

        // Relevant terms to extract are tutorialName, tutorialDayOfWeek, studentListString, tutorialModuleCode,
        // tutorialStartTime, tutorialDuration, tutorialWeeks.

        String tutorialNameFromTutorialString = tutorialString.substring(tutorialString.indexOf("tutorialName=") +
                "tutorialName=".length(), tutorialString.indexOf("tutorialDayOfWeek=") - 2);

        return tutorialStringToMap;
    }

    public String studentListToString(List<Student> studentList) {
        String studentListString = "[";

        for (Student s : studentList ) {
            LinkedHashMap<String, String> studentMap = new LinkedHashMap<String, String>();
            studentMap.put(STUDENT_NAME, s.getName().toString());
            studentMap.put(STUDENT_EMAIL, s.getEmail().toString());
            studentMap.put(STUDENT_MATRIC_NUMBER, s.getMatricNum().toString());
            studentMap.put(STUDENT_NUSNET_ID, s.getNusnetId().toString());
            studentMap.put(STUDENT_MODULE_CODE, s.getModCode().toString());
            studentMap.put(STUDENT_TUTORIAL_NAME, s.getTutName().toString());
            studentListString = studentListString + studentMap.toString() + "],[";
        }

        // Remove the last instance of "[,]" from studentListString
        if (studentList.size() != 0) {
            studentListString = studentListString.substring(0, (studentListString.length() - 2));
        } else {
            // There are no students in the list. studentListString is just "[]".
            studentListString = studentListString + "]";
        }
        return studentListString;
    }

    public Student jsonStringToStudent(String studentString, ModCode modCode, String tutorialName) {
        String name = studentString.substring(0, studentString.indexOf("Email:")).trim();
        String email = studentString.substring(studentString.indexOf("Email:") + "Email:".length(),
                studentString.indexOf("Matric Number:")).trim();
        String matricNum = studentString.substring(studentString.indexOf("Matric Number:")
                + "Matric Number:".length(), studentString.indexOf("NUSNET Id:")).trim();
        String nusnetId = studentString.substring(studentString.indexOf("NUSNET Id:" )
                + "NUSNET Id:".length()).strip();

                    /*
                    System.out.println("Name: " + name);
                    System.out.println("Email: " + email);
                    System.out.println("Matric Num: " + matricNum);
                    System.out.println("NUSNET ID: " + nusnetId);
                    */
        Optional<MatricNum> jsonStudentMatricNum = Optional.empty();
        if (matricNum.contains("empty")) {
            jsonStudentMatricNum = Optional.empty();
        } else {
            // Parse the string as matricNum is "Optional{A0155413M]"
            matricNum = matricNum.replace("Optional[", "");
            matricNum = matricNum.replace("]", "");

            jsonStudentMatricNum = Optional.of(new MatricNum(matricNum));
        }

        Optional<NusnetId> jsonStudentNusnetId = Optional.empty();
        if (nusnetId.contains("empty")) {
            jsonStudentNusnetId = Optional.empty();
        } else {
            // Parse the string as nusnedId is "Optional[E0031550]"
            nusnetId = nusnetId.replace("Optional[", "");
            nusnetId = nusnetId.replace("]", "");

            jsonStudentNusnetId = Optional.of(new NusnetId(nusnetId));
        }

        Name jsonStudentName = new Name(name);
        Email jsonStudentEmail = new Email(email);
        ModCode jsonStudentModCode = modCode;
        TutName jsonTutorialName = new TutName(tutorialName);

        return new Student(jsonStudentName, jsonStudentEmail, jsonStudentMatricNum,
                jsonStudentNusnetId, jsonStudentModCode, jsonTutorialName);

    }


    /**
     * Checks if the studentString contains is valid ie contains the strings "Email:", "Matric Number:" and
     * "NUSNED Id:".
     *
     * @param studentString String representing a Student from Json object.
     * @return Boolean.
     */
    public Boolean isValidStudentString (String studentString) {
        return (studentString.contains("Email:") && studentString.contains("Matric Number:")
                && studentString.contains("NUSNET Id:"));
    }
}

