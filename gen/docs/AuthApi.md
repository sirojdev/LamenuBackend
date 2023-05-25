# AuthApi

All URIs are relative to *http://0.0.0.0:8181/api/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**staffAuthPost**](AuthApi.md#staffAuthPost) | **POST** /staff/auth | 


<a name="staffAuthPost"></a>
# **staffAuthPost**
> List&lt;LoginResponse&gt; staffAuthPost(staffDto)



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
    defaultClient.setBasePath("http://0.0.0.0:8181/api/v1");

    AuthApi apiInstance = new AuthApi(defaultClient);
    StaffDto staffDto = new StaffDto(); // StaffDto | This object must contain only deviceUuid:unique and phone
    try {
      List<LoginResponse> result = apiInstance.staffAuthPost(staffDto);
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

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **staffDto** | [**StaffDto**](StaffDto.md)| This object must contain only deviceUuid:unique and phone |

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
**200** | OK |  -  |

