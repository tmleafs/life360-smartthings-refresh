/**
 *  Copyright 2015 SmartThings
 *
 *	BTRIAL DISTANCE AND SLEEP PATCH 29-12-2017
 *	Updated Code to handle distance from, and sleep functionality
 *
 *	TMLEAFS REFRESH PATCH 06-12-2016 V1.1
 *	Updated Code to match Smartthings updates 12-05-2017 V1.2
 *	Added Null Return on refresh to fix WebCoRE error 12-05-2017 V1.2
 *	Added updateMember function that pulls all usefull information Life360 provides for webCoRE use V2.0
 *	Changed attribute types added Battery & Power Source capability 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Life360-User
 *
 *  Author: jeff
 *  Date: 2013-08-15
 */
 
preferences {
	input title:"Distance", description:"This feature allows you change the display of distance to either Miles or KM. Please note, any changes will take effect only on the NEXT update or forced refresh.", type:"paragraph", element:"paragraph"
	input name: "units", type: "enum", title: "Distance Units", description: "Miles or Kilometers", required: false, options:["Kilometers","Miles"]
} 
 
metadata {
definition (name: "Life360 User", namespace: "tmleafs", author: "tmleafs", mnmn:"SmartThings", vid:"generic-arrival") {
	capability "Presence Sensor"
	capability "Sensor"
    capability "Refresh"
	capability "Sleep Sensor"
    capability "Battery"
    capability "Power Source"
    
	attribute "distanceMetric", "Number"
   	attribute "distanceKm", "Number"
	attribute "distanceMiles", "Number"
    attribute "prevAddress1", "String"
    attribute "prevAddress2", "String"
	attribute "address1", "String"
  	attribute "address2", "String"
  	attribute "battery", "number"
   	attribute "charge", "boolean"
   	attribute "lastCheckin", "number"
   	attribute "inTransit", "boolean"
   	attribute "isDriving", "boolean"
   	attribute "latitude", "number"
   	attribute "longitude", "number"
   	attribute "since", "number"
   	attribute "speedMetric", "number"
    attribute "speedMiles", "number"
    attribute "speedKm", "number"
   	attribute "wifiState", "boolean"

	command "refresh"
	command "asleep"
    command "awake"
    command "toggleSleeping"
    command "setBattery",["number","boolean"]
    
	
	}

	simulator {
		status "present": "presence: 1"
		status "not present": "presence: 0"
	}

	tiles {
		multiAttributeTile(name: "display", type: "generic", width: 2, height: 2, canChangeBackground: true) {
			tileAttribute ("device.display", key: "PRIMARY_CONTROL") {
            			attributeState "present, not sleeping", label: 'Home', icon:"st.nest.nest-away", backgroundColor:"#c0ceb9"
				attributeState "present, sleeping", label: 'Home (asleep)', icon:"st.Bedroom.bedroom2", backgroundColor:"#6879a3"
				attributeState "not present", label: 'Away', icon:"st.Office.office5", backgroundColor:"#777777"
            		}
       			tileAttribute ("device.status", key: "SECONDARY_CONTROL") {
				attributeState "default", label:'${currentValue}'
			}
        	}
		
		standardTile("presence", "device.presence", width: 4, height: 2, canChangeBackground: true) {
			state("present", labelIcon:"st.presence.tile.mobile-present", backgroundColor:"#00A0DC")
			state("not present", labelIcon:"st.presence.tile.mobile-not-present", backgroundColor:"#ffffff")
		}
		
		standardTile("sleeping", "device.sleeping", width: 2, height: 2, canChangeBackground: true) {
			state("sleeping", label:"Asleep", icon: "st.Bedroom.bedroom2", action: "awake", backgroundColor:"#00A0DC")
			state("not sleeping", label:"Awake", icon: "st.Health & Wellness.health12", action: "asleep", backgroundColor:"#ffffff")
		}
       
		valueTile("lastLocationUpdate", "device.lastLocationUpdate", width: 4, height: 1) {
			state("default", label: '${currentValue}')
		}
        
        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		standardTile("blank", "blank", height: 1, width: 1, decoration: "flat") {
            state("default", label:'')
        }
		
        standardTile("information", "information", height: 1, width: 4, decoration: "flat") {
            state("default", label:'Below is information that can be used in webCoRE')
        }
        
        standardTile("blank", "blank", height: 1, width: 1, decoration: "flat") {
            state("default", label:'')
        }
        
        standardTile("address1", "device.address1", height: 1, width: 3, decoration: "flat") {
            state("default", label:'Address 1: ${currentValue}')
        }
        
        standardTile("address2", "device.address2", height: 1, width: 3, decoration: "flat") {
            state("default", label:'Address 2: ${currentValue}')
        }
        
        standardTile("battery", "device.battery", height: 1, width: 2, decoration: "flat") {
            state("default", label:'Battery: ${currentValue}%')
        }
        
        valueTile("charging", "device.powerSource", height: 1, width: 2, decoration: "flat"){
        	state "battery", label: 'Charging: No'
            state "dc", label: 'Charging: Yes'
        }
        
         standardTile("wifistate", "device.wifiState", height: 1, width: 2, decoration: "flat") {
            state "false", label: 'Wifi: Off'
            state "true", label: 'Wifi: On'
        }
        
        standardTile("lastcheckin", "device.lastCheckin", height: 1, width: 3, decoration: "flat") {
            state("default", label:'Last Server Checkin: ${currentValue}')
        }
        
        standardTile("since", "device.since", height: 1, width: 3, decoration: "flat") {
            state("default", label:'In Location Since: ${currentValue}')
        }
        
        standardTile("moving", "device.inTransit", height: 1, width: 3, decoration: "flat") {
            state "false", label: 'Moving: No'
            state "true", label: 'Moving: Yes'
        }
        
        standardTile("driving", "device.isDriving", height: 1, width: 3, decoration: "flat") {
            state "false", label: 'Driving: No'
            state "true", label: 'Driving: Yes'
        }
        
        standardTile("speedMetric", "device.speedMetric", height: 1, width: 2, decoration: "flat") {
            state("default", label:'MPS: ${currentValue}')
        }
        
        standardTile("speedMPH", "device.speedMiles", height: 1, width: 2, decoration: "flat") {
            state("default", label:'MPH: ${currentValue}')
        }
        
        standardTile("speedKPH", "device.speedKm", height: 1, width: 2, decoration: "flat") {
            state("default", label:'KPH: ${currentValue}')
        }
        
        standardTile("latitude", "device.latitude", height: 1, width: 3, decoration: "flat") {
            state("default", label:'Latitude: ${currentValue}')
        }
        
        standardTile("longitude", "device.longitude", height: 1, width: 3, decoration: "flat") {
            state("default", label:'Longitude: ${currentValue}')
        }

		main "presence"
		details(["display", "presence", "sleeping", "lastLocationUpdate", "refresh", "blank","information", "blank", "address1", "address2", "battery", "charging", "wifistate", "lastcheckin", "since", "moving", "driving", "speedMetric", "speedMPH", "speedKPH","latitude", "longitude", "testing"])
	}
}

