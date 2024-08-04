import React, { useContext, useEffect, useState } from 'react';
import UserContext from "../UserContext.jsx";
import EinladungCard from "./EinladungCard.jsx";
import Button from "../utils/Button.jsx";

const EinladungenBenutzerSeite = () => {
    const { invitations, handleInvitation, benutzer, benutzerHandle } = useContext(UserContext);
    const [loading, setLoading] = useState(false);
    const [ausfluege, setAusfluege] = useState([]);
    const [error, setError] = useState(null);
    const baseUrl = import.meta.env.VITE_BASE_URL;


    useEffect(() => {
        fetchAusfluege();
    }, []);

    const fetchAusfluege = async () => {
        try {
            const response = await fetch(`${baseUrl}/api/ausfluege`);
            if (!response.ok) {
                throw new Error('Fehler beim Anfragen des Ausfluges');
            }
            const data = await response.json();
            setAusfluege(data);
        } catch (error) {
            console.error('Fehler beim Laden der Ausflüge:', error);
            setError('Fehler beim Laden der Ausflüge');
        }
    };

    const handleInvitationResponse = (invitationId, accept) => {
        setLoading(true);
        setError(null);

        const invitation = invitations.find(inv => inv.id === invitationId);
        if (!invitation) {
            console.error('Einladung nicht gefunden');
            setError('Einladung nicht gefunden');
            setLoading(false);
            return;
        }

        handleInvitation(invitationId, accept ? 'accept' : 'decline')
            .then(response => {
                if (accept) {
                    const { benutzer: updatedBenutzer, ausflug } = response;
                    if (invitation.art === 'EINLADUNG') {
                        benutzerHandle({
                            ...updatedBenutzer,
                            ausfluege: [...(benutzer.ausfluege || []), ausflug]
                        });
                    } else {
                        benutzerHandle(updatedBenutzer);
                    }
                } else {
                    benutzerHandle(response);
                }
                // console.log(`Einladung ${accept ? 'angenommen' : 'abgelehnt'}`);
            })
            .catch(error => {
                console.error('Fehler beim Bearbeiten der Einladung:', error);
                setError('Ein Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.');
            })
            .finally(() => setLoading(false));
    };


    const renderInvitation = (invitation) => {
        const isInvitation = invitation.art === 'EINLADUNG';

        const ausflug = ausfluege.find(a => a.id === invitation.ausflugId);
        const message = isInvitation
            ? `${invitation.nameDesTeilnehmers} lädt dich zum ${invitation.nameDesAusflugs} ein:`
            : `Anfrage zum Teilnehmen von ${invitation.nameDesTeilnehmers}`;

        return (
            <div className="ausflug-card" key={invitation.id}>
                <div className="einladungMessage">
                    <p className="mb-4 imessage">{message}</p>
                    <div className="flex space-x-4" id="einladung-btn">
                        <Button
                            text={isInvitation ? "Annehmen" : "Genehmigen"}
                            onClick={() => handleInvitationResponse(invitation.id, true)}
                            color="var(--main-green-color)"
                        />
                        <Button
                            className=""
                            text={isInvitation ? "Ablehnen" : "Verweigern"}
                            onClick={() => handleInvitationResponse(invitation.id, false)}
                            color="var(--main-lavender-color)"
                            icon={() => <span className="icon-cancel">✖</span>}
                        />
                    </div>
                </div>
                {ausflug && <EinladungCard ausflug={ausflug} baseUrl={baseUrl}/>}
            </div>
        );
    };

    return (
        <div className="card-list trennen">
            {error && <p className="text-red-500">{error}</p>}
            {invitations.length > 0 ? (
                invitations.map(invitation => renderInvitation(invitation))
            ) : <p>Keine Einladungen vorhanden.</p>}
        </div>
    );
};

export default EinladungenBenutzerSeite;
