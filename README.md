Ingenico Connect - Android SDK
=======================

The Ingenico Connect Android SDK provides a convenient way to support a large number of payment methods inside your Android app.
It supports Gingerbread (Android version 2.3.3) and up out-of-the box.
The Android SDK comes with an example app that illustrates the use of the SDK and the services provided by Ingenico ePayments on the GlobalCollect platform.

The documentation is available on [https://developer.globalcollect.com/documentation/sdk/android/](https://developer.globalcollect.com/documentation/sdk/android/).

Installation
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
