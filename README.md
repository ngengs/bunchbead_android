# bunchbead_android
Skripsi julita

# Build
- Clone / Download this repo
  ```
  git clone  https://github.com/ngengs/bunchbead_android.git
  ```
  or download from [here](https://github.com/ngengs/bunchbead_android/archive/master.zip) and extract
- Open the `configuration.zip` and enter the ``password`` (Julita have this password or contact me for the password)
- Make sure to have this file inside the project
  ```
  /app/keys/*.jks <-- This for signing the APK
  /app/properties/keystore.properties
  /app/google-services.json
  ```
- Build from your Android Studio or from command ``./gradlew assembleRelease``
