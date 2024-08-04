import React, { useContext, useState, useEffect } from 'react';
import '../styles/benutzerSeite.css';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import UserContext from '../UserContext.jsx';
import AusflugCard from '../components/AusflugCard.jsx';
import Button from '../utils/Button.jsx'; // Import Button component
import useProfilePicture from '../components/useProfilePicture.jsx';

const BenutzerAllgemeineSeite = () => {
    const { benutzer, benutzerHandle } = useContext(UserContext);
    const location = useLocation();
    const navigate = useNavigate();
    const { userId } = location.state;
    const [currentUser, setCurrentUser] = useState(null);
    const profilePicturePreview = useProfilePicture(userId);
    const baseUrl = import.meta.env.VITE_BASE_URL;

    useEffect(() => {
        axios.get(`${baseUrl}/api/benutzer/${userId}`)
            .then((res) => {
                setCurrentUser(res.data);
            })
            .catch((err) => {
                console.error("Error fetching all user's data:", err);
            });
    }, [userId]);

    useEffect(() => {
        axios.get(`${baseUrl}/api/benutzer/${benutzer.id}`)
            .then((res) => {
                benutzerHandle(res.data);
            })
            .catch((err) => {
                console.error("Error fetching all user's data:", err);
            });
    }, [benutzer.id, benutzerHandle]);

    const handleNavigate = () => {
        navigate('/ausflug-erstellen');
    };

    const benutzerAusfluege = benutzer ? benutzer.ausfluege : [];
    const userAusfluege = currentUser ? currentUser.ausfluege : [];
    const gemeinsameAusfluege = benutzerAusfluege && userAusfluege
        ? benutzerAusfluege.filter((benutzerAusflug) => userAusfluege.some((userAusflug) => benutzerAusflug.id === userAusflug.id))
        : [];

    return (
        <div className="position-relative z-index home min-h-screen flex flex-col items-center justify-center text-center">
            <div className="z-index container mx-auto px-4 flex flex-col items-center">
                {currentUser && (
                    <>
                        <img
                            src={profilePicturePreview || "https://cdn.pixabay.com/photo/2015/03/04/22/35/avatar-659651_1280.png"}
                            alt={`${currentUser?.userName || 'User'}'s profile picture`}
                            className="profile-image"
                        />
                        <div className="about-section">
                            <h3>Über mich</h3>
                            <p className="ueber-mich">
                                {currentUser.beschreibung ? currentUser.beschreibung : 'Der Benutzer hat noch keine Beschreibung hinzugefügt'}
                            </p>
                        </div>
                    </>
                )}
                <h3>Gemeinsame Ausflüge</h3>
                {gemeinsameAusfluege.length > 0 ? (
                    gemeinsameAusfluege.map((ausflug, index) => (
                        <div className="cards-container" key={index}>
                            <div className="card-list">
                                <AusflugCard
                                    ausflug={ausflug}
                                    baseUrl={baseUrl}
                                />
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="keine-ausfluege">
                        <p>Es gibt noch keine gemeinsame Ausfluege</p>
                        <Button
                            text="Ausflug erstellen"
                            onClick={handleNavigate}
                            color="var(--main-lavender-color)"
                        />
                    </div>
                )}
            </div>
        </div>
    );
};

export default BenutzerAllgemeineSeite;
