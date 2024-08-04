import React, { useState, useContext, useRef, useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/benutzerSeite.css';
import Button from "../utils/Button.jsx";
import AusflugCardListe from "../components/AusflugCardListe.jsx";
import UserContext from "../UserContext.jsx";
import Popup from '../components/Popup.jsx';
import Einladungen from "../components/Einladungen.jsx";
import useProfilePicture from '../components/useProfilePicture';
import Pagination from "../components/Pagination";
import EinladungenBenutzerSeite from "../components/EinladungenBenutzerSeite.jsx";

const BenutzerSeite = () => {
    const { benutzer, benutzerHandle, invitations, refreshUserData } = useContext(UserContext);
    const [showPopup, setShowPopup] = useState(false);
    const navigate = useNavigate();
    const profilePicturePreview = useProfilePicture(benutzer.id);

    const [currentPage, setCurrentPage] = useState(1);
    const [ausfluegePerPage] = useState(4); // Hier einstellen, wie viele Ausflüge pro Seite
    const paginationAnchorRef = useRef(null);

    const indexOfLastAusflug = currentPage * ausfluegePerPage;
    const indexOfFirstAusflug = indexOfLastAusflug - ausfluegePerPage;
    const sortedAusfluege = benutzer.ausfluege.sort((a, b) => a.id - b.id);
    const currentAusfluege = sortedAusfluege.slice(indexOfFirstAusflug, indexOfLastAusflug);
    const baseUrl = import.meta.env.VITE_BASE_URL;


    useEffect(() => {
        refreshUserData();
    }, []); // Refresh user data when component mounts

    const paginate = (pageNumber) => {
        setCurrentPage(pageNumber);
        if (paginationAnchorRef.current) {
            paginationAnchorRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    };

    const handleCancel = useCallback(() => {
        setShowPopup(false);
        console.log('Popup canceled');
    }, []);

    const handleConfirm = () => {
        setShowPopup(false);
        navigate("/login");
        benutzerHandle(null);
        console.log('Popup confirmed');
    };

    const handleEditProfile = () => {
        navigate('/benutzer-bearbeiten');
    };

    return (
        <div>
            <div className="position-relative z-index home min-h-screen flex flex-col items-center justify-center text-center">
                <div className="z-index container mx-auto px-4 flex flex-col items-center">
                    <div className="profile-section">
                        <div className="profil-bild"
                             style={{ backgroundImage: `url(${profilePicturePreview || 'https://upload.wikimedia.org/wikipedia/commons/2/2c/Default_pfp.svg'})` }}>
                        </div>
                        <h2>{benutzer.userName}</h2>
                        <p>{benutzer.userEmail}</p>

                        <div className="profile-buttons">
                            <Button text="Profil korrigieren" color="var(--main-green-color)" onClick={handleEditProfile} />
                            <Button text="Abmelden" color="var(--main-lavender-color)" onClick={() => setShowPopup(true)} />
                        </div>
                    </div>

                    {benutzer.beschreibung ? (
                        <div className="about-section">
                            <h3>Über mich</h3>
                            <p className="ueber-mich">{benutzer.beschreibung}</p>
                        </div>
                    ) : (
                        <div className="about-section">
                            <h3>Über mich</h3>
                            <p className="ueber-mich">Dieser Benutzer hat noch keine Beschreibung über sich hinzugefügt</p>
                        </div>
                    )}

                {invitations.length > 0? (
                    <EinladungenBenutzerSeite />
                ) : "" }

                    {benutzer && benutzer.ausfluege && benutzer.ausfluege.length > 0 && (
                        <>
                            <div ref={paginationAnchorRef} className="pagination-anchor"></div>
                            <div className="about-section">
                                <h3>Meine Ausflüge</h3>
                            </div>
                            <AusflugCardListe ausfluege={currentAusfluege} baseUrl={baseUrl}/>
                            <Pagination
                                ausfluegePerPage={ausfluegePerPage}
                                totalAusfluege={benutzer.ausfluege.length}
                                paginate={paginate}
                                currentPage={currentPage}
                            />
                        </>
                    )}
                    {showPopup && (
                        <Popup
                            title="Bestätigung"
                            description="Möchten Sie sich wirklich abmelden?"
                            onCancel={handleCancel}
                            onConfirm={handleConfirm}
                            cancelText="Abbrechen"
                            confirmText="Abmelden"
                        />
                    )}
                </div>
            </div>
        </div>
    );
};

export default BenutzerSeite;
