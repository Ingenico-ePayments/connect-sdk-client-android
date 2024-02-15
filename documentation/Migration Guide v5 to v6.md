# Migrating from version 5.x.x to version 6.0.0
This migration guide will help you migrating from version 5 to version 6. There are two sections.

The first section is about the new architecture for the SDK, where initialization and error handling of the SDK has been improved. These changes are *not* mandatory to move to version 6 of the SDK. The old architecture has been deprecated, but is still available and will not be removed until a following major version update.

The second section lists the breaking changes that were made as a result of removing/changing SDK elements that were already deprecated in version 5 of the SDK.

## 1. Migrating to the new Architecture

> *Note:* The changes described in this section are not breaking at this time. The old way of using the SDK, via the Session object, has been deprecated, but will be available until a future major version update.

Major improvements have been made to the SDKs error handling, and to the initialization of the SDK. This is an overview of all architecture changes:

- The SDK has received a new entry point: `ConnectSDK`. Its initialize method should be used to provide Session Configuration, obtained through the Server to Server API, and Payment Configuration.
- Session has been replaced by `ClientAPI`. Once the SDK has been initialized, an instance of ClientAPI can be obtained by calling `ConnectSDK.getClientAPI`.
- Error handling has been improved greatly when making requests through `ClientAPI`. When making a call, a (nameless) functional interface must be provided, that implements the following methods:
    - `onSuccess(<ExpectedResponseType> response)` - Called when the request was successful. The response parameter will contain the response object, according to the existing, unchanged domain model.
    - `onApiError(ApiErrorResponse apiError)` - Called when the Connect gateway returned an error response. The apiError parameter will contain further information on the error, such as the errorCode and a descriptive message.
    - `onFailure(Throwable failure)` - Called when an exception occurred while executing the request. The failure parameter will contain the throwable that indicates what went wrong.

### Using the new architecture

The steps below describe how to migrate to the new SDK architecture in version 6 of the SDK.

1. Upgrade to the latest Android Connect SDK version in your build.gradle:
```groovy
implementation 'com.ingenico.connect.gateway:connect-sdk-client-android:6.0.0'
```
2. Replace session initialization with ConnectSDK initialization:
```kotlin
// Kotlin example

val connectSDKConfiguration = ConnectSDKConfiguration.Builder(
        sessionConfiguration,
        context
    )
    .enableNetworkLogs(false)
    .preLoadImages(false)
    .applicationId("my application name")
    .build()

val paymentConfiguration = PaymentConfiguration.Builder(paymentContext)
    .groupPaymentProducts(false)
    .build()

ConnectSDK.initialize(connectSDKConfiguration, paymentConfiguration)
```
```java
// Java example

ConnectSDKConfiguration connectSDKConfiguration = new ConnectSDKConfiguration.Builder(
        sessionConfiguration,
        context
    )
    .enableNetworkLogs(false)
    .preLoadImages(false)
    .applicationId("my application name")
    .build()

PaymentConfiguration paymentConfiguration = new PaymentConfiguration.Builder(paymentContext)
    .groupPaymentProducts(false)
    .build()

ConnectSDK.INSTANCE.initialize(connectSDKConfiguration, paymentConfiguration)
```

ConnectSDKConfiguration  has the following extra methods that allow you to set additional SDK settings:

- `enableNetworkLogs` will make the SDK log all network requests and responses to the console. This setting can be used to investigate issues.
- `preloadImages` determines whether image resources, initially returned by the API as their location, will be retrieved by the SDK, or whether you will retrieve them on the go when required. The SDK loads the images by default, to make sure behaviour is as it used to be. We have added the option to disable preloading to allow you to use frameworks for image loading on demand.
- `applicationId` is the identifier or name that you choose for your app.

`PaymentConfiguration` has a configuration option for enabling, or disabling grouping. Enabling will cause credit cards to be bundled when performing a get payment items request.

