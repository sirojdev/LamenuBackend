# AuthApi

All URIs are relative to *http://127.0.0.1:9000/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**merchantAuthPost**](AuthApi.md#merchantAuthPost) | **POST** /merchant/auth |  |
| [**staffAuthPost**](AuthApi.md#staffAuthPost) | **POST** /staff/auth |  |


<a name="merchantAuthPost"></a>
# **merchantAuthPost**
> List&lt;MerchantDto&gt; merchantAuthPost(merchantAuthPostRequest)



Via this endpoint you can get auth token

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
    MerchantAuthPostRequest merchantAuthPostRequest = new MerchantAuthPostRequest(); // MerchantAuthPostRequest | A JSON object containing staff information
    try {
      List<MerchantDto> result = apiInstance.merchantAuthPost(merchantAuthPostRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#merchantAuthPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **merchantAuthPostRequest** | [**MerchantAuthPostRequest**](MerchantAuthPostRequest.md)| A JSON object containing staff information | |

### Return type

[**List&lt;MerchantDto&gt;**](MerchantDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a name="staffAuthPost"></a>
# **staffAuthPost**
> List&lt;LoginResponse&gt; staffAuthPost(staffDto1)



Via this endpoint you can get auth token

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
    StaffDto1 staffDto1 = new StaffDto1(); // StaffDto1 | A JSON object containing staff information
    try {
      List<LoginResponse> result = apiInstance.staffAuthPost(staffDto1);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AuthApi#staffAuthPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **staffDto1** | [**StaffDto1**](StaffDto1.md)| A JSON object containing staff information | |

### Return type

[**List&lt;LoginResponse&gt;**](LoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

