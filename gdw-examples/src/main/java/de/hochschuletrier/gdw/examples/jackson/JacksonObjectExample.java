package de.hochschuletrier.gdw.examples.jackson;

import java.util.List;

import de.hochschuletrier.gdw.commons.jackson.JacksonList;

/**
 * An example object for the Jackson reader/writer
 *
 * @author Santo Pfingsten
 */
public class JacksonObjectExample {

    public String name;
    public Integer age;
    public Float progress;
    public Boolean master;
    public Gender gender;
    @JacksonList(Course.class)
    public List<Course> courses;
    @JacksonList(String.class)
    public List<String> tags;

    public static class Course {

        public Course() {
        }

        public Course(String name, int tries) {
            this.name = name;
            this.tries = tries;
        }
        public String name;
        public Integer tries;
    }

    public static enum Gender {

        MALE, FEMALE;
    }
}
