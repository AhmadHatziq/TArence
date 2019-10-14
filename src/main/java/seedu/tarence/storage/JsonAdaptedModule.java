package seedu.tarence.storage;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.tarence.commons.exceptions.IllegalValueException;
import seedu.tarence.logic.parser.ParserUtil;
import seedu.tarence.logic.parser.exceptions.ParseException;
import seedu.tarence.model.module.ModCode;
import seedu.tarence.model.module.Module;
import seedu.tarence.model.person.Email;
import seedu.tarence.model.person.Name;
import seedu.tarence.model.student.MatricNum;
import seedu.tarence.model.student.NusnetId;
import seedu.tarence.model.student.Student;
import seedu.tarence.model.tutorial.TutName;
import seedu.tarence.model.tutorial.Tutorial;
import seedu.tarence.model.tutorial.Week;

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
    public static final String STUDENT_NAME = "studentName";
    public static final String STUDENT_EMAIL = "studentEmail";
    public static final String STUDENT_MATRIC_NUMBER = "studentMatricNumber";
    public static final String STUDENT_NUSNET_ID = "studentNusnetId";
    public static final String STUDENT_MODULE_CODE = "studentModuleCode";
    public static final String STUDENT_TUTORIAL_NAME = "studentTutorialName";

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Tutorial's %s field is missing!";
    public static final String INVALID_FIELD_MESSAGE_FORMAT = "Tutorial's %s field is invalid!";
    public static final String MISSING_GENERIC_FIELD = "Error in reading field! ";
    public static final String INVALID_FIELD = "Invalid field in %s";

    // Json fields
    private String moduleCode;
    private LinkedHashMap<String, String> mapOfDifferentTutorials; // Implemented LinkedHashMap to preserve ordering.

    // Constructor from Json file. Invoked during reading the file.
    @JsonCreator
    public JsonAdaptedModule(@JsonProperty("moduleCode") String moduleName,
                             @JsonProperty("tutorialMap") LinkedHashMap<String, String> map) {
        this.moduleCode = moduleName;
        this.mapOfDifferentTutorials = map;
    }

    // Constructor from Module object. Invoked during saving of the file.
    public JsonAdaptedModule(Module source) {
        moduleCode = source.getModCode().toString();
        mapOfDifferentTutorials = new LinkedHashMap<String, String>();

        for (Tutorial t : source.getTutorials()) {
            LinkedHashMap<String, String> singleTutorialMap = new LinkedHashMap<String, String>();

            // Obtain all the fields that defines a single Tutorial object.
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

            mapOfDifferentTutorials.put(tutorialName, singleTutorialMap.toString());
        }
    }


    /**
     * Converts JsonAdaptedModule into a Module object. Invoked during reading of Json file.
     *
     * @return Module object.
     * @throws IllegalValueException when there is an error in reading one of the fields.
     */
    public Module toModelType() throws IllegalValueException {
        List<Tutorial> tutorials = new ArrayList<Tutorial>();

        for (String tutorialName : mapOfDifferentTutorials.keySet()) {
            // Parses the tutorialString into a LinkedHashMap of the Tutorial's components.
            LinkedHashMap<String, String> mapOfSingleTutorial = tutorialStringToMap(mapOfDifferentTutorials.get(tutorialName));

            // Creates a Tutorial Object from the tutorialString
            Tutorial tutorialFromJson = tutorialMapToTutorial(mapOfSingleTutorial);

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
     * Returns a Tutorial Object given the TutorialMap constructed from Json.
     *
     * @param tutorialMap LinkedHashMap obtained after parsing Tutorial String.
     * @return Tutorial object.
     * @throws IllegalValueException when Tutorial components are unable to be parsed correctly from Strings.
     */
    public Tutorial tutorialMapToTutorial(LinkedHashMap<String, String> tutorialMap)
            throws IllegalValueException {
        try {
            List<Student> studentList = studentStringToList(tutorialMap.get(TUTORIAL_STUDENT_LIST));
            TutName tutorialName = new TutName(tutorialMap.get(TUTORIAL_NAME));
            DayOfWeek day = ParserUtil.parseDayOfWeek(tutorialMap.get(TUTORIAL_DAY));
            Set<Week> weeks = ParserUtil.parseWeeks(tutorialMap.get(TUTORIAL_WEEKS));
            ModCode modCode = ParserUtil.parseModCode(tutorialMap.get(TUTORIAL_MODULE_CODE));
            Duration duration = Duration.parse(tutorialMap.get(TUTORIAL_DURATION));
            LocalTime startTime = LocalTime.parse(tutorialMap.get(TUTORIAL_START_TIME), DateTimeFormatter.ISO_TIME);

            return new Tutorial(tutorialName, day, startTime, weeks, duration, studentList, modCode);
        } catch (ParseException e) {
            throw new IllegalValueException(MISSING_GENERIC_FIELD + e.getMessage());
        } catch (DateTimeParseException e) {
            // Thrown by either Duration or LocalTime objects.
            String errorMessage = String.format(INVALID_FIELD_MESSAGE_FORMAT, Duration.class.getSimpleName())
                    + " Or " + String.format(INVALID_FIELD_MESSAGE_FORMAT, LocalTime.class.getSimpleName());
            throw new IllegalValueException(errorMessage);
        }
    }

    /**
     * Converts a student ListString to a list of Students.
     *
     * @param studentListString A String representing 0,1 or more Students.
     * @return List of Student objects.
     */
    public List<Student> studentStringToList(String studentListString) throws IllegalValueException {
        List<Student> studentList = new ArrayList<Student>();
        if (studentListString.equals("[]")) {
            return studentList; //studentString is empty.
        }

        String[] students = studentListString.split("\\]\\,\\[");

        for (String s : students) {
            String studentString = s.replace("[", "").replace("]", "");
            Student studentFromJson = studentStringToStudent(studentString);
            studentList.add(studentFromJson);
        }
        return studentList;
    }

    /**
     * Converts a studentString, representing a single student, to a Student object.
     *
     * @param studentString String sequence representing a single Student.
     * @return Student object.
     */
    public Student studentStringToStudent(String studentString) throws IllegalValueException {
        if (!isValidStudentString(studentString)) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Student.class.getSimpleName()));
        }

        // Extracts the correct Strings needed to create a Student object.
        String studentNameString = extractField(STUDENT_NAME, STUDENT_EMAIL, studentString);
        String studentEmailString = extractField(STUDENT_EMAIL, STUDENT_MATRIC_NUMBER, studentString);
        String studentMatricNumberString = extractField(STUDENT_MATRIC_NUMBER, STUDENT_NUSNET_ID, studentString);
        String studentNusnetIdString = extractField(STUDENT_NUSNET_ID, STUDENT_MODULE_CODE, studentString);
        String studentModuleCodeString = extractField(STUDENT_MODULE_CODE, STUDENT_TUTORIAL_NAME, studentString);
        String studentTutorialNameString = extractLastField(STUDENT_TUTORIAL_NAME, studentString);

        Student studentFromJson = studentStringsToStudent(studentNameString, studentEmailString,
                studentMatricNumberString, studentNusnetIdString,
                studentModuleCodeString, studentTutorialNameString);

        return studentFromJson;
    }

    /**
     * Converts a tutorialString to a LinkedHashMap. Represents the components needed to construct a Tutorial object.
     *
     * @param tutorialString String.
     * @return LinkedHashMap.
     */
    public LinkedHashMap<String, String> tutorialStringToMap(String tutorialString) throws IllegalValueException {
        if (!isValidTutorialString(tutorialString)) {
            throw new IllegalValueException("Tutorial string has invalid fields");
        }

        LinkedHashMap<String, String> tutorialStringToMap = new LinkedHashMap<String, String>();
        // Extracts the correct Strings needed to populate the LinkedHashMap.
        // Relevant terms to extract are tutorialName, tutorialDayOfWeek, studentListString, tutorialModuleCode,
        // tutorialStartTime, tutorialDuration, tutorialWeeks.
        String tutorialNameFromTutorialString = extractField(TUTORIAL_NAME, TUTORIAL_DAY, tutorialString);
        String tutorialDayOfWeek = extractField(TUTORIAL_DAY, TUTORIAL_START_TIME, tutorialString);
        String tutorialStartTime = extractField(TUTORIAL_START_TIME, TUTORIAL_WEEKS, tutorialString);
        String tutorialWeeks = extractField(TUTORIAL_WEEKS, TUTORIAL_DURATION, tutorialString);
        String tutorialDuration = extractField(TUTORIAL_DURATION, TUTORIAL_STUDENT_LIST, tutorialString);
        String tutorialStudentList = extractField(TUTORIAL_STUDENT_LIST, TUTORIAL_MODULE_CODE, tutorialString);
        String tutorialModuleCode = extractLastField(TUTORIAL_MODULE_CODE, tutorialString);

        // Places the extracted Strings into a HashMap
        tutorialStringToMap.put(TUTORIAL_NAME, tutorialNameFromTutorialString);
        tutorialStringToMap.put(TUTORIAL_DAY, tutorialDayOfWeek);
        tutorialStringToMap.put(TUTORIAL_START_TIME, tutorialStartTime);
        tutorialStringToMap.put(TUTORIAL_WEEKS, tutorialWeeks);
        tutorialStringToMap.put(TUTORIAL_DURATION, tutorialDuration);
        tutorialStringToMap.put(TUTORIAL_STUDENT_LIST, tutorialStudentList);
        tutorialStringToMap.put(TUTORIAL_MODULE_CODE, tutorialModuleCode);

        return tutorialStringToMap;
    }

    /**
     * Creates a Student Object with the given parsed String params.
     *
     * @param studentNameString Parsed student name string.
     * @param studentEmailString Parsed student email string.
     * @param studentMatricNumberString Parsed student matriculation number string.
     * @param studentNusnetIdString Parsed student NUSNET ID string.
     * @param studentModuleCodeString Parsed student module code string.
     * @param studentTutorialNameString Parsed student tutorial string.
     * @return Student object.
     */
    public Student studentStringsToStudent(String studentNameString, String studentEmailString,
                                           String studentMatricNumberString, String studentNusnetIdString,
                                           String studentModuleCodeString, String studentTutorialNameString) {

        // Populates the fields needed to create a Student object.
        Name studentName = new Name(studentNameString);

        Email studentEmail = new Email(studentEmailString);

        Optional<MatricNum> studentMatricNumber = Optional.empty();
        if (studentMatricNumberString.contains("empty")) {
            studentMatricNumber = Optional.empty();
        } else {
            studentMatricNumberString = studentMatricNumberString.replace("Optional", "");
            studentMatricNumber = Optional.of(new MatricNum(studentMatricNumberString));
        }

        Optional<NusnetId> studentNusnetId = Optional.empty();
        if (studentNusnetIdString.contains("empty")) {
            studentNusnetId = Optional.empty();
        } else {
            studentNusnetIdString = studentNusnetIdString.replace("Optional", "");
            studentNusnetId = Optional.of(new NusnetId(studentNusnetIdString));
        }

        ModCode studentModuleCode = new ModCode(studentModuleCodeString);

        TutName studentTutorialName = new TutName(studentTutorialNameString);

        return new Student(studentName, studentEmail, studentMatricNumber, studentNusnetId,
                studentModuleCode, studentTutorialName);
    }

    /**
     * Converts a list of Students to a String.
     * Eg. [{studentName=Ellie Yee, studentEmail=e0035152@u.nus.edu.sg, studentMatricNumber=OptionalA0155413M,
     * studentNusnetId=OptionalE0031550, studentModuleCode=CS1010S, studentTutorialName=Lab Session}]
     *
     * @param studentList List of Student objects.
     * @return String representation of a Student List.
     */
    public String studentListToString(List<Student> studentList) {
        String studentListString = "[";

        for (Student s : studentList) {
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

    /**
     * Returns the last field of an identifier.
     *
     * @param identifier Last field to be extracted from a String sequence.
     * @param sequence String that contains the last field and identifier.
     * @return Exact String field of the identifier.
     */
    public String extractLastField(String identifier, String sequence) {
        return sequence.substring(sequence.indexOf(identifier)
                + identifier.length() + 1).replace("}", "").trim();
    }

    /**
     * Returns the exact field of an identifier.
     * Pre-condition: Desired field must not be the last field of the string.
     *
     * @param identifier Desired field to extract.
     * @param nextIdentifier Subsequent identifier located after the desired identified.
     * @param sequence String that contains the fields and identifiers.
     * @return Exact String field of the identifier.
     */
    public String extractField(String identifier, String nextIdentifier, String sequence) {
        return sequence.substring(sequence.indexOf(identifier)
                + identifier.length() + 1, sequence.indexOf(nextIdentifier) - 2).trim();
    }

    /**
     * Checks if the studentString contains valid fields and the order correct.
     *
     * @param studentString String representing a Student from Json object.
     * @return Boolean.
     */
    public Boolean isValidStudentString (String studentString) {
        return (studentString.contains(STUDENT_EMAIL) && studentString.contains(STUDENT_MATRIC_NUMBER)
                && studentString.contains(STUDENT_MODULE_CODE) && studentString.contains(STUDENT_NAME)
                && studentString.contains(STUDENT_NUSNET_ID) && studentString.contains(STUDENT_TUTORIAL_NAME)
                && (studentString.indexOf(STUDENT_NAME) < studentString.indexOf(STUDENT_EMAIL))
                && (studentString.indexOf(STUDENT_EMAIL) < studentString.indexOf(STUDENT_MATRIC_NUMBER))
                && (studentString.indexOf(STUDENT_MATRIC_NUMBER) < studentString.indexOf(STUDENT_NUSNET_ID))
                && (studentString.indexOf(STUDENT_NUSNET_ID) < studentString.indexOf(STUDENT_MODULE_CODE))
                && (studentString.indexOf(STUDENT_MODULE_CODE) < studentString.indexOf(STUDENT_TUTORIAL_NAME)));
    }

    /**
     * Checks if the tutorialString contains valid fields.
     *
     * @param tutorialString
     * @return Boolean.
     */
    public Boolean isValidTutorialString (String tutorialString) {
        return (tutorialString.contains(TUTORIAL_WEEKS) && tutorialString.contains(TUTORIAL_DAY)
                && tutorialString.contains(TUTORIAL_DURATION) && tutorialString.contains(TUTORIAL_MODULE_CODE)
                && tutorialString.contains(TUTORIAL_NAME) && tutorialString.contains(TUTORIAL_START_TIME)
                && tutorialString.contains(TUTORIAL_STUDENT_LIST)
                && (tutorialString.indexOf(TUTORIAL_NAME) < tutorialString.indexOf(TUTORIAL_DAY))
                && (tutorialString.indexOf(TUTORIAL_DAY) < tutorialString.indexOf(TUTORIAL_START_TIME))
                && (tutorialString.indexOf(TUTORIAL_START_TIME) < tutorialString.indexOf(TUTORIAL_WEEKS))
                && (tutorialString.indexOf(TUTORIAL_WEEKS) < tutorialString.indexOf(TUTORIAL_DURATION))
                && (tutorialString.indexOf(TUTORIAL_DURATION) < tutorialString.indexOf(TUTORIAL_STUDENT_LIST))
                && (tutorialString.indexOf(TUTORIAL_STUDENT_LIST) < tutorialString.indexOf(TUTORIAL_MODULE_CODE)));
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

