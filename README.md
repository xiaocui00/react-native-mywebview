
# react-native-mywebview

## Getting started

`$ npm install react-native-mywebview --save`

### Mostly automatic installation

`$ react-native link react-native-mywebview`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mywebview` and add `RNReactNativeMywebview.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNReactNativeMywebview.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNReactNativeMywebviewPackage;` to the imports at the top of the file
  - Add `new RNReactNativeMywebviewPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mywebview'
  	project(':react-native-mywebview').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mywebview/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mywebview')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNReactNativeMywebview.sln` in `node_modules/react-native-mywebview/windows/RNReactNativeMywebview.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using React.Native.Mywebview.RNReactNativeMywebview;` to the usings at the top of the file
  - Add `new RNReactNativeMywebviewPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNReactNativeMywebview from 'react-native-mywebview';

// TODO: What to do with the module?
RNReactNativeMywebview;
```
  