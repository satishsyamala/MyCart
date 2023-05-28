import { geolocated } from "react-geolocated";
import React, { Component } from 'react';
import APIService from "../service/APIService"

class GeoCoords {
    constructor(props) {
        
    }

    position = async () => {
        await navigator.geolocation.getCurrentPosition(
            position => {

                APIService.setLocalStorage('lat', position.coords.latitude);
                APIService.setLocalStorage('long', position.coords.longitude);
            },
            err => console.log(err)
        );

    }

    getLatitude() {
        this.position();
        return APIService.getLocalStorage('lat');
    }

    getLongitude() {
        this.position();
        return APIService.getLocalStorage('long');;
    }
} export default GeoCoords;