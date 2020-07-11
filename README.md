# emoji-utility
Create in-app emoji's that can be added to your Android TextViews. These are custom ones that you create specifically for use in your application. They can easily be embedded in any TextView.

The utility stores the drawable objects that act as the emojis. You register one or more drawables with the utility. Then all you need to do is supply your text, which will contain key tags placed where you want the emoji(s) to display, along with your TextView, and the utility will render everything for you. It uses a Pattern Matcher to find where the emojis go, and Android Image Spans to make the substitutions.
