import { useContext, useEffect, useState } from 'react';
import '../styles/ausflugSeite.css';
import Button from "../utils/Button.jsx";
import UserContext from "../UserContext.jsx";
import axios from "axios";
import { SlCalender, SlLocationPin, SlPeople } from "react-icons/sl";
import { useLocation, useNavigate } from "react-router-dom";
import BildHochladen from "../components/BildHochladen.jsx";
import useProfilePicture from '../components/useProfilePicture';
import Map from "../components/Map.jsx";

import PopupAnfrageGesendet from "../components/PopupAnfrageGesendet.jsx";
import TriangleSpinner from "../utils/TriangleSpinner.jsx";

const AusflugSeite = () => {
    const [showAnfragePopup, setShowAnfragePopup] = useState(false);
    const navigate = useNavigate();
    const { benutzer } = useContext(UserContext);
    const [ersteller, setErsteller] = useState(null);
    const location = useLocation();
    const [ausflug, setAusflug] = useState(null);
    const [loading, setLoading] = useState(true);
    const [mapLoading, setMapLoading] = useState(true);  // New state for map loading
    const [bildSrc, setBildSrc] = useState('');
    const [teilnahmeanfrageSent, setTeilnahmeanfrageSent] = useState(false);
    const [isTeilnehmer, setIsTeilnehmer] = useState(false);
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const ausflugId = location.pathname.split('/').pop();
    const erstellerId = ausflug?.erstellerDesAusflugsId;
    const googleMapsApiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;

    const handlePopupConfirm = () => {
        setShowAnfragePopup(false);
    };

    useEffect(() => {
        const fetchAusflugData = async () => {
            try {
                const res = await axios.get(`${baseUrl}/api/ausfluege/${ausflugId}`);
                const fetchedAusflug = res.data;

                if (fetchedAusflug.teilnehmerIds && fetchedAusflug.nameDerTeilnehmer) {
                    const teilnehmerIds = [];
                    const teilnehmerNamen = [];
                    fetchedAusflug.teilnehmerIds.forEach((id, index) => {
                        if (!teilnehmerIds.includes(id)) {
                            teilnehmerIds.push(id);
                            teilnehmerNamen.push(fetchedAusflug.nameDerTeilnehmer[index]);
                        }
                    });
                    fetchedAusflug.teilnehmerIds = teilnehmerIds;
                    fetchedAusflug.nameDerTeilnehmer = teilnehmerNamen;
                } else {
                    fetchedAusflug.teilnehmerIds = [fetchedAusflug.erstellerDesAusflugsId];
                    fetchedAusflug.nameDerTeilnehmer = [];
                }
                setAusflug(fetchedAusflug);

                if (fetchedAusflug.imageUrl) {
                    setBildSrc(fetchedAusflug.imageUrl);
                }

                // Fetch the ersteller data
                if (fetchedAusflug.erstellerDesAusflugsId) {
                    const erstellerRes = await axios.get(`${baseUrl}/api/benutzer/${fetchedAusflug.erstellerDesAusflugsId}`);
                    setErsteller(erstellerRes.data);
                }

                // Fetch user details for each article
                if (fetchedAusflug.sachenZumMitbringen) {
                    const artikelPromises = fetchedAusflug.sachenZumMitbringen.map(item =>
                        axios.get(`${baseUrl}/api/artikel/${item.id}`)
                    );
                    const artikelResponses = await Promise.all(artikelPromises);
                    const updatedSachenZumMitbringen = artikelResponses.map(res => res.data);
                    setAusflug(prevAusflug => ({
                        ...prevAusflug,
                        sachenZumMitbringen: updatedSachenZumMitbringen,
                    }));
                }

                setLoading(false);
            } catch (err) {
                console.error("Error fetching ausflug data:", err);
                setLoading(false);
            }
        };

        fetchAusflugData();
    }, [ausflugId, baseUrl]);

    useEffect(() => {
        if (ausflug) {
            const teilnehmerIds = ausflug.teilnehmerIds || ausflug.teilnehmerListe?.map(t => t.id) || [];
            const isAlreadyTeilnehmer = teilnehmerIds.some(teilnehmerId => teilnehmerId === benutzer.id);
            setIsTeilnehmer(isAlreadyTeilnehmer);
        }
    }, [ausflug, benutzer.id]);

    const profilePicturePreview = useProfilePicture(ausflug?.erstellerDesAusflugsId);

    const teilnahmeanfrage = () => {
        axios.post(`${baseUrl}/api/einladungen/simpel/${ausflug.id}/${benutzer.id}`)
            .then((res) => {
                if (res.status === 200) {
                    setTeilnahmeanfrageSent(true);
                    setShowAnfragePopup(true);
                }
            })
            .catch((err) => {
                console.error("Fehler beim Senden der Teilnahmeanfrage:", err);

                let errorMessage = "Ein unbekannter Fehler ist aufgetreten.";

                if (err.response) {
                    if (err.response.data && err.response.data.message) {
                        errorMessage = err.response.data.message;
                    } else {
                        errorMessage = `Fehler: ${err.response.status} ${err.response.statusText}`;
                    }
                } else if (err.request) {
                    errorMessage = "Keine Antwort vom Server erhalten. Bitte überprüfen Sie Ihre Internetverbindung.";
                } else {
                    errorMessage = err.message;
                }

                alert("Fehler beim Senden der Teilnahmeanfrage: " + errorMessage);
            });
    };

    useEffect(() => {
        if (!ausflug) {
            return;
        }

        const fetchBild = async () => {
            if (ausflug.hauptBild?.imageUrl) {
                // If it's an external URL, use the proxy
                if (ausflug.hauptBild.imageUrl.startsWith('http')) {
                    setBildSrc(`${baseUrl}/api/ausflugbild/proxy-image?url=${encodeURIComponent(ausflug.imageUrl)}`);
                } else {
                    setBildSrc(ausflug.hauptBild.imageUrl);
                }
            } else {
                try {
                    const response = await fetch(`${baseUrl}/api/ausflugbild/ausflug/${ausflug.id}`);
                    if (response.ok) {
                        const contentType = response.headers.get('content-type');
                        if (contentType && contentType.includes('text/plain')) {
                            // It's a URL
                            const imageUrl = await response.text();
                            // Use the proxy for this URL as well
                            setBildSrc(`${baseUrl}/api/ausflugbild/proxy-image?url=${encodeURIComponent(imageUrl)}`);
                        } else {
                            // It's a file
                            const blob = await response.blob();
                            const objectURL = URL.createObjectURL(blob);
                            setBildSrc(objectURL);
                        }
                    } else {
                        console.error("Error fetching image:", response.statusText);
                    }
                } catch (error) {
                    console.error('Error fetching image:', error);
                }
            }
        };

        fetchBild();
    }, [ausflug, baseUrl]);

    const handleErstellerSeite = (userId) => {
        if (ersteller) {
            navigate(`/benutzer-seite/${userId}`, { state: { userId } });
        }
    };

    const handleTeilnehmerSeite = (userId) => {
        navigate(`/benutzer-seite/${userId}`, { state: { userId } });
    };

    const handleBenutzerSeite = () => {
        navigate(`/benutzer-seite`);
    };

    const handleImageUpload = (newImageUrl) => {
        setBildSrc(newImageUrl);
    };

    const handleSacheZumMitbringenBenutzerHinzufuegen = (artikelId) => {
        axios.put(`${baseUrl}/api/artikel/${artikelId}/benutzer/${benutzer.id}`)
            .then((res) => {
                if (res.status === 200) {
                    const updatedArtikel = res.data;

                    setAusflug(prevAusflug => ({
                        ...prevAusflug,
                        sachenZumMitbringen: prevAusflug.sachenZumMitbringen.map(artikel =>
                            artikel.id === artikelId
                                ? { ...artikel, benutzer: { id: benutzer.id, userName: benutzer.userName } }
                                : artikel
                        )
                    }));
                } else {
                    alert("Fehler beim Reservieren des Artikels");
                }
            })
            .catch((err) => {
                console.error("Error reserving artikel:", err);
                alert("Fehler beim Reservieren des Artikels");
            });
    };

    const handleSacheZumMitbringenBenutzerLoeschen = (artikelId) => {
        // Fetch the existing artikel to get all details
        axios.get(`${baseUrl}/api/artikel/${artikelId}`)
            .then(res => {
                const artikel = res.data;

                // Remove the user from the artikel
                artikel.benutzer = null;

                // Send the updated artikel to the backend
                return axios.put(`${baseUrl}/api/artikel/${artikelId}/entferne`);
            })
            .then((res) => {
                if (res.status === 204) {
                    const updatedArtikel = res.data;

                    setAusflug(prevAusflug => ({
                        ...prevAusflug,
                        sachenZumMitbringen: prevAusflug.sachenZumMitbringen.map(artikel =>
                            artikel.id === artikelId
                                ? { ...artikel, benutzer: null }
                                : artikel
                        )
                    }));
                } else {
                    alert("Fehler beim Entfernen des Benutzers vom Artikel: Unerwarteter Statuscode");
                }
            })
            .catch((err) => {
                console.error("Fehler beim Entfernen des Benutzers vom Artikel:", err);

                let errorMessage = "Ein unbekannter Fehler ist aufgetreten.";

                if (err.response) {
                    if (err.response.data && err.response.data.message) {
                        errorMessage = err.response.data.message;
                    } else {
                        errorMessage = `Fehler: ${err.response.status} ${err.response.statusText}`;
                    }
                } else if (err.request) {
                    errorMessage = "Keine Antwort vom Server erhalten. Bitte überprüfen Sie Ihre Internetverbindung.";
                } else {
                    errorMessage = err.message;
                }

                alert("Fehler beim Entfernen des Benutzers vom Artikel: " + errorMessage);
            });
    };

    const renderSachenZumMitbringen = () => {
        const handleItemChange = (itemId, isChecked) => {
            if (isChecked) {
                handleSacheZumMitbringenBenutzerHinzufuegen(itemId);
            } else {
                handleSacheZumMitbringenBenutzerLoeschen(itemId);
            }
        };

        return (
            <form>
                <fieldset>
                    <legend>Bitte wähle die Artikel aus, die du mitbringen möchtest:</legend>
                    {ausflug.sachenZumMitbringen?.map(item => {
                        const isTeilnehmer = ausflug.teilnehmerIds.some(teilnehmerId => teilnehmerId == benutzer.id);
                        const isItemSelected = item.benutzer && item.benutzer.id == benutzer.id;
                        return (
                            <li key={item.id} className="flex-row">
                                {isTeilnehmer ? (
                                    <>
                                        <input
                                            type="checkbox"
                                            className="input-styles"
                                            id={item.id}
                                            name="sachenZumMitbringen"
                                            value={item.id}
                                            checked={isItemSelected}
                                            onChange={(e) => handleItemChange(item.id, e.target.checked)}
                                        />
                                        <label htmlFor={item.id}>{item.name}</label>
                                    </>
                                ) : (<span>{item.name}</span>)}
                                {item.benutzer && (
                                    <span className="benutzer-definieren"> - {item.benutzer.userName}</span>
                                )}
                            </li>
                        );
                    })}
                </fieldset>
            </form>
        );
    };

    const renderTeilnehmer = () => {
        if (!ausflug || !ausflug.nameDerTeilnehmer || ausflug.nameDerTeilnehmer.length === 0) {
            return <p className="teilnehmerliste">Keine Teilnehmer vorhanden, lade jemanden ein!</p>;
        }

        return ausflug.nameDerTeilnehmer.map((teilnehmerName, index) => {
            const teilnehmerId = ausflug.teilnehmerIds[index];
            const isLastItem = index === ausflug.nameDerTeilnehmer.length - 1;
            return (
                <p
                    className={`teilnehmerliste ${teilnehmerId == benutzer.id ? 'teilnehmerliste-benutzer' : ''}`}
                    key={teilnehmerId}
                    onClick={() => teilnehmerId != benutzer.id ? handleTeilnehmerSeite(teilnehmerId) : null}
                >
                    {teilnehmerName === benutzer.userName
                        ? `${teilnehmerName} (Du)${isLastItem ? '' : ','}`
                        : `${teilnehmerName}${isLastItem ? '' : ','}`
                    }
                </p>
            );
        });
    };

    if (loading) {
        return <TriangleSpinner visible={true} />;
    }

    if (!ausflug) {
        return <div>Ausflug nicht gefunden</div>;
    }

    return (
        <div className="ausflugseite">
            <div className="z-index position-relative padding-left-10pr">
                <div className="bild-container">
                    <img
                        className="img"
                        src={bildSrc || "https://cdn.pixabay.com/photo/2024/03/21/14/29/car-8647797_960_720.jpg"}
                        alt="Ausflug Bild"
                    />
                </div>
                <div className="position-relative">
                    <h1>{ausflug.titel}</h1>
                </div>

                {ausflug.erstellerDesAusflugsId == benutzer.id ? (
                    <div className="admin-div">

                        <div className="admin-buttons position-center">
                            <Button
                                text="Ausflug korrigieren"
                                color="var(--main-green-color)"
                                onClick={() => navigate(`/ausflug-bearbeiten/${ausflug.id}`, { state: { ausflug } })}
                            />
                            <Button
                                text="Löschen"
                                color="var(--main-lavender-color)"
                                onClick={() => alert('Löschen')}
                            />
                        </div>
                        <BildHochladen
                            ausflugId={ausflug.id}
                            maxFileSize={10 * 1024 * 1024}
                            onImageUpload={handleImageUpload}
                        />
                    </div>
                ) : (
                    !teilnahmeanfrageSent && !isTeilnehmer && (
                        <div className="teilnehmeanfrage-buttons">
                            <Button text="Teilnahmeanfrage" color="var(--main-green-color)"
                                    onClick={teilnahmeanfrage} />
                        </div>
                    )
                )}

                <div className="ausflugseite-flex-block">
                    <div className="ausflugseite-block ausflugseite-flex-block-child">
                        <div className="ausflug-seite-data">
                            <SlCalender className="ausflug-card-icon" />
                            <p>{ausflug.ausflugsdatum.replace("T", " ").slice(0, 16)}</p>
                        </div>
                        <div className="ausflug-seite-data">
                            <SlLocationPin className="ausflug-card-icon" />
                            <p>{ausflug.reiseziel}</p>
                        </div>
                        <div className="display-column ausflug-card-info ausflug-seite-data">
                            <div className="ausflug-card-info">
                                <SlPeople className="ausflug-card-icon" />
                                <p>Teilnehmer: {ausflug.teilnehmerIds?.length || 0}/{ausflug.teilnehmerAnzahl}</p>
                            </div>
                            <div>
                                {renderTeilnehmer()}
                            </div>
                        </div>
                    </div>
                    <div className="ausflugseite-block ausflugseite-flex-block-child admin">
                        <h3 className="ersteller">Ersteller des Ausflugs:</h3>
                        <div className="admin-info"
                             onClick={() => ausflug.erstellerDesAusflugsId == benutzer.id ? handleBenutzerSeite() : handleErstellerSeite(ausflug.erstellerDesAusflugsId)}>
                            <div className="profil-bild-klein"
                                 style={{backgroundImage: `url(${profilePicturePreview || 'https://upload.wikimedia.org/wikipedia/commons/2/2c/Default_pfp.svg'})`}}></div>
                            <p className="color-lavander">{ersteller ? ersteller.userName : ""}</p>
                        </div>
                    </div>
                </div>
                <div className="ausflugseite-block">
                    <h3 className="ausflug-seite-data">Beschreibung:</h3>
                    <p>{ausflug.beschreibung}</p>
                </div>
                <div className="ausflugseite-block">
                    <h3 className="ausflug-seite-data">Sachen zum Mitnehmen:</h3>
                    <ul>
                        {renderSachenZumMitbringen()}
                    </ul>

                </div>
            </div>


            <div>

                <Map googleMapsApiKey={googleMapsApiKey} destination={ausflug.reiseziel} onLoad={() => setMapLoading(false)} />
            </div>
            {showAnfragePopup && (
                <PopupAnfrageGesendet
                    description="Anfrage wurde erfolgreich gesendet"
                    onConfirm={handlePopupConfirm}
                />
            )}
        </div>
    );
};

export default AusflugSeite;
