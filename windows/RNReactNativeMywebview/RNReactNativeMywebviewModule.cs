using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace React.Native.Mywebview.RNReactNativeMywebview
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNReactNativeMywebviewModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNReactNativeMywebviewModule"/>.
        /// </summary>
        internal RNReactNativeMywebviewModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNReactNativeMywebview";
            }
        }
    }
}