def generatePresenceEvent(boolean present, homeDistance) {
	log.info "Life360 generatePresenceEvent (present = $present, homeDistance = $homeDistance)"
	def presence = formatValue(present)
	def linkText = getLinkText(device)
	def descriptionText = formatDescriptionText(linkText, present)
	def handlerName = getState(present)

	def sleeping = (presence == 'not present') ? 'not sleeping' : device.currentValue('sleeping')
	
	if (sleeping != device.currentValue('sleeping')) {
    	sendEvent( name: "sleeping", value: sleeping, isStateChange: true, displayed: true, descriptionText: sleeping == 'sleeping' ? 'Sleeping' : 'Awake' )
    }
	
    def display = presence + (presence == 'present' ? ', ' + sleeping : '')
	if (display != device.currentValue('display')) {
    	sendEvent( name: "display", value: display, isStateChange: true, displayed: false )
    }
	
	def results = [
		name: "presence",
		value: presence,
		unit: null,
		linkText: linkText,
		descriptionText: descriptionText,
		handlerName: handlerName,
	]
	log.debug "Generating Event: ${results}"
	sendEvent (results)
	
    if(units == "Kilometers" || units == null || units == ""){
	def statusDistance = homeDistance / 1000
	def status = sprintf("%.2f", statusDistance.toDouble().round(2)) + " km from: Home"
    if(status != device.currentValue('status')){
    sendEvent( name: "status", value: status, isStateChange: true, displayed: false )
    state.update = true}
    }else{
	def statusDistance = (homeDistance / 1000) / 1.609344 
   	def status = sprintf("%.2f", statusDistance.toDouble().round(2)) + " Miles from: Home"
    if(status != device.currentValue('status')){
   	sendEvent( name: "status", value: status, isStateChange: true, displayed: false )
    state.update = true}
	}
	
    def km = sprintf("%.2f", homeDistance / 1000)
    if(km.toDouble().round(2) != device.currentValue('distanceKm')){
    sendEvent( name: "distanceKm", value: km.toDouble().round(2), isStateChange: true, displayed: false )
    state.update = true}
    
    def miles = sprintf("%.2f", (homeDistance / 1000) / 1.609344)
	if(miles.toDouble().round(2) != device.currentValue('distanceMiles')){    
    sendEvent( name: "distanceMiles", value: miles.toDouble().round(2), isStateChange: true, displayed: false )
	state.update = true}
    
    if(homeDistance.toDouble().round(2) != device.currentValue('distanceMetric')){
	sendEvent( name: "distanceMetric", value: homeDistance.toDouble().round(2), isStateChange: true, displayed: false )
	state.update = true}
    
    if(state.update == true){
	sendEvent( name: "lastLocationUpdate", value: "Last location update on:\r\n${formatLocalTime("MM/dd/yyyy @ h:mm:ss a")}", displayed: false ) 
	state.update = false}
}

