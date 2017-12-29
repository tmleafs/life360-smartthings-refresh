# life360-smartthings-refresh-distance-sleep

If you installed Life360 though the IDE just update your DH and App with this code and your have:
1) refresh button,
2) distance from your chosen place (can be used in webCore),
3) sleep / awake status (can be used in webCore) 
4) last location update date/time

If you installed though the smartthings marketplace its best to remove all Life360 devices and the app and then install this DH and app though the IDE, enable OAuth on the app, publish to me and install the app from the "My Apps" in the marketplace.

Your end up with this for each life360 device (Please note doesnt matter which device you use to refresh it will refresh all life360 devices)
<p align="center">
  <img src="https://raw.githubusercontent.com/tmleafs/life360-smartthings-refresh/master/images/preview.png" width="350"/>
</p>

Using CoRE I was able too use a Presence Sensor as Redundancy to bring the Life360 device back in sync
Other ideas are run a refresh at set times like 15 mins after you go to work every day or every 30 mins etc
  
The piston runs after the presence sensor has left and the life360 device hasnt left after 10mins, it refreshes if it didnt work it sends a notifcation
<p align="center">
  <img src="https://raw.githubusercontent.com/tmleafs/life360-smartthings-refresh/master/images/core.jpg" width="350"/>
</p>

As you can see from the two screenshots after 10mins the piston ran and the life360 device updated

<p align="center">
  <img src="https://raw.githubusercontent.com/tmleafs/life360-smartthings-refresh/master/images/sensor.png" width="350"/>
  <img src="https://raw.githubusercontent.com/tmleafs/life360-smartthings-refresh/master/images/mobile.png" width="350"/>
</p>

