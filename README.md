# Klinker Apps Theme Spotlight #

This app is an update to our old Klinker Apps Portal. It allows users to view all EvolveSMS and Talon themes
on the Google Play Store, along with seeing screenshots and downloading those themes. There is also a "Featured Themers"
 section where themers can add their names and themes to the list of those available. Just submit a pull request with
 your added information and I'll try and get it merged as quick as I can!

Another really cool thing about it, is that it is fully set up and ready to go with jUnit testing using Robolectric
and Mockito. Android testing frameworks aren't good at all natively, but Robolectric allows tests to not be run on
the device which greatly speeds up the process. So, if you're looking for a good way to integrate unit testing into
your apps, take a look at the structure. Maybe don't look too closely at all the tests though, I'm still learning that.

You can also find this app on the [Play Store](https://play.google.com/store/apps/details?id=com.klinker.android.theme_spotlight).

### How do I get set up? ###

* Clone the project
* Edit it however you'd like
* Build and test it, you can build on the command line by executing

```
./gradlew build
```

which will also run through all of your tests for you using Robolectric.

### How can I become a featured themer? ###

Adding yourself as a featured themer is very easy to do. First, fork the project here on GitHub. You'll need to go into
the Themers class and create yourself as a new, static FeaturedThemer object. Specify name, description, icon url, and
your themes, whether those be from the play store or just download links. Look at the constructors on the
FeaturedThemers object for more information. If you want to upload your own themes, not through the play store, then
simply add those themes to the Themes class.

There are examples for both of these cases already in the Themers and Themes classes.

After you've edited those objects, commit your changes to your own fork, then you can use GitHub to send me a pull
request which I will accept soon after assuming everything is good to go.

Have fun!

## License

    Copyright (C) 2014 Klinker Apps, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.