private extraInfo(address1,address2,battery,charge,endTimestamp,inTransit,isDriving,latitude,longitude,since,speedMetric,speedMiles,speedKm,wifiState){
	//log.debug "extrainfo = Address 1 = $address1 | Address 2 = $address2 | Battery = $battery | Charging = $charge | Last Checkin = $endTimestamp | Moving = $inTransit | Driving = $isDriving | Latitude = $latitude | Longitude = $longitude | Since = $since | Speedmeters = $speedMetric | SpeedMPH = $speedMiles | SpeedKPH = $speedKm | Wifi = $wifiState"
	   
	if(address1 != device.currentValue('address1')){
    sendEvent( name: "prevAddress1", value: device.currentValue('address1'), isStateChange: true, displayed: false )
    sendEvent( name: "address1", value: address1, isStateChange: true, displayed: false )
	}
    if(address2 != device.currentValue('address2')){
    sendEvent( name: "prevAddress2", value: device.currentValue('address2'), isStateChange: true, displayed: false )
    sendEvent( name: "address2", value: address2, isStateChange: true, displayed: false )   
	}
	if(battery != device.currentValue('battery'))
   	sendEvent( name: "battery", value: battery, isStateChange: true, displayed: false )
    if(charge != device.currentValue('charge'))
   	sendEvent( name: "charge", value: charge, isStateChange: true, displayed: false )
    
    def curcheckin = device.currentValue('lastCheckin').toString()
    if(endTimestamp != curcheckin)
   	sendEvent( name: "lastCheckin", value: endTimestamp, isStateChange: true, displayed: false )
    if(inTransit != device.currentValue('inTransit'))
   	sendEvent( name: "inTransit", value: inTransit, isStateChange: true, displayed: false )
    
	def curDriving = device.currentValue('isDriving')
    //log.debug "Current Driving Status = $curDriving - New Driving Status = $isDriving"
    if(isDriving != device.currentValue('isDriving')){
	//log.debug "If was different, isDriving = $isDriving"
   	sendEvent( name: "isDriving", value: isDriving, isStateChange: true, displayed: false )
    }
    def curlat = device.currentValue('latitude').toString()
    def curlong = device.currentValue('longitude').toString()
    latitude = latitude.toString()
    longitude = longitude.toString()
    if(latitude != curlat)
    sendEvent( name: "latitude", value: latitude, isStateChange: true, displayed: false )
    if(longitude != curlong)
   	sendEvent( name: "longitude", value: longitude, isStateChange: true, displayed: false )
    if(since != device.currentValue('since'))
   	sendEvent( name: "since", value: since, isStateChange: true, displayed: false )
    if(speedMetric != device.currentValue('speedMetric'))
	sendEvent( name: "speedMetric", value: speedMetric, isStateChange: true, displayed: false )
    if(speedMiles != device.currentValue('speedMiles'))
	sendEvent( name: "speedMiles", value: speedMiles, isStateChange: true, displayed: false )
    if(speedKm != device.currentValue('speedKm'))
	sendEvent( name: "speedKm", value: speedKm, isStateChange: true, displayed: false )
    if(wifiState != device.currentValue('wifiState'))
   	sendEvent( name: "wifiState", value: wifiState, isStateChange: true, displayed: false )
   	setBattery(battery.toInteger(), charge.toBoolean(), charge.toString())
}

