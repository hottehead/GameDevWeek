package de.hochschuletrier.gdw.commons.jackson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.utils.SilentCloser;
import java.io.OutputStream;

/**
 * Writing Json from objects
 *
 * @author Santo Pfingsten
 */
public class JacksonWriter {

    private static final JsonFactory factory = new JsonFactory();

    public static void writeList(String filename, List<?> list)
            throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InstantiationException, ParseException {

        SilentCloser closer = new SilentCloser();
        try {
            OutputStream outputStream = CurrentResourceLocator.write(filename);
            closer.set(outputStream);
            JsonGenerator generator = factory.createGenerator(outputStream, JsonEncoding.UTF8);
            closer.set(generator);

            generator.useDefaultPrettyPrinter();
            writeList(list, generator);
        } finally {
            closer.close();
        }
    }

    public static void write(String filename, Object object)
            throws IOException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InstantiationException, ParseException {

        SilentCloser closer = new SilentCloser();
        try {
            OutputStream outputStream = CurrentResourceLocator.write(filename);
            closer.set(outputStream);
            JsonGenerator generator = factory.createGenerator(outputStream, JsonEncoding.UTF8);
            closer.set(generator);

            generator.useDefaultPrettyPrinter();
            writeObject(object, generator);
        } finally {
            closer.close();
        }
    }

    private static void writeList(List<?> list, JsonGenerator generator)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        generator.writeStartArray();
        for (Object item : list) {
            if (item == null) {
                generator.writeNull();
            } else if (item instanceof String) {
                generator.writeString((String) item);
            } else if (item instanceof Integer) {
                generator.writeNumber((Integer) item);
            } else if (item instanceof Float) {
                generator.writeNumber((Float) item);
            } else if (item instanceof Boolean) {
                generator.writeBoolean((Boolean) item);
            } else if (item instanceof Enum) {
                generator.writeString(item.toString());
            } else if (item instanceof List) {
                writeList((List<?>) item, generator);
            } else {
                writeObject(item, generator);
            }
        }
        generator.writeEndArray();
    }

    private static void writeObject(Object object, JsonGenerator generator)
            throws InstantiationException, IllegalAccessException, IOException,
            NoSuchFieldException, ParseException {
        generator.writeStartObject();

        Class<?> clazz = object.getClass();

        // Check all declared, non-static fields
        for (Field field : getAllFields(new LinkedList<Field>(), clazz)) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {

                    if (value instanceof String) {
                        generator.writeStringField(field.getName(),
                                (String) value);
                    } else if (value instanceof Integer) {
                        generator.writeNumberField(field.getName(),
                                (Integer) value);
                    } else if (value instanceof Float) {
                        generator.writeNumberField(field.getName(),
                                (Float) value);
                    } else if (value instanceof Boolean) {
                        generator.writeBooleanField(field.getName(),
                                (Boolean) value);
                    } else if (value instanceof Enum) {
                        generator.writeStringField(field.getName(),
                                value.toString());
                    } else if (value instanceof List) {
                        generator.writeFieldName(field.getName());
                        writeList((List<?>) value, generator);
                    } else {
                        writeObject(value, generator);
                    }
                }
            }
        }

        generator.writeEndObject();
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }
}
