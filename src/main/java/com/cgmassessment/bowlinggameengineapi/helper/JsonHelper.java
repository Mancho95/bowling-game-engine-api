package com.cgmassessment.bowlinggameengineapi.helper;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonHelper {

  public static List<String> fromArrayNodeToList(ArrayNode arrayNode) {
    List<String> retList = new ArrayList<>();

    for (JsonNode element : arrayNode) {
      String elementString = JsonHelper.getString(element);
      if (elementString != null) {
        retList.add(elementString);
      }
    }

    return retList;
  }

  public static ObjectNode getEmptyObjectNode() {
    return JsonNodeFactory.instance.objectNode();
  }

  public static ArrayNode getEmptyArrayNode() {
    return JsonNodeFactory.instance.arrayNode();
  }

  public static boolean notEmpty(JsonNode node) {
    return node != null && !node.isNull();
  }

  public static boolean notEmpty(ArrayNode node) {
    return node != null && !node.isEmpty();
  }
  
  public static JsonNode get(JsonNode node, String key) {
	    JsonNode result = null;
	    if (node != null && !key.isEmpty()) {
	      result = node.get(key);
	      if (result == null) result = getDecoded(node, decodeListKeys(key));
	    }
	    return result;
	  }
  
  private static String[] decodeListKeys(String key) {
	    if (!key.isEmpty())
	      if (!key.contains(".")) return new String[] {key};
	      else return key.split("\\.");
	    return null;
	  }

  private static JsonNode getDecoded(JsonNode node, String[] keys) {
    JsonNode result = null;
    if (keys != null)
      if (keys.length == 1) result = node.get(keys[0]);
      else {
        int i = 0;
        result = node.get(keys[i++]);
        do {
          if (result != null) {
            String key = keys[i];
            if (key != null && key.length() > 0) result = result.get(key);
          } else break;
          i++;
        } while (i < keys.length);
      }
    return result;
  }

  public static ObjectNode getAsObject(JsonNode node) {
    if (node != null && node.isObject()) return (ObjectNode) node;
    return null;
  }

  public static ObjectNode getAsObject(JsonNode node, String key) {
    return getAsObject(get(node, key));
  }

  public static ArrayNode getAsArray(JsonNode node) {
    if (node != null && node.isArray()) return (ArrayNode) node;
    return null;
  }

  public static ArrayNode getAsArray(JsonNode node, String key) {
    return getAsArray(get(node, key));
  }

  public static String getString(JsonNode node, String key) {
    return getString(get(node, key));
  }

  public static String getString(JsonNode node) {
    if (node != null && node.isTextual()) return node.asText().trim();
    return null;
  }

  public static String getString(JsonNode node, String... keys) {
    Optional<JsonNode> result = Optional.ofNullable(node);
    for (String key : keys) {
      result = result.map(item -> item.get(key));
    }

    return result.filter(JsonNode::isTextual).map(JsonNode::asText).orElse(null);
  }

  public static Integer getInt(JsonNode node, String key) {
    JsonNode _o = get(node, key);
    if (_o != null && _o.isInt()) return _o.asInt();
    return null;
  }

  public static Long getLong(JsonNode node, String key) {
    JsonNode _o = get(node, key);
    if (_o != null && _o.isLong()) return _o.asLong();
    return null;
  }

  public static Boolean getBoolean(JsonNode node, String key) {
    JsonNode _o = get(node, key);
    if (_o != null && _o.isBoolean()) return _o.asBoolean();
    return null;
  }

  public static Boolean getBoolean(JsonNode node, String key, boolean defaultValue) {
    Boolean _o = getBoolean(node, key);
    return _o != null ? _o : defaultValue;
  }
  
  public static boolean hasAnyKey(ObjectNode objectNode, List<String> keys) {    
    for (String key : keys) {
      if (objectNode.has(key)) {
        return true;
      }      
    }
    return false;
  }

  public static boolean isEmpty(JsonNode jsonNode) {
    return jsonNode == null || jsonNode instanceof NullNode;
  }
  
  public static ObjectNode fromStringToObjectNode(String s) throws JsonMappingException, JsonProcessingException {
	  return new ObjectMapper().readValue(s, ObjectNode.class);
  }

}