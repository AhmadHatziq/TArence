package seedu.tarence.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.tarence.commons.exceptions.IllegalValueException;
import seedu.tarence.model.Application;
import seedu.tarence.model.ReadOnlyApplication;
import seedu.tarence.model.person.Person;
import seedu.tarence.model.module.Module;

/**
 * An Immutable application that is serializable to JSON format.
 */
@JsonRootName(value = "application")
class JsonSerializableApplication {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedModule> modules = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableApplication} with the given persons.
     * Reads the Json file and converts to model.
     */
    @JsonCreator
    public JsonSerializableApplication(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("modules") List<JsonAdaptedModule> modules) {
        this.persons.addAll(persons);

        // Toggles if modules is read or not
        this.modules.addAll(modules);
    }

    /**
     * Converts a given {@code ReadOnlyApplication} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableApplication}.
     */
    public JsonSerializableApplication(ReadOnlyApplication source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));

        // A JsonAdaptedModule is created for each module in the list.
        modules.addAll(source.getModuleList().stream().map(JsonAdaptedModule::new).collect(Collectors.toList()));
    }

    /**
     * Converts this application into the model's {@code Application} object.
     * Converts Json to models.
     *
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Application toModelType() throws IllegalValueException {
        Application application = new Application();


        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (application.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            application.addPerson(person);
        }

        for (JsonAdaptedModule jsonAdaptedModule : modules) {

            Module module = jsonAdaptedModule.toModelType();
            // module will be created and not added to application's module list yet
            System.out.println("Module created with data: " + module.toString());
        }



        return application;
    }

    public List<JsonAdaptedModule> getJsonAdaptedModules() {
        return this.modules;
    }

}