3. Replace al api requests that were called through session with ConnectSDK ClientAPI calls:
```kotlin
// Kotlin example

ConnectSDK.getClientApi().getPaymentItems(
    { basicPaymentItems: BasicPaymentItems ->
        // use the basicPaymentItems object
    },
    { apiError: ApiErrorResponse ->
        // process error
    },
    { failure: Throwable ->
        // process failure
    }
)
```
```java
// Java example

ConnectSDK.INSTANCE.getClientApi().getPaymentItems(
    (basicPaymentItems -> {
            // use the basicPaymentItems object
        }  
    ),
    (apiErrorResponse -> {
            // process error
        }
    ),
    (failure -> {
            // process failure
        }
    )
);
```

4. Call the code that used to be in your call complete listeners to the corresponding onSuccess status callbacks. When onSuccess is called, the response object will always be populated. Null-checks are no longer required in this case.

5. Implement errorHandling for ApiErrorResponse and Failure status.

For more information about version 6 or the Android SDK in general, also review the Android SDK developer documentation and/or the Kotlin example app.

### Deprecations

This is the full list of classes that have become deprecated related to the architecture change.

- `Session`
- `Util`
- `C2SCommunicator`
- `C2SCommunicatorConfiguration`
- `TLSSocketFactory`
- `EncryptData`
- `Encryptor`
- `EncryptUtil`
- `AssetManager`
- `SessionEncryptionHelper`
- `BasicPaymentItemsAsyncTask`
- `BasicPaymentProductGroupsAsyncTask`
- `BasicPaymentProductsAsyncTask`
- `ConvertAmountAsyncTask`
- `EncryptDataAsyncTask`
- `IinLookupAsyncTask`
- `LoadImageAsyncTask`
- `PaymentProductAsyncTask`
- `PaymentProductDirectoryAsyncTask`
- `PaymentProductGroupAsyncTask`
- `PublicKeyAsyncTask`
- `ThirdPartyStatusAsyncTask`
- `BadPaymentItemException`
- `CommunicationException`
- `UnknownNetworkException`
- `CacheHandler`
- `Preferences`
- `ReadInternalStorage`
- `WriteInternalStorage`


## 2. Breaking Changes in version 6.0.0

Code that had been marked deprecated in the past has been removed. This section contains everything that was deleted, and what method, class or mechanism to use instead.

### Region & Environment

Classes `Region` & `Environment` and all methods that required these types as a parameter, or returned these types, have been removed. These classes were used to determine the base URLs (asset and client URL) for the server that the Android SDK would connect to for the current session. Use the URLs that are returned in the Server-to-Server Create Client Session API instead.

The following classes and methods were removed:

- Class `Region`
- Class `Environment`
- Methods in `Util`
    - `getCS2BaseUrlByRegion(Region region, Environment.EnvironmentType environment)`
    - `getAssetsBaseUrlByRegion(Region region, Environment.EnvironmentType environment)`

The following methods and constructors had their arguments or return types changed:

- Constructor in `PaymentContext`:
    - PaymentContext(AmountOfMoney amountOfMoney, CountryCode countryCode, boolean isRecurring)
- Methods in `AssetManager`:
    - `updateLogos(Region region, EnvironmentType environment, List<BasicPaymentItem> basicPaymentItems, Size size)`
    - `getImageFromUrl(Region region, EnvironmentType environment, Map<String, String> logoMapping, BasicPaymentItem product, Size size)`
- Constructors in `C2sCommunicator`:
    - `C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment)`
    - `C2sCommunicatorConfiguration(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress)`
- Methods in `C2SCommunicator`:
    - `initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment)`
    - `initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier)`
    - `initWithClientSessionId(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress)`
    - `initSession(String clientSessionId, String customerId, Region region, EnvironmentType environment, String appIdentifier, String ipAddress)`


### CountryCode & CurrencyCode

Enumerations `CountryCode` & `CurrencyCode` have been removed. Everywhere these types were used as parameter or return type have been replaced by methods using type String for these values. The allowed country codes are defined by the ISO-3166-1 alpha-2 standard. For currency codes the the ISO-4217 standard is used.

