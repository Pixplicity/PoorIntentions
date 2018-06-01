# Poor Intentions

Illustrates invalid back stacks on Android and offers a resolution.

## The problem

This app consists of two screens: MainActivity and DetailActivity. The app can only exist in one of two states:

<img src="design/flow1.svg" height="120" title="Expected flow" />

Unfortunately, due to a peculiarity in Android, launching an app that is already running might result in it entering an invalid state:

<img src="design/flow2.svg" height="120" title="Unexpected flow" />

This issue can best be reproduced by launching an app from its Google Play details page, navigating to DetailActivity, pressing home, then launching it from the app drawer.

When the resolution is enabled, this app will pop up a toast displaying "Task resuming" when the issue is detected. The resolution is simply to finish the excess instance of MainActivity when it is started by a launch intent but is not the task's root activity.

## The resolution

In your launch activity, simply perform the following check:

```
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check that the activity was launched correctly
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action == Intent.ACTION_MAIN) {
            // This activity doesn't belong; just the launcher or whatever misbehaving
            Toast.makeText(this, "Task resuming", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }
```

## About

<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" height="100" />

[Made with ❤ by Pixplicity](https://pixplicity.com)

## License

```
   Copyright 2018 Pixplicity

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
