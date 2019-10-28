# VARpedia
VARpedia creates video files of words you search on Wikipedia, with images from the Flicker API. You can then play your creations through an in-built media player. The application is coded with bash and JavaFX, so it will require the Linux/Unix operating system.   

### Libraries required for the audio functionality are in the repository under the directory lib.  Please use the ECSE computer lab Linux image for testing purposes.  

## Directions to easily execute the runnable .jar file provided in the repository
- **Step 1**: Navigate to the directory of the .jar file, and open a terminal there.  

- **Step 2**: Type into the terminal the following: ***java -jar VARpedia.jar***. If you are running this in the virtual box type in ***java --module-path /home/student/Downloads/openjfx-13-rc+2_linux-x64_bin-sdk/javafx-sdk-13/lib  --add-modules=javafx.media --add-modules=javafx.controls -jar VARpedia.jar***
- **Step 3**: Press enter. The application should now be running. Enjoy!  

## Directions on how to run the project through Eclipse
- **Step 1**: Download/Clone the repository and save in Documents  
- **Step 2**: On Eclipse, select File &rarr; Open project from file system &rarr; Navigate to "Wiki_Speak" directory &rarr; Make sure
that Wiki_Speak Folder is ticked &rarr; Finish  

- **Step 3**: Right click Wiki_Speak from Package Explorer &rarr; Build Paths &rarr; Add Libraries &rarr; User Library &rarr; User
libraries &rarr; New... &rarr; name it whatever you want &rarr; Add JARs &rarr; Browse to WikiSpeak/.resources/libs &rarr; Highlight all .jar
files &rarr; Apply and close &rarr; Make sure library is ticked &rarr; Finish  
- **Step 4**: Go to Main.java &rarr; Run. The application should now be running.  Enjoy!

## Attributions
### Note: All mp3 files have been trimmed to a length of 1 minute starting from the 30 second mark  
- **Classical.mp3:** Moonlight Sonata (Shifting Sun Mix) by Speck (c) copyright 2018 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/speck/57884 Ft: Snowflake  

- **Electronic.mp3:** yellow by cyba (c) copyright 2019 Licensed under a Creative Commons Attribution Noncommercial  (3.0) license. http://dig.ccmixter.org/files/cyba/60166   

- **Light.mp3:** Light by onlymeith (c) copyright 2018 Licensed under a Creative Commons Attribution Noncommercial  (3.0) license. http://dig.ccmixter.org/files/onlymeith/58693 Ft: airtone  

- **book.png:** Tkgd2007 (https://en.wikipedia.org/wiki/File:Question_book-new.svg), "Question book-new", https://creativecommons.org/licenses/by-sa/3.0/legalcode  

- **arrow.png:** Masur (https://commons.wikimedia.org/wiki/File:Red_Arrow_Right.svg), "Red Arrow Right", marked as public domain, more details on Wikimedia Commons: https://commons.wikimedia.org/wiki/Template:PD-shape  

- **thumb.png:** Image by <a href="https://pixabay.com/users/DarkAthena-5167878/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=4007573">DarkAthena</a> from <a href="https://pixabay.com/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=4007573">Pixabay</a>  

- **man.png:** Image by <a href="https://pixabay.com/users/mohamed_hassan-5229782/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=2758705">mohamed Hassan</a> from <a href="https://pixabay.com/?utm_source=link-attribution&amp;utm_medium=referral&amp;utm_campaign=image&amp;utm_content=2758705">Pixabay</a>

- **.titleFont.otf:** Read .titleFontreadme.txt in the main package for more details  

- **.myFont.ttf:** Read .myFontreadme.txt in main package for more details

- **badWords.txt:** Retrieved from https://www.freewebheaders.com/full-list-of-bad-words-banned-by-google/
