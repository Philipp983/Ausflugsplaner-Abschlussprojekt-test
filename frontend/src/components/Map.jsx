import React, { useEffect, useState, useRef } from 'react';
import { GoogleMap, Marker, InfoWindow, DirectionsRenderer, useJsApiLoader } from '@react-google-maps/api';
import axios from 'axios';
import Sidebar from './Sidebar';

const mapContainerStyle = {
    height: "50vh",
    flex: 1,
};

const defaultCenter = {
    lat: 50.373920,
    lng: 120.735603,
};
const libraries = ['places'];

function Map({ googleMapsApiKey, destination }) {
    const [markers, setMarkers] = useState([]);
    const [selectedPlace, setSelectedPlace] = useState(null);
    const [reiseziel, setReiseziel] = useState(null);
    const [directions, setDirections] = useState(null);
    const [routeDetails, setRouteDetails] = useState(null);
    const [travelMode, setTravelMode] = useState("DRIVING");
    const [currentLocation, setCurrentLocation] = useState(null);
    const [useCurrentLocation, setUseCurrentLocation] = useState(true);
    const [startAddress, setStartAddress] = useState("");
    const [showFullSteps, setShowFullSteps] = useState(false);

    const { isLoaded, loadError } = useJsApiLoader({
        googleMapsApiKey: googleMapsApiKey,
        libraries: libraries,
        language: "de"
    });

    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const { latitude, longitude } = position.coords;
                    setCurrentLocation({ lat: latitude, lng: longitude });
                },
                (error) => console.error("Fehler beim Lokalisieren der aktuellen Postition:", error),
                { timeout: 10000 }
            );
        }
    }, []);

    useEffect(() => {
        if (destination && isLoaded) {
            axios.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${destination}&key=${googleMapsApiKey}&language=de`)
                .then((response) => {
                    if (response.data.results.length > 0) {
                        const location = response.data.results[0].geometry.location;
                        setReiseziel(location);
                    } else {
                        console.error("No results found for the destination.");
                    }
                })
                .catch((error) => {
                    console.error("Error fetching destination coordinates:", error);
                });
        }
    }, [destination, googleMapsApiKey, isLoaded]);

    const handleDirections = async () => {
        if (reiseziel) {
            const origin = useCurrentLocation ? currentLocation : await getCoordinates(startAddress);
            if (!origin) {
                console.error("Error fetching start address coordinates.");
                return;
            }
            const directionsService = new window.google.maps.DirectionsService();
            directionsService.route(
                {
                    origin: origin,
                    destination: reiseziel,
                    travelMode: window.google.maps.TravelMode[travelMode],
                    language: 'de'
                },
                (result, status) => {
                    if (status === window.google.maps.DirectionsStatus.OK) {
                        setDirections(result);
                        const route = result.routes[0].legs[0];
                        setRouteDetails({
                            duration: route.duration.text,
                            distance: route.distance.text,
                            steps: route.steps.map(step => ({
                                instructions: stripHtml(step.instructions),
                                distance: step.distance.text,
                                duration: step.duration.text,
                            })),
                        });
                    } else {
                        console.error(`Error fetching directions ${result}`);
                    }
                }
            );
        }
    };

    const getCoordinates = async (address) => {
        try {
            const response = await axios.get(`https://maps.googleapis.com/maps/api/geocode/json?address=${address}&key=${googleMapsApiKey}&language=de`);
            if (response.data.results.length > 0) {
                return response.data.results[0].geometry.location;
            } else {
                console.error("No results found for the start address.");
            }
        } catch (error) {
            console.error("Error fetching start address coordinates:", error);
        }
        return null;
    };

    const stripHtml = (html) => {
        const temporalDivElement = document.createElement("div");
        temporalDivElement.innerHTML = html;
        return temporalDivElement.textContent || temporalDivElement.innerText || "";
    };

    const handleToggleSteps = () => {
        setShowFullSteps(!showFullSteps);
    };

    if (loadError) {
        return <div>Fehler laden Maps</div>;
    }

    if (!isLoaded) {
        return <div>Lade Maps</div>;
    }

    return (
        <>
        <div className="map-container">
            <Sidebar
                travelMode={travelMode}
                setTravelMode={setTravelMode}
                handleDirections={handleDirections}
                reiseziel={reiseziel}
                currentLocation={currentLocation}
                useCurrentLocation={useCurrentLocation}
                setUseCurrentLocation={setUseCurrentLocation}
                startAddress={startAddress}
                setStartAddress={setStartAddress}
                googleMapsApiKey={googleMapsApiKey}
                routeDetails={routeDetails}
            />
            <GoogleMap
                mapContainerClassName="google-maps-custom"
                mapContainerStyle={mapContainerStyle}
                center={currentLocation || defaultCenter}
                zoom={currentLocation ? 10 : 3}
            >
                {currentLocation && (
                    <Marker position={currentLocation} label="Sie sind hier" />
                )}
                {markers.map((marker, index) => (
                    <Marker
                        key={index}
                        position={marker.location}
                        onClick={() => setSelectedPlace(marker)}
                    />
                ))}
                {selectedPlace && (
                    <InfoWindow
                        position={selectedPlace.location}
                        onCloseClick={() => setSelectedPlace(null)}
                    >
                        <div>
                            <h2>{selectedPlace.formattedAddress}</h2>
                            <button onClick={() => setReiseziel(selectedPlace.location)}>Als Reiseziel festlegen</button>
                        </div>
                    </InfoWindow>
                )}
                {directions && (
                    <DirectionsRenderer directions={directions} />
                )}
            </GoogleMap>

        </div>
    {routeDetails && (
        <div className="route-details">
            <h4>Schritte:</h4>
            <ul>
                {routeDetails.steps.slice(0, showFullSteps ? undefined : 3).map((step, index) => (
                    <li key={index}>
                        {step.instructions} ({step.distance}, {step.duration})
                    </li>
                ))}
            </ul>
            {routeDetails.steps.length > 3 && (
                <button className="read-more" onClick={handleToggleSteps}>
                    {showFullSteps ? 'Show Less' : 'Read More'}
                </button>
            )}
        </div>
    )}
    </>
    );
}

export default Map;
