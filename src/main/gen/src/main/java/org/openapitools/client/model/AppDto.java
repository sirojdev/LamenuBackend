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
 * AppDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-09-03T23:20:57.815967300+05:00[Asia/Tashkent]")
public class AppDto {
  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private Integer id;

  public static final String SERIALIZED_NAME_MERCHANT_ID = "merchantId";
  @SerializedName(SERIALIZED_NAME_MERCHANT_ID)
  private Integer merchantId;

  public static final String SERIALIZED_NAME_GOOGLE_TOKEN = "googleToken";
  @SerializedName(SERIALIZED_NAME_GOOGLE_TOKEN)
  private String googleToken;

  public static final String SERIALIZED_NAME_APPLE_TOKEN = "appleToken";
  @SerializedName(SERIALIZED_NAME_APPLE_TOKEN)
  private String appleToken;

  public static final String SERIALIZED_NAME_TELEGRAM_BOT_TOKEN = "telegramBotToken";
  @SerializedName(SERIALIZED_NAME_TELEGRAM_BOT_TOKEN)
  private String telegramBotToken;

  public AppDto() {
  }

  public AppDto id(Integer id) {
    
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


  public AppDto merchantId(Integer merchantId) {
    
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


  public AppDto googleToken(String googleToken) {
    
    this.googleToken = googleToken;
    return this;
  }

   /**
   * Get googleToken
   * @return googleToken
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getGoogleToken() {
    return googleToken;
  }


  public void setGoogleToken(String googleToken) {
    this.googleToken = googleToken;
  }


  public AppDto appleToken(String appleToken) {
    
    this.appleToken = appleToken;
    return this;
  }

   /**
   * Get appleToken
   * @return appleToken
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getAppleToken() {
    return appleToken;
  }


  public void setAppleToken(String appleToken) {
    this.appleToken = appleToken;
  }


  public AppDto telegramBotToken(String telegramBotToken) {
    
    this.telegramBotToken = telegramBotToken;
    return this;
  }

   /**
   * Get telegramBotToken
   * @return telegramBotToken
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getTelegramBotToken() {
    return telegramBotToken;
  }


  public void setTelegramBotToken(String telegramBotToken) {
    this.telegramBotToken = telegramBotToken;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AppDto appDto = (AppDto) o;
    return Objects.equals(this.id, appDto.id) &&
        Objects.equals(this.merchantId, appDto.merchantId) &&
        Objects.equals(this.googleToken, appDto.googleToken) &&
        Objects.equals(this.appleToken, appDto.appleToken) &&
        Objects.equals(this.telegramBotToken, appDto.telegramBotToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, merchantId, googleToken, appleToken, telegramBotToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    merchantId: ").append(toIndentedString(merchantId)).append("\n");
    sb.append("    googleToken: ").append(toIndentedString(googleToken)).append("\n");
    sb.append("    appleToken: ").append(toIndentedString(appleToken)).append("\n");
    sb.append("    telegramBotToken: ").append(toIndentedString(telegramBotToken)).append("\n");
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
    openapiFields.add("googleToken");
    openapiFields.add("appleToken");
    openapiFields.add("telegramBotToken");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to AppDto
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (AppDto.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in AppDto is not found in the empty JSON string", AppDto.openapiRequiredFields.toString()));
        }
      }

      Set<Entry<String, JsonElement>> entries = jsonObj.entrySet();
      // check to see if the JSON string contains additional fields
      for (Entry<String, JsonElement> entry : entries) {
        if (!AppDto.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `AppDto` properties. JSON: %s", entry.getKey(), jsonObj.toString()));
        }
      }
      if ((jsonObj.get("googleToken") != null && !jsonObj.get("googleToken").isJsonNull()) && !jsonObj.get("googleToken").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `googleToken` to be a primitive type in the JSON string but got `%s`", jsonObj.get("googleToken").toString()));
      }
      if ((jsonObj.get("appleToken") != null && !jsonObj.get("appleToken").isJsonNull()) && !jsonObj.get("appleToken").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `appleToken` to be a primitive type in the JSON string but got `%s`", jsonObj.get("appleToken").toString()));
      }
      if ((jsonObj.get("telegramBotToken") != null && !jsonObj.get("telegramBotToken").isJsonNull()) && !jsonObj.get("telegramBotToken").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `telegramBotToken` to be a primitive type in the JSON string but got `%s`", jsonObj.get("telegramBotToken").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AppDto.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AppDto' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AppDto> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AppDto.class));

       return (TypeAdapter<T>) new TypeAdapter<AppDto>() {
           @Override
           public void write(JsonWriter out, AppDto value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public AppDto read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             return thisAdapter.fromJsonTree(jsonObj);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of AppDto given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of AppDto
  * @throws IOException if the JSON string is invalid with respect to AppDto
  */
  public static AppDto fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AppDto.class);
  }

 /**
  * Convert an instance of AppDto to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

