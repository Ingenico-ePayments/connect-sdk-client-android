GlobalCollect - Android SDK
=======================

The GlobalCollect Android SDK provides a convenient way to support a large number of payment methods inside your Android app.
It supports Gingerbread (Android version 2.3.3) and up out-of-the box.
The Android SDK comes with an example app that illustrates the use of the SDK and the services provided by GlobalCollect.

The documentation is available on [https://developer.globalcollect.com/documentation/sdk/mobile/android/](https://developer.globalcollect.com/documentation/sdk/mobile/android/).

Installation
------------

To install the Android SDK and the example app, first download the code from Git.

```
$ git clone https://github.com/globalcollect/globalcollect-android.git
```

Afterwards, you can open the project you just downloaded in Android Studio to execute the example app.

To use the Android SDK in your own app, you need to add the "globalcollect-sdk" gradle module to the build path of your project as follows:

1. Open your app in Android Studio,
2. Select "File", "New", "Import Module"
3. Select source directory by clicking on the "..." button.
4. Browse to the check out global sdk project, and select the folder "globalcollect-sdk" which contains all the sdk source code, click "Ok".
5. Click on "Finish" to add the "globalcollect-sdk" module to your project.
6. Wait untill Android Studio is done building/cleaning the project, and you see a module named "globalcollect" appear in your Android Studio projectbrowser.
7. The last step is now to tell your App to use the added module.
8. Select your app, and click "File", "Project Structure". In the "Modules" section select your app, and go to the tab "Dependencies".
9. Click on the "+" sign and select "Module dependency".
10.Select the "globalcollect-sdk" module and press "Ok".
11. You now have access to use all the Android SDK classes. 