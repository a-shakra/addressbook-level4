package seedu.address.storage;

import java.time.LocalDateTime;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Task;
import seedu.address.model.personal.PersonalTask;
import seedu.address.model.tutee.TuitionTask;

/**
 * JAXB-friendly version of the Task.
 */
//@@author a-shakra
public class XmlAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String duration;
    @XmlElement(required = true)
    private String dateAndTime;

    /**
     * Constructs an XmlAdaptedTask.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTask() {}

    /**
     * Constructs an {@code XmlAdaptedTask} with given personal task details.
     */
    public XmlAdaptedTask(String description, String duration, String dateAndTime) {
        //this.name = "null";
        this.description = description;
        this.duration = duration;
        this.dateAndTime = dateAndTime;
    }

    public XmlAdaptedTask(String name, String description, String duration, String dateAndTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.dateAndTime = dateAndTime;
    }

    /**
     * Converts a given Task into this class for JAXB use.
     *
     */
    public XmlAdaptedTask(Task source) {
        description = source.getDescription();
        duration = source.getDuration();
        dateAndTime = source.getTaskDateTime().toString();
        if (source instanceof TuitionTask) {
            name = ((TuitionTask) source).getPerson();
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     * Because of the way Task was designed (As an interface), i'm forced to just input this as a PersonalTask
     * until a better solution can be found
     */

    public Task toModelType() throws IllegalValueException {
        LocalDateTime taskDateTime = LocalDateTime.parse(dateAndTime);
        if (this.description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Task.MESSAGE_DESCRIPTION_CONSTRAINTS));
        }
        if (this.duration == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Task.MESSAGE_DURATION_CONSTRAINTS));
        }
        if (this.dateAndTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Task.MESSAGE_DATETIME_CONSTRAINTS));
        }
        if (this.name == null) {
            return new PersonalTask(taskDateTime, duration, description);
        } else {
            return new TuitionTask(name, taskDateTime, duration, description);
        }
    }

    /**
     * Returns true if the two tasks are equal. Needs to be updated to reflect the name parameter
     */
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTask)) {
            return false;
        }

        XmlAdaptedTask otherTask = (XmlAdaptedTask) other;
        return Objects.equals(description, otherTask.description)
                && Objects.equals(duration, otherTask.duration)
                && Objects.equals(dateAndTime, otherTask.dateAndTime)
                && Objects.equals(name, otherTask.name);
    }
}
