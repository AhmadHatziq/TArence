package seedu.tarence.storage;

import java.util.ArrayList;
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
    private String moduleName;
    private ArrayList<String> tutorialListForModule;

    public JsonAdaptedModule(Module source) {
        moduleName = source.getModCode().toString();
        tutorialListForModule = new ArrayList<String>();
        for (Tutorial t : source.getTutorials()) {
            tutorialListForModule.add(t.toJsonString());
        }
    }

    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleName") String moduleName,
                             @JsonProperty("tutorialListForModule") ArrayList<String> tutorialListForModule) {
        this.moduleName = moduleName;
        this.tutorialListForModule = tutorialListForModule;
    }

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

    public String getModuleName() {
        return this.moduleName;
    }

    public ArrayList<String> getTutorialListForModule() {
        return this.tutorialListForModule;
    }
}
