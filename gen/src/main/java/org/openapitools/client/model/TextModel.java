/*
 * Lamenu documentation
 * Lamenu documentation allows to you view the schema of Lamenu project
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

/**
 * TextModel
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-15T14:01:51.605023389+05:00[Asia/Tashkent]")
public class TextModel {
  public static final String SERIALIZED_NAME_UZ = "uz";
  @SerializedName(SERIALIZED_NAME_UZ)
  private String uz;

  public static final String SERIALIZED_NAME_RU = "ru";
  @SerializedName(SERIALIZED_NAME_RU)
  private String ru;

  public static final String SERIALIZED_NAME_ENG = "eng";
  @SerializedName(SERIALIZED_NAME_ENG)
  private String eng;


  public TextModel uz(String uz) {
    
    this.uz = uz;
    return this;
  }

   /**
   * Get uz
   * @return uz
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getUz() {
    return uz;
  }


  public void setUz(String uz) {
    this.uz = uz;
  }


  public TextModel ru(String ru) {
    
    this.ru = ru;
    return this;
  }

   /**
   * Get ru
   * @return ru
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getRu() {
    return ru;
  }


  public void setRu(String ru) {
    this.ru = ru;
  }


  public TextModel eng(String eng) {
    
    this.eng = eng;
    return this;
  }

   /**
   * Get eng
   * @return eng
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getEng() {
    return eng;
  }


  public void setEng(String eng) {
    this.eng = eng;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextModel textModel = (TextModel) o;
    return Objects.equals(this.uz, textModel.uz) &&
        Objects.equals(this.ru, textModel.ru) &&
        Objects.equals(this.eng, textModel.eng);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uz, ru, eng);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextModel {\n");
    sb.append("    uz: ").append(toIndentedString(uz)).append("\n");
    sb.append("    ru: ").append(toIndentedString(ru)).append("\n");
    sb.append("    eng: ").append(toIndentedString(eng)).append("\n");
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

}

