import React, {useState, useEffect, useRef} from 'react';
import axios from 'axios';
import {Autocomplete} from "@react-google-maps/api";

const Sidebar = ({
                     travelMode,
                     setTravelMode,
                     handleDirections,
                     reiseziel,
                     currentLocation,
                     useCurrentLocation,
                     setUseCurrentLocation,
                     startAddress,
                     setStartAddress,
                     googleMapsApiKey,
                     routeDetails
                 }) => {
    const autocompleteRef = useRef(null);

    const onLoad = (autocomplete) => {
        autocompleteRef.current = autocomplete;
    };
    const onPlaceChanged = () => {
        if (autocompleteRef.current) {
            const place = autocompleteRef.current.getPlace();
            if (place.geometry) {
                setStartAddress(place.formatted_address);
            }
        }
    };
    const getCoordinates = async (address) => {
        try {
            const response = await axios.get(`https://maps.googleapis.com/maps/api/geocode/json`, {
                params: {
                    address,
                    key: googleMapsApiKey,
                    language: 'de'
                },
            });
            if (response.data.results.length > 0) {
                return response.data.results[0].geometry.location;
            } else {
                console.error("No results found for the address.");
                return null;
            }
        } catch (error) {
            console.error("Fehler beim Abrufen der Koordinaten:", error);
            return null;
        }
    };

    return (
        <div className="sidebar">
            <div>
                <label className={`flex items-center ${travelMode === "DRIVING" ? 'bg-blue-200' : ''}`}>
                    <input
                        className="input-styles"
                        type="radio"
                        value="DRIVING"
                        checked={travelMode === "DRIVING"}
                        onChange={(e) => setTravelMode(e.target.value)}
                    />
                    🚗 mit dem Auto
                    {travelMode === "DRIVING" && routeDetails && (
                        <div className="ml-4 text-sm text-white">
                            <p>Dauer: {routeDetails.duration}</p>
                            <p>Distanz: {routeDetails.distance}</p>
                        </div>
                    )}
                </label>
                <label className={`flex items-center ${travelMode === "WALKING" ? 'bg-blue-200' : ''}`}>
                    <input
                        className="input-styles"
                        type="radio"
                        value="WALKING"
                        checked={travelMode === "WALKING"}
                        onChange={(e) => setTravelMode(e.target.value)}
                    />
                    🚶 Zu Fuß
                    {travelMode === "WALKING" && routeDetails && (
                        <div className="ml-4 text-sm text-white">
                            <p>Duration: {routeDetails.duration}</p>
                            <p>Distance: {routeDetails.distance}</p>
                        </div>
                    )}
                </label>
                <label className={`flex items-center ${travelMode === "BICYCLING" ? 'bg-blue-200' : ''}`}>
                    <input
                        className="input-styles"
                        type="radio"
                        value="BICYCLING"
                        checked={travelMode === "BICYCLING"}
                        onChange={(e) => setTravelMode(e.target.value)}
                    />
                    🚴 Mit dem Fahrrad
                    {travelMode === "BICYCLING" && routeDetails && (
                        <div className="ml-4 text-sm text-white">
                            <p>Duration: {routeDetails.duration}</p>
                            <p>Distance: {routeDetails.distance}</p>
                        </div>
                    )}
                </label>
                <label className={`flex items-center ${travelMode === "TRANSIT" ? 'bg-blue-200' : ''}`}>
                    <input
                        className="input-styles"
                        type="radio"
                        value="TRANSIT"
                        checked={travelMode === "TRANSIT"}
                        onChange={(e) => setTravelMode(e.target.value)}
                    />
                    🚇 Mit öffentlichen Verkehrsmitteln
                    {travelMode === "TRANSIT" && routeDetails && (
                        <div className="ml-4 text-sm text-white">
                            <p>Duration: {routeDetails.duration}</p>
                            <p>Distance: {routeDetails.distance}</p>
                        </div>
                    )}
                </label>
            </div>
            <div>
                <label>
                    <input
                        className="input-styles"
                        type="radio"
                        checked={useCurrentLocation}
                        onChange={() => setUseCurrentLocation(true)}
                    />
                    Aktuellen Standort verwenden
                </label>
                <label>
                    <input
                        className="input-styles"
                        type="radio"
                        checked={!useCurrentLocation}
                        onChange={() => setUseCurrentLocation(false)}
                    />
                    Geben Sie die Startadresse ein
                </label>
                {!useCurrentLocation && (
                    <Autocomplete
                        onLoad={onLoad}
                        onPlaceChanged={onPlaceChanged}
                        options={{
                            language: 'de'
                        }}
                    >
                        <input
                            className="input-styles"
                            type="text"
                            placeholder="Geben Sie die Startadresse ein"
                            className="text-sm bg-gray-100 p-1 rounded"
                        />
                    </Autocomplete>
                )}
            </div>
            {reiseziel && (useCurrentLocation || startAddress) && (
                <div>
                    <button onClick={handleDirections} className="paddin-left-no bg-btn-green text-white p-3 rounded">
                        Route berechnen
                    </button>
                </div>
            )}
        </div>
    );
};

export default Sidebar;
