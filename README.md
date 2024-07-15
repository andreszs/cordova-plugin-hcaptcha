![npm](https://img.shields.io/npm/dt/cordova-plugin-hcaptcha) ![npm](https://img.shields.io/npm/v/cordova-plugin-hcaptcha) ![GitHub package.json version](https://img.shields.io/github/package-json/v/andreszs/cordova-plugin-hcaptcha?color=FF6D00&label=master&logo=github) ![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/andreszs/cordova-plugin-hcaptcha) ![GitHub top language](https://img.shields.io/github/languages/top/andreszs/cordova-plugin-hcaptcha) ![GitHub](https://img.shields.io/github/license/andreszs/cordova-plugin-hcaptcha) ![GitHub last commit](https://img.shields.io/github/last-commit/andreszs/cordova-plugin-hcaptcha)

# cordova-plugin-hcaptcha

Cordova plugin for integrating the [hCaptcha](https://github.com/hCaptcha/hcaptcha-android-sdk) challenge [Android SDK](https://github.com/hCaptcha/hcaptcha-android-sdk) and [web component API](https://docs.hcaptcha.com/invisible).

# Platforms

- Android
- Browser

# Installation

### Plugin Versions

| Plugin version | Cordova | Cordova Android | hCaptcha |
| --- | --- | --- | --- |
| 1.0.0 | >= 10.0.0 | >= 10.0.0 | 4.0.0 |

### Install latest version from NPM

```bash
  cordova plugin add cordova-plugin-hcaptcha
```

### Install latest with custom [SDK](https://github.com/hCaptcha/hcaptcha-android-sdk/releases) version

```bash
  cordova plugin add cordova-plugin-hcaptcha --variable HCAPTCHA_VERSION=4.0.0
```

### Install latest version from master

```bash
  cordova plugin add https://github.com/andreszs/cordova-plugin-hcaptcha
```

# Methods

## verify

To invoke the hCaptcha API, you call the `verify()` method. Usually, this method corresponds to the user's selecting a UI element, such as a button, in your activity.

```javascript
cordova.plugins.Hcaptcha.verify(onSuccess, onFailure, [args])
```

#### args parameters object

The following list contains configuration properties to allow customization of the hCaptcha verification flow.

| parameter | type | default | description |
| --- | --- | --- | --- |
| siteKey | String | | A site key from the hCaptcha [dashboard](https://dashboard.hcaptcha.com/sites), valid for any platform. |
| locale | String | AUTO | An ISO 639-1 [language code](https://docs.hcaptcha.com/languages) to enforce a specific language. |
| loading | Boolean | true | Show or hide the loading dialog. Android only. |
| tokenExpiration | long | 120 | hCaptcha token expiration timeout in seconds. |

### onSuccess callback return values

- **hCaptcha response token**

When the hCaptcha API executes the `onSuccess()` method, the user has successfully completed the CAPTCHA challenge. However, this method only indicates that the user has solved the CAPTCHA correctly. You still need to validate the user's response token from your backend server.

To learn how to validate the user's response token, see [Verify the User Response Server Side](https://docs.hcaptcha.com/#verify-the-user-response-server-side).

### onFailure callback return values

- When challenge is dismissed: "hCaptcha failed: Challenge Closed(30)"
- When challenge expires: "hCaptcha failed: Session Timeout(15)"
- When error occurs: The relevant error description is returned.

### Example

 ```javascript
var onSuccess = function (strToken) {
    console.log(strToken);
};
var onFailure = function (strError) {
    console.warn(strError);
};
var args = {
    siteKey: 'YOUR_SITE_KEY',
    locale: 'es',
    loading: true,
    tokenExpiration: 120
};
cordova.plugins.Hcaptcha.verify(onSuccess, onFailure, args);
```

# 
# Remarks

- You must verify the generated tokens from the server side to complete the verification correctly.
- The hCaptcha params `size` and `theme` are not configurable since they are valid for the checkbox only.
- The plugin will not render the checkbox and instead jump directly to the CAPTCHA challenge.
- The plugin does not work on Android 4.4 because the hCaptcha SDK has internal dependencies not available in API 19.
- On cordova-android 9.X.X and earlier the plugin requires [cordova-plugin-android-fragmentactivity](https://www.npmjs.com/package/cordova-plugin-android-fragmentactivity).

# Plugin demo app

- [Compiled APK](https://github.com/andreszs/cordova-plugin-demos/com.andreszs.hcaptcha.demo/apk)
- [Source code for www folder](https://github.com/andreszs/cordova-plugin-demos)

<img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.hcaptcha.demo/screenshots/android/hcaptcha-001.png?raw=true" width="200" /> <img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.hcaptcha.demo/screenshots/android/hcaptcha-002.png?raw=true" width="200" /> <img src="https://github.com/andreszs/cordova-plugin-demos/blob/main/com.andreszs.hcaptcha.demo/screenshots/android/hcaptcha-003.png?raw=true" width="200" />

# Changelog

### 1.0.0

- First version
- Tested on Android 5.1 and up
- Tested on browser platform
