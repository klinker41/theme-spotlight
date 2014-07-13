# Klinker Apps Theme Spotlight #

This app is an update to our old Klinker Apps Portal. It allows users to view all EvolveSMS and Talon themes
on the Google Play Store, along with seeing screenshots and downloading those themes. There is also a "Featured Themers"
 section where themers can add their names and themes to the list of those available. Just submit a pull request with
 your added information and I'll try and get it merged as quick as I can!

Another really cool thing about it, is that it is fully set up and ready to go with jUnit testing using Robolectric
and Mockito. Android testing frameworks aren't good at all natively, but Robolectric allows tests to not be run on
the device which greatly speeds up the process. So, if you're looking for a good way to integrate unit testing into
your apps, take a look at the structure. Maybe don't look too closely at all the tests though, I'm still learning that.

### How do I get set up? ###

* Clone the project
* Edit it however you'd like
* Build and test it, you can build on the command line by executing ./gradlew build, which will also run through
all of your tests for you

### How can I become a featured themer? ###

Adding yourself as a featured themer is very easy to do. You'll need to go into the Themers class and create
yourself as a new, static FeaturedThemer object. Specify name, description, icon url, and your themes, whether
those be from the play store or just download links. Look at the constructors on the FeaturedThemers object for
more information. If you want to upload your own themes, not through the play store, then simply add those themes to
the Themes class.

There are examples for both of these cases already in the Themers and Themes classes.