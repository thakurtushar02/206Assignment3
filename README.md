# Wiki_Speak App!
The Wiki_Speak application creates video files of words you search on Wikipedia, with images from the Flicker API. You can then play your creations through an in-built media player. The application is coded with bash and JavaFX, so it will require the Linux/Unix operating system.   

### Libraries required for the audio functionality are in the repository under the directory lib.  
### Please use the ECSE computer lab Linux image for testing purposes.  

## Here are the directions to easily execute the runnable .jar file provided in the repository.
Step 1: Navigate to the directory of the .jar file, and open a terminal there.  
Step 2: Type into the terminal the following: *java -jar WikiSpeak.jar*  
Step 3: Press enter. The application should now be running. Enjoy!  

String cMD = "cat *.jpg | ffmpeg -f image2pipe -framerate $((" + _numPics + "))/$(soxi -D temp.wav) -i - -c:v libx264 -pix_fmt yuv420p -vf \"scale=w=1280:h=720\" -r 25 -y visual.mp4 ; rm " + _term + "??.jpg ; ffmpeg -i visual.mp4 -vf \"drawtext=fontsize=50:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:borderw=5:text=\'" + _term + "\'\" out.mp4 ; ffmpeg -i out.mp4 -i temp.wav -c:v copy -c:a aac -strict experimental -y " + _name + ".mp4 &>/dev/null ; rm visual.mp4 ; rm out.mp4";
