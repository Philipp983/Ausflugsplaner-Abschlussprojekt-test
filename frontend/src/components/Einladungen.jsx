import {useContext, useState} from 'react';
import axios from 'axios';
import UserContext from "../UserContext.jsx";
import EinladungCard from "./EinladungCard.jsx";
import Button from "../utils/Button.jsx";

const Einladungen = () => {
    const { invitations, setInvitations, benutzer, benutzerHandle } = useContext(UserContext);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const baseUrl = import.meta.env.VITE_BASE_URL;

    // Funktion zum Behandeln von Einladungen und Anfragen
    const handleInvitation = (invitationId, accept) => {
        setLoading(true);
        setError(null);
        const invitation = invitations.find(inv => inv.id === invitationId);
        if (!invitation) {
            setError('Einladung nicht gefunden');
            setLoading(false);
            return;
        }
        const url = `${baseUrl}/api/einladungen/${invitationId}/${accept ? 'annehmen' : 'ablehnen'}`;
        const method = accept ? axios.put : axios.delete;

        method(url)
            .then(response => {
                if (accept) {
                    // Aktualisiere den Benutzer und seine Ausfluege
                    const { benutzer: updatedBenutzer, ausflug } = response.data;
                    if (invitation.art === 'EINLADUNG') {
                        // For EINLADUNG, add the new Ausflug to the user's list
                        benutzerHandle({
                            ...updatedBenutzer,
                            ausfluege: [...(benutzer.ausfluege || []), ausflug]
                        });
                    } else {
                        // For ANFRAGE, just update the user data
                        benutzerHandle(updatedBenutzer);
                    }
                } else {
                    // Aktualisiere nur den Benutzer
                    const updatedBenutzer = response.data;
                    benutzerHandle(updatedBenutzer);
                }
                // Entferne die Einladung aus dem lokalen Zustand
                setInvitations(prevInvitations =>
                    prevInvitations.filter(inv => inv.id !== invitationId)
                );
            })
            .catch(error => {
                console.error('Fehler beim Bearbeiten der Einladung:', error);
                setError('Ein Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.');
            })
            .finally(() => setLoading(false));
    };

    // Rendert eine einzelne Einladung oder Anfrage
    const renderInvitation = (invitation) => {
        const isInvitation = invitation.art === 'EINLADUNG';
        const message = isInvitation
            ? `${invitation.nameDesTeilnehmers} hat Sie zum Ausflug "${invitation.nameDesAusflugs}" eingeladen`
            : `${invitation.nameDesTeilnehmers} möchte am Ausflug "${invitation.nameDesAusflugs}" teilnehmen`;

        return (
            <div key={invitation.id} className="bg-black shadow-md rounded px-8 pt-6 pb-8 mb-4">
            {/*    {isInvitation ?
                <div className="invintation-marker">Einladung</div> : ""}*/}
                <p className="mb-4">{message}</p>
                <div className="flex space-x-4">
                    <button
                        onClick={() => handleInvitation(invitation.id, true)}
                        className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                        disabled={loading}
                    >
                        {isInvitation ? "Annehmen" : "Genehmigen"}
                    </button>
                    <button
                        onClick={() => handleInvitation(invitation.id, false)}
                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
                        disabled={loading}
                    >
                        {isInvitation ? "Ablehnen" : "Verweigern"}
                    </button>
                </div>
            </div>
        );
    };

    if (loading) return <div>Lädt...</div>;
    if (error) return <div className="text-red-500">{error}</div>;

    return (
        <div className="container mx-auto px-4 py-8">
            <h2 className="text-2xl font-bold mb-6">Einladungen und Anfragen</h2>
            {invitations.length === 0 ? (
                <p>Keine Einladungen oder Anfragen vorhanden.</p>
            ) : (
                invitations.map(renderInvitation)
            )}
        </div>
    );
};

export default Einladungen;
