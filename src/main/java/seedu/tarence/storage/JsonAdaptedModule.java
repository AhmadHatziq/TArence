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

    // Serializes the Module model into a Json object.
    public JsonAdaptedModule(Module source) {

        moduleCode = source.getModCode().toString();

        tutorialMap = new LinkedHashMap<String, String>();

        for (Tutorial t : source.getTutorials()) {
            LinkedHashMap<String, String> singleTutorialMap = new LinkedHashMap<String, String>();

            String tutorialName = t.getTutName().toString();
            String tutorialDayOfWeek = t.getTimeTable().getDay().toString();
            String tutorialStartTime = t.getTimeTable().getStartTime().toString();
            String tutorialWeeks = t.getTimeTable().getWeeks().toString();
            String tutorialDuration = t.getTimeTable().getDuration().toString();

            // Creates the studentListString.
            // Sample output for 2 students:
            // [{tutorialName=Lab Session, moduleCode=CS1010S, name=Ellie Yee, matricNumber=Optional[A0155413M],
            // nusnetId=Optional[E0031550], email=e0035152@u.nus.edu.sg}],
            // [{tutorialName=Lab Session, moduleCode=CS1010S, name=Prof Damith, matricNumber=Optional.empty,
            // nusnetId=Optional.empty, email=e0012352@u.nus.edu.sg}]
            String studentListString = "[";

            for (Student s : t.getStudents() ) {
                String studentName = s.getName().toString();
                String studentEmail = s.getEmail().toString();
                String studentMatricNumber = s.getMatricNum().toString();
                String studentNusnetId = s.getNusnetId().toString();
                String studentModuleCode = s.getModCode().toString();
                String studentTutorialName = s.getTutName().toString();

                LinkedHashMap<String, String> studentMap = new LinkedHashMap<String, String>();
                studentMap.put("name", studentName);
                studentMap.put("email", studentEmail);
                studentMap.put("matricNumber", studentMatricNumber);
                studentMap.put("nusnetId", studentNusnetId);
                studentMap.put("moduleCode", studentModuleCode);
                studentMap.put("tutorialName", studentTutorialName);

                studentListString = studentListString + studentMap.toString() + "],[";
            }

            // Remove the last instance of "[,]" from studentListString
            if (t.getStudents().size() != 0) {
                studentListString = studentListString.substring(0, (studentListString.length() - 2));
            } else {
                // There are no students in the list. studentListString is just "[]".
                studentListString = studentListString + "]";
            }
            // End of studentListString block

            String tutorialModuleCode = t.getModCode().toString();

            // Add into LinkedHashMap<String,String>, singleTutorialMap
            singleTutorialMap.put("tutorialName", tutorialName);
            singleTutorialMap.put("tutorialDayOfWeek", tutorialDayOfWeek);
            singleTutorialMap.put("tutorialStartTime", tutorialStartTime);
            singleTutorialMap.put("tutorialWeeks", tutorialWeeks);
            singleTutorialMap.put("tutorialDuration", tutorialDuration);
            singleTutorialMap.put("studentListString", studentListString);
            singleTutorialMap.put("tutorialModuleCode", tutorialModuleCode);

            tutorialMap.put(tutorialName, singleTutorialMap.toString());
        }
    }

    // Called during reading of Json File.
    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleCode") String moduleName,
                             @JsonProperty("tutorialMap") LinkedHashMap<String, String> map)  {
        this.moduleCode = moduleName;
        this.tutorialMap = map;
    }

    public Module toModelType() throws IllegalArgumentException {
        System.out.println("Module name: " + moduleCode);
        for (String tutorialName : tutorialMap.keySet()) {

            String tutorialString = tutorialMap.get(tutorialName);
            // tutorialString contains info about the whole tutorial ie name, day, list of students etc

            // Converts tutorialString to a LinkedhashMap<String,String> for easy parsing
            LinkedHashMap<String, String> tutorialMapFromJson = new LinkedHashMap<String, String>();

            // Relevant terms to extract are tutorialName, tutorialDayOfWeek, studentListString, tutorialModuleCode,
            // tutorialStartTime, tutorialDuration, tutorialWeeks.



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

