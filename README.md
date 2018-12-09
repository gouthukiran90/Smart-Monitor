# Smart-Monitor
Application for Monitoring the activities of a scene in a video.

In this app we will select camera area in which we are interested to monitor.
Camera area can be any room or area in a house like:
BedRoom
CourtYard
Living room
Hall
BasketCourt
Balcony

We used Google cloud intelligence API for listing out the scene lables (so come under activities and entities) in a video.
https://cloud.google.com/video-intelligence/
Input video for intelligence API is hosted in Google cloud storage.
Custom GS url : gs://staging.recipeapp-a7075.appspot.com/videoplay_movmnt_stop.mp4

When a harmful activity like a person fell down on floor detected , App will send notification about activity and time frame of that activity.

Gave a settings prototype for demonstrating the camera settings like scheduling the camera monitoring service, notifiations frequency setup etc.

