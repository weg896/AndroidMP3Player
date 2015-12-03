# AndroidMP3Player

----------------------------------------------------------------------

ThreadTesting Branch --
Try to create a new thread to hold a mediaplayer object,
basic on the test, it seem mediaplayer object has it own thread (or Asynchronize)
when mediaplayer is in start state (.start()).


----------------------------------------------------------------------

version 1.0.1:
trying to implement a Service,
basic on google guide, I should new a thread for handle the Service
to prevent ANR (Application Not Responding).

The guide suggest me to use IntentService rather than Service
for avoiding ANR.

next version may create another Activity for start a Service,
then the exist Activity bind to the Service

----------------------------------------------------------------------

version 1.0.0:
a media player only using one Activity and Media classes
next version will try to implement a Service so that music can play on background

main idea
1. Media object get the mp3 stream from internet and prepare for play
2. Button object setup event listener for control music play and pause
3. Handler object update the progress bar by implement Runnable object
