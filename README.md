To make this work you might have to add the following lines to the JVM arguments at compilation :

-Dgstreamer.library.path=C:/PATH/TO/PROCESSING/processing-2.2.1-windows64/processing-2.2.1/modes/java/libraries/video/library/windows64
-Dgstreamer.plugin.path=C:/PATH/TO/PROCESSING/processing-2.2.1-windows64/processing-2.2.1/modes/java/libraries/video/library/windows64/plugins


Ou sur mac : 

-Dgstreamer.library.path=/Applications/Processing.app/Contents/Java/modes/java/libraries/video/library/macosx64
-Dgstreamer.plugin.path=/Applications/Processing.app/Contents/Java/modes/java/libraries/video/library/macosx64/plugins
