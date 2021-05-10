package it.polimi.ingsw.controller.gsonManager;

import com.google.gson.*;
import it.polimi.ingsw.model.Resources.Resource;

import java.lang.reflect.Type;

/**
 * this class is used to deserialize with gson the interface resource
 */
@SuppressWarnings("ALL")
public class ResourceInterfaceAdapter implements JsonSerializer<Resource>, JsonDeserializer<Resource> {

    /**
     * this is the name of the class
     */
    private static final String CLASSNAME = "Resource";

    /**
     * this is the attribute of the class
     */
    private static final String DATA = "ResourceType";

    /**
     * this method deserialize the resource
     * @param jsonElement this is the json element
     * @param type this is the type
     * @param jsonDeserializationContext this is the jsonDeserializationContext
     * @return the marble deserialized
     * @throws JsonParseException when a json parser exception occurs
     */
    public Resource deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        @SuppressWarnings("rawtypes") Class klass = getObjectClass(className);
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
    }

    /**
     * this method serialize the resource
     * @param jsonElement this is the marble to serialize
     * @param type this is the type
     * @param jsonSerializationContext this is the jsonSerializationContext
     * @return the json element associated with the resource
     */
    public JsonElement serialize(Resource jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
        return jsonObject;
    }

    /**
     * this method is a helper method to get the class name
     * @param className this is the string representing the name of the class
     * @return the class with the name specified in input
     */
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }
}
