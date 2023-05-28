import React, { Component } from 'react';
import { Map, InfoWindow, Marker, GoogleApiWrapper } from 'google-maps-react';

export class MapContainer extends Component {


    constructor(props) {
        super(props);
        this.state = {
            markers: [
                {
                    title: "The marker`s title will appear as a tooltip.",
                    name: "SOMA",
                    position: { lat: this.props.lat, lng: this.props.lon }
                }
            ]
        };
        this.mapClicked = this.mapClicked.bind(this);
    }

    mapClicked(t, map, coord) {
        const { latLng } = coord;
        const lat = latLng.lat();
        const lng = latLng.lng();
       let po={lat:lat,lon:lng} ; 
        this.setState(previousState => {
            return {
                markers: [
                    {
                        title: "",
                        name: "",
                        position: { lat, lng }
                    }
                ]
            };
        });
      
        this.props.onSubmit(this.props.keyType,po);
    }



   

    render() {
        return (
            <Map
                google={this.props.google}
                style={{ width: "90%", height: "80%" }}
                className={"map"}
                
                zoom={8}
                onClick={this.mapClicked}
                initialCenter={{lat: this.props.lat, lng: this.props.lon}}
            >
                {this.state.markers.map((marker, index) => (
                    <Marker
                        key={index}
                        title={marker.title}
                        name={marker.name}
                        position={marker.position}
                    />
                ))}
            </Map>
        );
    }
}

export default GoogleApiWrapper({
    apiKey: ('AIzaSyBQbEik6AdLZg1IIO1KlYBYjeOYtYcXXxA')
})(MapContainer)