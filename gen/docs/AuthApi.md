# AuthApi

All URIs are relative to *http://127.0.0.1:9000/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**clientAuthSendSmsPost**](AuthApi.md#clientAuthSendSmsPost) | **POST** /client/auth/send-sms | 
[**clientAuthSignUpPost**](AuthApi.md#clientAuthSignUpPost) | **POST** /client/auth/sign-up | 
[**clientAuthVerifyPost**](AuthApi.md#clientAuthVerifyPost) | **POST** /client/auth/verify | 
[**clientDevicePost**](AuthApi.md#clientDevicePost) | **POST** /client/device | 


<a name="clientAuthSendSmsPost"></a>
# **clientAuthSendSmsPost**
> List&lt;DeviceModel&gt; clientAuthSendSmsPost(inlineObject)



Auth client sms

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AuthApi apiInstance = new AuthApi(defaultClient);
    InlineObject inlineObject = new InlineObject(); // InlineObject | 
    try {
      List<DeviceModel> result = apiInstance.clientAuthSendSmsPost(inlineObject);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#clientAuthSendSmsPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **inlineObject** | [**InlineObject**](InlineObject.md)|  |

### Return type

[**List&lt;DeviceModel&gt;**](DeviceModel.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

<a name="clientAuthSignUpPost"></a>
# **clientAuthSignUpPost**
> List&lt;DeviceModel&gt; clientAuthSignUpPost(inlineObject2)



Auth client sign up

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AuthApi apiInstance = new AuthApi(defaultClient);
    InlineObject2 inlineObject2 = new InlineObject2(); // InlineObject2 | 
    try {
      List<DeviceModel> result = apiInstance.clientAuthSignUpPost(inlineObject2);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#clientAuthSignUpPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **inlineObject2** | [**InlineObject2**](InlineObject2.md)|  |

### Return type

[**List&lt;DeviceModel&gt;**](DeviceModel.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

<a name="clientAuthVerifyPost"></a>
# **clientAuthVerifyPost**
> List&lt;DeviceModel&gt; clientAuthVerifyPost(inlineObject1)



Auth client verify

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    AuthApi apiInstance = new AuthApi(defaultClient);
    InlineObject1 inlineObject1 = new InlineObject1(); // InlineObject1 | 
    try {
      List<DeviceModel> result = apiInstance.clientAuthVerifyPost(inlineObject1);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#clientAuthVerifyPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **inlineObject1** | [**InlineObject1**](InlineObject1.md)|  |

### Return type

[**List&lt;DeviceModel&gt;**](DeviceModel.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

<a name="clientDevicePost"></a>
# **clientDevicePost**
> List&lt;DeviceModel&gt; clientDevicePost()



Auth client for device

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AuthApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");

    AuthApi apiInstance = new AuthApi(defaultClient);
    try {
      List<DeviceModel> result = apiInstance.clientDevicePost();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#clientDevicePost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;DeviceModel&gt;**](DeviceModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

