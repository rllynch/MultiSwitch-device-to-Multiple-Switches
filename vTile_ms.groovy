/**
 *  V Tile device-type for ms_w_vts
 *
 *	Needed for Multi Switch with Virtual Tiles to create virtual switch tiles in ST for devices that have multiple "switch[x]"
 *     attributes within them and have on[x] and off[x] commands for each (fairly common device-types)
 *     Also has support for device-label inside the name when on or off and polling occurs
 *  Copyright 2014 Cooper Lee
 *
 */
metadata {
	definition (name: "vTile_ms", namespace: "ms_w_vts", author: "Cooper Lee") {
		capability "Switch"
		capability "relaySwitch"
		capability "Polling"
		capability "Refresh"
	}
}

preferences {
	tiles {
		standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "name", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#DDDDff", nextState: "turningOn"
			state "off", label: 'off', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#DDDDff", nextState: "turningOn"
			state "on", label: 'on', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "turningOff"
			state "turningOff", label: 'turning off', icon: "st.switches.switch.on", backgroundColor: "#c0c0c0", nextState: "on"
			state "turningOn", label: 'turning on', icon: "st.switches.switch.off", backgroundColor: "#c0c0c0", nextState: "off"
		}

		main "switch"
		details(["switch"])
	}
}

def parse(desc) {
	def results = []
    if(desc=="updated") { log.debug "Device $device.label has been UPDATED"; poll() }
}

def on() {
	sendEvent([name: "switch", value: "on"])
    parent.on(this)
  	log.debug "$device.label is On"
}

def off() {
	sendEvent([name: "switch", value: "off"])
    parent.off(this)
  	log.debug "$device.label is Off"
}

def poll() {
	def current = device.currentValue("switch")
    log.debug "Polling - $device.label is $current"
	if(!current || current=="off") { sendEvent(name:"switch", value:"$device.label", isStateChange:true, displayed:false) }
}

def df(e) {
	//  *  df(e) - Date Format "E"
	//  *     Takes epoch time format and returns Date formatted in current timezone
	//  *  Copyright 2014 Cooper Lee
	def locale = getWeatherFeature("geolookup", zip); def tz = TimeZone.getTimeZone(locale.location.tz_long); def formatted
	if(e) { formatted = new Date(e).format("EEE, MMM d, 'at' hh:mm aaa", tz); return formatted }
}
