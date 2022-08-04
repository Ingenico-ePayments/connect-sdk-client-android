## Ingenico Connect - Android SDK

The Ingenico Connect Android SDK provides a convenient way to support a large number of payment methods inside your Android app.
It supports Lollipop (Api version 21) and up out-of-the box.
The Android SDK comes with an [example application](https:/github.com/Ingenico-ePayments/connect-sdk-client-android-example-kotlin) that illustrates the use of the SDK and the services provided by Ingenico ePayments on the Ingenico ePayments platform.

See the [Ingenico Connect Developer Hub](https://epayments.developer-ingenico.com/documentation/sdk/mobile/android/) for more information on how to use the SDK.

# Installation via Gradle
Add an implementation to your build configuration by adding the dependency to your project build.gradle file where `x.y.z` is the version number:

    dependencies {
        implementation "com.ingenico.connect.gateway:connect-sdk-client-android:x.y.z'
    }

# Migration Guide
A migration guide was written to help you migrate from version 5 to version 6 of the SDK. This guide explains the new architecture of the SDK, as well as the breaking changes made for version 6.

You can find the migration guide [here](/documentation/Migration%20Guide%20v5%20to%20v6.md).