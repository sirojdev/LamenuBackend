/*
 * Lamenu documentation Merchant
 * Lamenu documentation Merchant allows to you view the schema of Merchant Lamenu project
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import org.openapitools.client.model.TextModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openapitools.client.JSON;

/**
 * CategoryGroupDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-09-03T23:20:57.815967300+05:00[Asia/Tashkent]")
public class CategoryGroupDto {
  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private Integer id;

  public static final String SERIALIZED_NAME_MERCHANT_ID = "merchantId";
  @SerializedName(SERIALIZED_NAME_MERCHANT_ID)
  private Integer merchantId;

  public static final String SERIALIZED_NAME_TITLE = "title";
  @SerializedName(SERIALIZED_NAME_TITLE)
  private TextModel title;

  public static final String SERIALIZED_NAME_BG_COLOR = "bgColor";
  @SerializedName(SERIALIZED_NAME_BG_COLOR)
  private String bgColor;

  public static final String SERIALIZED_NAME_TEXT_COLOR = "textColor";
  @SerializedName(SERIALIZED_NAME_TEXT_COLOR)
  private String textColor;

  public CategoryGroupDto() {
  }

  public CategoryGroupDto id(Integer id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getId() {
    return id;
  }


  public void setId(Integer id) {
    this.id = id;
  }


  public CategoryGroupDto merchantId(Integer merchantId) {
    
    this.merchantId = merchantId;
    return this;
  }

   /**
   * Get merchantId
   * @return merchantId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Integer getMerchantId() {
    return merchantId;
  }


  public void setMerchantId(Integer merchantId) {
    this.merchantId = merchantId;
  }


  public CategoryGroupDto title(TextModel title) {
    
    this.title = title;
    return this;
  }

   /**
   * Get title
   * @return title
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public TextModel getTitle() {
    return title;
  }


  public void setTitle(TextModel title) {
    this.title = title;
  }


  public CategoryGroupDto bgColor(String bgColor) {
    
    this.bgColor = bgColor;
    return this;
  }

   /**
   * Get bgColor
   * @return bgColor
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getBgColor() {
    return bgColor;
  }


  public void setBgColor(String bgColor) {
    this.bgColor = bgColor;
  }


  public CategoryGroupDto textColor(String textColor) {
    
    this.textColor = textColor;
    return this;
  }

   /**
   * Get textColor
   * @return textColor
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getTextColor() {
    return textColor;
  }


  public void setTextColor(String textColor) {
    this.textColor = textColor;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoryGroupDto categoryGroupDto = (CategoryGroupDto) o;
    return Objects.equals(this.id, categoryGroupDto.id) &&
        Objects.equals(this.merchantId, categoryGroupDto.merchantId) &&
        Objects.equals(this.title, categoryGroupDto.title) &&
        Objects.equals(this.bgColor, categoryGroupDto.bgColor) &&
        Objects.equals(this.textColor, categoryGroupDto.textColor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, merchantId, title, bgColor, textColor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoryGroupDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    merchantId: ").append(toIndentedString(merchantId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    bgColor: ").append(toIndentedString(bgColor)).append("\n");
    sb.append("    textColor: ").append(toIndentedString(textColor)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("id");
    openapiFields.add("merchantId");
    openapiFields.add("title");
    openapiFields.add("bgColor");
    openapiFields.add("textColor");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to CategoryGroupDto
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (CategoryGroupDto.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in CategoryGroupDto is not found in the empty JSON string", CategoryGroupDto.openapiRequiredFields.toString()));
        }
      }

      Set<Entry<String, JsonElement>> entries = jsonObj.entrySet();
      // check to see if the JSON string contains additional fields
      for (Entry<String, JsonElement> entry : entries) {
        if (!CategoryGroupDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `CategoryGroupDto` properties. JSON: %s", entry.getKey(), jsonObj.toString()));
        }
      }
      // validate the optional field `title`
      if (jsonObj.get("title") != null && !jsonObj.get("title").isJsonNull()) {
        TextModel.validateJsonObject(jsonObj.getAsJsonObject("title"));
      }
      if ((jsonObj.get("bgColor") != null && !jsonObj.get("bgColor").isJsonNull()) && !jsonObj.get("bgColor").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `bgColor` to be a primitive type in the JSON string but got `%s`", jsonObj.get("bgColor").toString()));
      }
      if ((jsonObj.get("textColor") != null && !jsonObj.get("textColor").isJsonNull()) && !jsonObj.get("textColor").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `textColor` to be a primitive type in the JSON string but got `%s`", jsonObj.get("textColor").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!CategoryGroupDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'CategoryGroupDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<CategoryGroupDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(CategoryGroupDto.class));

       return (TypeAdapter<T>) new TypeAdapter<CategoryGroupDto>() {
           @Override
           public void write(JsonWriter out, CategoryGroupDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public CategoryGroupDto read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             return thisAdapter.fromJsonTree(jsonObj);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of CategoryGroupDto given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of CategoryGroupDto
  * @throws IOException if the JSON string is invalid with respect to CategoryGroupDto
  */
  public static CategoryGroupDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, CategoryGroupDto.class);
  }

 /**
  * Convert an instance of CategoryGroupDto to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

