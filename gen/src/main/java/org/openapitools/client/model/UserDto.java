/*
 * Lamenu documentation Client
 * Lamenu documentation Clint allows to you view the schema of Client Lamenu project
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
import org.openapitools.client.model.BadgeDto;

/**
 * UserDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-06-25T19:44:26.434788700+05:00[Asia/Tashkent]")
public class UserDto {
  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private Integer id;

  public static final String SERIALIZED_NAME_MERCHANT_ID = "merchantId";
  @SerializedName(SERIALIZED_NAME_MERCHANT_ID)
  private Integer merchantId;

  public static final String SERIALIZED_NAME_BADGE = "badge";
  @SerializedName(SERIALIZED_NAME_BADGE)
  private BadgeDto badge;

  public static final String SERIALIZED_NAME_PHONE = "phone";
  @SerializedName(SERIALIZED_NAME_PHONE)
  private String phone;

  public static final String SERIALIZED_NAME_FIRST_NAME = "firstName";
  @SerializedName(SERIALIZED_NAME_FIRST_NAME)
  private String firstName;

  public static final String SERIALIZED_NAME_LAST_NAME = "lastName";
  @SerializedName(SERIALIZED_NAME_LAST_NAME)
  private String lastName;

  public static final String SERIALIZED_NAME_IMAGE = "image";
  @SerializedName(SERIALIZED_NAME_IMAGE)
  private String image;

  public static final String SERIALIZED_NAME_BIRTH_DAY = "birthDay";
  @SerializedName(SERIALIZED_NAME_BIRTH_DAY)
  private String birthDay;

  public static final String SERIALIZED_NAME_TOKEN = "token";
  @SerializedName(SERIALIZED_NAME_TOKEN)
  private String token;


  public UserDto id(Integer id) {
    
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


  public UserDto merchantId(Integer merchantId) {
    
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


  public UserDto badge(BadgeDto badge) {
    
    this.badge = badge;
    return this;
  }

   /**
   * Get badge
   * @return badge
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public BadgeDto getBadge() {
    return badge;
  }


  public void setBadge(BadgeDto badge) {
    this.badge = badge;
  }


  public UserDto phone(String phone) {
    
    this.phone = phone;
    return this;
  }

   /**
   * Get phone
   * @return phone
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getPhone() {
    return phone;
  }


  public void setPhone(String phone) {
    this.phone = phone;
  }


  public UserDto firstName(String firstName) {
    
    this.firstName = firstName;
    return this;
  }

   /**
   * Get firstName
   * @return firstName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getFirstName() {
    return firstName;
  }


  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public UserDto lastName(String lastName) {
    
    this.lastName = lastName;
    return this;
  }

   /**
   * Get lastName
   * @return lastName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getLastName() {
    return lastName;
  }


  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  public UserDto image(String image) {
    
    this.image = image;
    return this;
  }

   /**
   * Get image
   * @return image
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getImage() {
    return image;
  }


  public void setImage(String image) {
    this.image = image;
  }


  public UserDto birthDay(String birthDay) {
    
    this.birthDay = birthDay;
    return this;
  }

   /**
   * Get birthDay
   * @return birthDay
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getBirthDay() {
    return birthDay;
  }


  public void setBirthDay(String birthDay) {
    this.birthDay = birthDay;
  }


  public UserDto token(String token) {
    
    this.token = token;
    return this;
  }

   /**
   * Get token
   * @return token
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public String getToken() {
    return token;
  }


  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDto userDto = (UserDto) o;
    return Objects.equals(this.id, userDto.id) &&
        Objects.equals(this.merchantId, userDto.merchantId) &&
        Objects.equals(this.badge, userDto.badge) &&
        Objects.equals(this.phone, userDto.phone) &&
        Objects.equals(this.firstName, userDto.firstName) &&
        Objects.equals(this.lastName, userDto.lastName) &&
        Objects.equals(this.image, userDto.image) &&
        Objects.equals(this.birthDay, userDto.birthDay) &&
        Objects.equals(this.token, userDto.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, merchantId, badge, phone, firstName, lastName, image, birthDay, token);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    merchantId: ").append(toIndentedString(merchantId)).append("\n");
    sb.append("    badge: ").append(toIndentedString(badge)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    birthDay: ").append(toIndentedString(birthDay)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

