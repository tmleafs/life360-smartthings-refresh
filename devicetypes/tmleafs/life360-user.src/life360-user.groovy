/**
 *  Copyright 2015 SmartThings
 *
 *	BTRIAL DISTANCE AND SLEEP PATCH 29-12-2017
 *	Updated Code to handle distance from, and sleep functionality
 *
 *	TMLEAFS REFRESH PATCH 06-12-2016 V1.1
 *	Updated Code to match Smartthings updates 12-05-2017 V1.2
 *	Added Null Return on refresh to fix WebCoRE error 12-05-2017 V1.2
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
 
metadata {
	definition (name: "Life360 User", namespace: "btrial", author: "btrial") {
		capability "Presence Sensor"
		capability "Sensor"
        capability "Refresh"
		capability "Sleep Sensor"
		attribute "distanceMetric", "Number"
		command "refresh"
		command "asleep"
        command "awake"
        command "toggleSleeping"
	}

	simulator {
		status "present": "presence: 1"
		status "not present": "presence: 0"
	}

	tiles {
		multiAttributeTile(name: "display", width: 2, height: 2, canChangeBackground: true) {
			tileAttribute ("device.display", key: "PRIMARY_CONTROL") {
            	attributeState "present, not sleeping", label: 'Home', icon:"st.nest.nest-away", backgroundColor:"#c0ceb9"
				attributeState "present, sleeping", label: 'Home (asleep)', icon:"st.Bedroom.bedroom2", backgroundColor:"#6879a3"
				attributeState "not present", label: 'Away', icon:"st.Office.office5", backgroundColor:"#777777"
            }
       		tileAttribute ("device.status", key: "SECONDARY_CONTROL") {
				attributeState "default", 
					label:'${currentValue}'
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

		main "presence"
		details(["display", "presence", "sleeping", "lastLocationUpdate", "refresh"])
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
	
	def status = sprintf("%.2f", homeDistance / 1000) + " km from: Home"
	if (status != device.currentValue('status')) {
    	sendEvent( name: "status", value: status, isStateChange: true, displayed: false )
    }
	
	sendEvent( name: "distanceMetric", value: homeDistance, isStateChange: true, displayed: false )
	
	sendEvent( name: "lastLocationUpdate", value: "Last location update on:\r\n${formatLocalTime("MM/dd/yyyy @ h:mm:ss a")}", displayed: false ) 
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

private formatLocalTime(format = "EEE, MMM d yyyy @ h:mm:ss a z", time = now()) {
	def formatter = new java.text.SimpleDateFormat(format)
	formatter.setTimeZone(location.timeZone)
	return formatter.format(time)
}