def setMemberId (String memberId) {
   log.debug "MemberId = ${memberId}"
   state.life360MemberId = memberId
}

def getMemberId () {

	log.debug "MemberId = ${state.life360MemberId}"
    
    return(state.life360MemberId)
}

private String formatValue(boolean present) {
	if (present)
    	return "present"
	else
    	return "not present"
}

private formatDescriptionText(String linkText, boolean present) {
	if (present)
		return "Life360 User $linkText has arrived"
	else
    	return "Life360 User $linkText has left"
}

private getState(boolean present) {
	if (present)
		return "arrived"
	else
    	return "left"
}

private toggleSleeping(sleeping = null) {
	sleeping = sleeping ?: (device.currentValue('sleeping') == 'not sleeping' ? 'sleeping' : 'not sleeping')
	def presence = device.currentValue('presence');
	
	if (presence != 'not present') {
		if (sleeping != device.currentValue('sleeping')) {
			sendEvent( name: "sleeping", value: sleeping, isStateChange: true, displayed: true, descriptionText: sleeping == 'sleeping' ? 'Sleeping' : 'Awake' )
		}
		
		def display = presence + (presence == 'present' ? ', ' + sleeping : '')
		if (display != device.currentValue('display')) {
			sendEvent( name: "display", value: display, isStateChange: true, displayed: false )
		}
	}
}

def asleep() {
	toggleSleeping('sleeping')
}

def awake() {
	toggleSleeping('not sleeping')
}

def refresh() {
	parent.refresh()
    return null
}

def setBattery(int percent, boolean charging, charge){
	if(percent != device.currentValue("battery"))
		sendEvent(name: "battery", value: percent, isStateChange: true, displayed: false);
        
    def ps = device.currentValue("powerSource") == "battery" ? "false" : "true"
    if(charge != ps)
		sendEvent(name: "powerSource", value: (charging ? "dc":"battery"), isStateChange: true, displayed: false);
}

private formatLocalTime(format = "EEE, MMM d yyyy @ h:mm:ss a z", time = now()) {
	def formatter = new java.text.SimpleDateFormat(format)
	formatter.setTimeZone(location.timeZone)
	return formatter.format(time)
}