Getters that were temporarily added to return `String` values of `CountryCode` and `CurrencyCode` (postfixed with 'String') have been deprecated. Instead, the regular getters can now be used to retrieve `String` values.

The following classes and methods were removed:

- Enum `CurrencyCode`
- Enum `CountryCode`

The following methods and constructors had their arguments or return types changed:

- Constructors in `AmountOfMoney`:
    - `AmountOfMoney(Long amount, CurrencyCode currencyCode)`
- Methods in `Session`:
    - `getDirectoryForPaymentProductId(String productId, CurrencyCode currencyCode, CountryCode countryCode, Context context, OnPaymentProductDirectoryCallCompleteListener listener)`
- Methods in `C2sCommunicator`:
    - `getPaymentProductDirectory(String productId, CurrencyCode currencyCode, CountryCode countryCode, Context context)`
- Constructor in `PaymentProductDirectoryAsyncTask`:
    - `PaymentProductDirectoryAsyncTask(String productId, CurrencyCode currencyCode, CountryCode countryCode, Context context, C2sCommunicator communicator, OnPaymentProductDirectoryCallCompleteListener listener)`
- Constructor in `PaymentItemCacheKey`:
    - `PaymentItemCacheKey(Long amount, CountryCode countryCode, CurrencyCode currencyCode, boolean isRecurring, String paymentProductId)`

The following getters have been marked deprecated, and should no longer be used:

- `AmountOfMoney`:
    - `getCurrencyCodeString()`
- `IinDetailsResponse`:
    - `getCountryCodeString()`
- `PaymentContext`:
    - `getCountryCodeString()`
- `PaymentItemCacheKey`:
    - `getCountryCodeString()`
    - `getCurrencyCodeString()`


### ValidationRule

The `validate(String text)` method was removed from the `ValidationRule` interface. Instead `validate(PaymentRequest paymentRequest, String fieldId)` should be used. All validation rules that implemented this interface have been updated.

In the following Validation rules, the `validate(String text)` method was removed:

- `ValidationRuleBoletoBancarioRequiredness`
- `ValidationRuleEmailAddress`
- `ValidationRuleExpirationDate`
- `ValidationRuleFixedList`
- `ValidationRuleIBAN`
- `ValidationRuleLength`
- `ValidationRuleLuhn`
- `ValidationRuleRange`
- `ValidationRuleRegex`
- `ValidationRuleResidentIdNumber`
- `ValidationRuleTermsAndConditions`

In class PaymentProductField the method `validateValue(String value)` was removed. Use `validateValue(PaymentRequest paymentRequest)` instead.

### Miscelaneous

The following methods have also been removed:

- `getBase64EncodedMetadata(Context context)` was removed from class `Util`. Please refrain from using the 'Util' class. It will be internalized to the SDK in a future major release.
- `getDisplayName()` was removed from class `ValueMap` . Use `getDisplayElements()` instead, which will return a map that contains a property displayName.
- The following constructor was removed from `SessionEncryptionHelper`: `SessionEncryptionHelper(Context context, PaymentRequest paymentrequest, String clientSessionId, OnPaymentRequestPreparedListener listener)`. Please refrain from using `SessionEncryptionHelper`. Instead, use `ConnectSDK.encryptPaymentRequest(paymentRequest, onSuccess, onFailure)`.

## Relevant links
- [Android Connect SDK on GitHub](https://github.com/Ingenico-ePayments/connect-sdk-client-android)
- [Kotlin example app on GitHub](https://github.com/Ingenico-ePayments/connect-sdk-client-android-example-kotlin)
- [Java example app on GitHub](https://github.com/Ingenico-ePayments/connect-sdk-client-android-example-java)
- [Android SDK documentation](https://epayments.developer-ingenico.com/documentation/sdk/mobile/android/)
- [Client API Reference](https://epayments-api.developer-ingenico.com/c2sapi/v1/en_US/index.html?paymentPlatform=ALL)


