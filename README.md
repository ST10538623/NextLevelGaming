# Next Level Gaming

A native Android starter app for the Next Level Gaming & Esports Arena. It is
built with Kotlin and Android XML layouts; it does not use a WebView.

## What is included

- Home, About Us, package catalogue, individual experiences and details for all
  seven offerings
- A multi-selection fee calculator with 0%, 5%, 10% and 15% discounts
- A validated contact enquiry form demonstration
- Dark, electric-blue and purple styling with scrollable screens and bottom
  navigation

Offering names, descriptions, inclusions and prices live in
`app/src/main/java/com/nextlevelgaming/arena/data/Offering.kt`. The fee rules
live separately in `app/src/main/java/com/nextlevelgaming/arena/calculator/FeeCalculator.kt`.

## Requirements

- Android Studio with its bundled JDK 17 or newer (JDK 21 is suitable)
- Android SDK Platform 36
- An Android emulator or a USB-connected Android device to run the app

This workspace targets Android SDK 36, uses Android Gradle Plugin 8.10.1 and
Gradle 8.11.1, and needs Java 17 to run the Android Gradle Plugin. Android
Studio's bundled JDK can be selected under **Settings > Build, Execution,
Deployment > Build Tools > Gradle > Gradle JDK** (on macOS, **Android Studio >
Settings**).

The Android SDK is commonly installed at `~/Library/Android/sdk`. If Android
Studio cannot find it, open **Tools > SDK Manager**, install **Android SDK
Platform 36** and **Android SDK Build-Tools**, then set `ANDROID_HOME` to that
SDK directory or set the SDK location in Android Studio.

## Open, build and run

1. Open this project folder in Android Studio and allow Gradle to sync. The
   first sync may download Gradle and the Android Gradle Plugin.
2. Create or select an emulator in **Tools > Device Manager**, or connect an
   Android device with USB debugging enabled.
3. Select the `app` run configuration and press **Run**.

From Terminal, use Android Studio's bundled JDK and your Android SDK, then run
the Gradle commands:

```sh
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$JAVA_HOME/bin:$ANDROID_HOME/platform-tools:$PATH"
./gradlew assembleDebug
./gradlew installDebug
```

If Android Studio is installed somewhere else, update `JAVA_HOME` to its
`Contents/jbr/Contents/Home` folder. In Android Studio, set the same JDK in
**Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK**.

To run the fee-calculation unit tests:

```sh
./gradlew testDebugUnitTest
```

The tests cover zero selections, one R750 offering, two/three/four R1500
offerings, the R1500 + R750 + R750 combination, and duplicate selections.

## Contact form note

The form validates a name, email address and message locally. There is no
backend, so it does **not** send or store enquiries. No phone number, email
address or street address has been invented; add verified business details and
a messaging backend before using this as a live contact channel.

## Beginner notes

- `MainActivity.kt` contains the simple screen and navigation flow. Reusable
  structure and offering cards are XML under `app/src/main/res/layout`.
- `Offering.kt` is the single catalogue used by the catalogues, detail pages
  and calculator.
- `FeeCalculator.kt` holds the discount calculation independently from the UI.
- The theme colours are in `app/src/main/res/values/colors.xml`.
