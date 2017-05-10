Ingenico Connect - Android SDK
=======================

The Ingenico Connect Android SDK provides a convenient way to support a large number of payment methods inside your Android app.
It supports Gingerbread (Android version 2.3.3) and up out-of-the box.
The Android SDK comes with an example app that illustrates the use of the SDK and the services provided by Ingenico ePayments on the GlobalCollect platform.

See the [Ingenico Connect Developer Hub](https://developer.globalcollect.com/documentation/sdk/mobile/android/) for more information on how to use the SDK.

Installation via Gradle
------------

Add a requirement to the SDK to your `build.gradle` file, where `x.y.z` is the version number:

    dependencies {
        // other dependencies
        compile 'com.ingenico.connect.gateway:connect-sdk-client-android:x.y.z'
    }

Manual installation
------------

To install the Android SDK and the example app, first download the code from GitHub.

```
$ git clone https://github.com/Ingenico-ePayments/connect-sdk-client-android.git
```

Afterwards, you can open the project you just downloaded in Android Studio to execute the example app.

To use the Android SDK in your own app, you need to add the `globalcollect-sdk` gradle module to the build path of your project as follows:

1. Open your app in Android Studio.
2. Select `File`, `New`, `Import Module`.
3. Select source directory by clicking on the `...` button.
4. Browse to the downloaded Android SDK project, select the folder `globalcollect-sdk` which contains all the SDK source code, and click `OK`.
5. Click on `Finish` to add the `globalcollect-sdk` module to your project.
6. Wait untill Android Studio is done building/cleaning the project, and you see a module named `globalcollect` appear in your Android Studio project browser.
7. The last step is now to tell your App to use the added module.
8. Select your app, and click `File`, `Project Structure`. In the `Modules` section select your app, and go to the tab `Dependencies`.
9. Click on the `+` sign and select `Module dependency`.
10.Select the `globalcollect-sdk` module and press `OK`.
11. You now have access to use all the Android SDK classes.

Running tests
-------------

The Android SDK comes with a set of integration tests. To run these tests, you will first need to modify file `globalcollect-sdk-integrationtest/src/test/resources/itconfiguration.properties`. This file is mostly complete, but you need to fill in the actual values for the following keys:
* `connect.api.apiKeyId` for the API key id to use. This can be retrieved from the Configuration Center.
* `connect.api.secretApiKey` for the secret API key to use. This can be retrieved from the Configuration Center.
* `connect.api.merchantId` for your merchant ID.

Besides these settings, you can also modify the settings for the [Java SDK](https://developer.globalcollect.com/documentation/sdk/server/java/), which is used to create the sessions and tokens needed to run the tests.

Afterwards, you can run the tests as follows:
1. Open the SDK in Android Studio. If you haven't opened it before, choose to import a project and browse to the Android SDK project.
2. On the `Project` tab, open `globalcollect-sdk-integrationtest`, then `java`. Right click on the `com.globalcollect.gateway.sdk.client.android.integrationtest` package, and click on `Run 'Tests in 'com.globalcollect.ga...'`.
