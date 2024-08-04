
const EinladungsMenu = ({ invitations }) => {

    return (
        <div className="einladung-menu padding absolute background right-0 mt-2 w-48 rounded-md overflow-hidden shadow-xl z-10">
            {invitations.map(invitation => {
                const isInvitation = invitation.art === 'EINLADUNG';
                return (
                    <div key={invitation.id} className="einladung-menu px-4 py-2 border-b">
                        <p className="text-sm">
                            {isInvitation
                                ? `${invitation.nameDesTeilnehmers} lädt dich zum ${invitation.nameDesAusflugs} ein`
                                : `Anfrage zum Teilnehmen von ${invitation.nameDesTeilnehmers}`}
                        </p>
                    </div>
                );
            })}
        </div>
    );
};

export default EinladungsMenu;
