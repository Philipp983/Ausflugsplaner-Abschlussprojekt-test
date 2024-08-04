// UserContext.jsx
import React, {createContext, useState, useEffect, useCallback} from "react";
import axios from "axios";

const UserContext = createContext();

export function UserProvider({ children }) {
    const [invitations, setInvitations] = useState([]);
    const baseUrl = import.meta.env.VITE_BASE_URL;
    const [benutzer, setBenutzer] = useState(() => {
        const savedBenutzer = localStorage.getItem("benutzer");
        try {
            return savedBenutzer ? JSON.parse(savedBenutzer) : null;
        } catch (e) {
            console.error("Error parsing saved benutzer from localStorage:", e);
            return null;
        }
    });

    useEffect(() => {
        if (benutzer) {
            localStorage.setItem("benutzer", JSON.stringify(benutzer));
            setInvitations(benutzer.einladungenSimpel || []);
        } else {
            localStorage.removeItem("benutzer");
            setInvitations([]);
        }
    }, [benutzer]);

    const benutzerHandle = useCallback((newBenutzer) => {
        setBenutzer(newBenutzer);
    }, []);

    const fetchInvitations = useCallback(() => {
        if (benutzer) {
            axios.get(`${baseUrl}/api/einladungen/benutzer/${benutzer.id}`)
                .then(response => {
                    setInvitations(response.data);
                })
                .catch(error => {
                    console.error('Error fetching invitations:', error);
                });
        }
    }, [benutzer]);

    const refreshUserData = useCallback(async () => {
        if (benutzer && benutzer.id) {
            try {
                const response = await axios.get(`${baseUrl}/api/benutzer/${benutzer.id}`);
                if (response.status === 200) {
                    setBenutzer(response.data);
                    setInvitations(response.data.einladungenSimpel || []);
                }
            } catch (error) {
                console.error('Error refreshing user data:', error);
            }
        }
    }, [benutzer]);

    const handleInvitation = async (invitationId, action) => {
        try {
            let response;
            if (action === 'accept') {
                response = await axios.put(`${baseUrl}/api/einladungen/${invitationId}/annehmen`);
            } else if (action === 'decline') {
                response = await axios.delete(`${baseUrl}/api/einladungen/${invitationId}/ablehnen`);
            } else {
                throw new Error('Invalid action');
            }

            if (response.status === 200) {
                // Update local state
                setInvitations(prevInvitations =>
                    prevInvitations.filter(inv => inv.id !== invitationId)
                );
                // Refresh user data after handling invitation
                await refreshUserData();
            }
            return response.data;
        } catch (error) {
            console.error('Error handling invitation:', error);
            throw error;
        }
    };

    return (
        <UserContext.Provider value={{ benutzerHandle, benutzer, invitations, fetchInvitations, handleInvitation, refreshUserData }}>
            {children}
        </UserContext.Provider>
    );
}

export default UserContext;
