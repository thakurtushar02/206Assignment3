# Wiki_Speak App!
The Wiki_Speak application creates video files of words you search on Wikipedia, with images from the Flicker API. You can then play your creations through an in-built media player. The application is coded with bash and JavaFX, so it will require the Linux/Unix operating system.   

### Libraries required for the audio functionality are in the repository under the directory lib.  
### Please use the ECSE computer lab Linux image for testing purposes.  

## Here are the directions to easily execute the runnable .jar file provided in the repository.
Step 1: Navigate to the directory of the .jar file, and open a terminal there.  
Step 2: Type into the terminal the following: *java -jar WikiSpeak.jar*  
Step 3: Press enter. The application should now be running. Enjoy!  

## Here are the directions on how to run the project through Eclipse
Step 1: Download/Clone the repository and save in Documents  
Step 2: On Eclipse, select File -> Open project from file system -> Navigate to "Wiki_Speak" directory -> Make sure  
that Wiki_Speak Folder is ticked -> Finish  
Step 3: Right click Wiki_Speak from Package Explorer -> Build Paths -> Add Libraries -> User Library -> User  
libraries -> New... -> name it whatever you want -> Add JARs -> Browse to WikiSpeak/libs -> Highlight all .jar  
files -> Apply and close -> Make sure library is ticked -> Finish  
Step 4: Go to Main.java -> Run -> Run configurations... -> Arguments tab -> In VM Arguments text area, type:  
*--module-path /home/student/Downloads/openjfx-13-rc+2_linux-x64_bin-sdk/javafx-sdk-13/lib
 --add-modules=javafx.controls --add-modules=javafx.media*
 Apply -> Run. The application should now be running. Enjoy! (Next time you can just press Ctrl + F11).
