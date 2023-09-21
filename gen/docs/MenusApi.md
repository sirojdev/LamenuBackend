# MenusApi

All URIs are relative to *http://127.0.0.1:9000/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**clientMenusGet**](MenusApi.md#clientMenusGet) | **GET** /client/menus | 


<a name="clientMenusGet"></a>
# **clientMenusGet**
> List&lt;MenuDto&gt; clientMenusGet()



Returns a list of all menus

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.auth.*;
import org.openapitools.client.models.*;
import org.openapitools.client.api.MenusApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://127.0.0.1:9000/v1");
    
    // Configure HTTP bearer authorization: bearerAuth
    HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
    bearerAuth.setBearerToken("BEARER TOKEN");

    MenusApi apiInstance = new MenusApi(defaultClient);
    try {
      List<MenuDto> result = apiInstance.clientMenusGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling MenusApi#clientMenusGet");
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

[**List&lt;MenuDto&gt;**](MenuDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |
**204** | No Content |  -  |

