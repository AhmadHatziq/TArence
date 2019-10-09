package seedu.tarence.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.tarence.model.module.Module;
import seedu.tarence.model.tutorial.Tutorial;

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
        // this.tutorialListForModule = tutorialListForModule;

    }
/*
    public Module toModelType() throws IllegalArgumentException {

    }
*/
    public String getModuleName() {
        return this.moduleName;
    }

    public ArrayList<String> getTutorialListForModule() {
        return this.tutorialListForModule;
    }
}
