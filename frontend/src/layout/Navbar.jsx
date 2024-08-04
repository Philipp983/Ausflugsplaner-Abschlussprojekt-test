import {useContext, useState, useEffect} from 'react';
import {FaBars, FaTimes, FaBell} from 'react-icons/fa';
import {Link} from 'react-router-dom';
import UserContext from "../UserContext.jsx";
import useProfilePicture from "../components/useProfilePicture.jsx";
import EinladungsMenu from "../components/EinladungsMenu.jsx"; // New component we'll create

const Navbar = () => {
    const {benutzer, benutzerHandle, invitations, fetchInvitations} = useContext(UserContext);
    const [isOpen, setIsOpen] = useState(false);
    const [isInvitationMenuOpen, setIsInvitationMenuOpen] = useState(false);
    const [triggerUpdate, setTriggerUpdate] = useState(false);

    const profilePicture = useProfilePicture(benutzer?.id, triggerUpdate);

    useEffect(() => {
        if (benutzer) {
            setTriggerUpdate(prev => !prev);
            fetchInvitations();
        }
    }, [benutzer, fetchInvitations]);

    const toggleMenu = () => {
        setIsOpen(!isOpen);
    };

    const toggleInvitationMenu = () => {
        setIsInvitationMenuOpen(!isInvitationMenuOpen);
    };

    return (
        <div className="z-index position-relative">
            <nav className="navigation-menu shadow-md">
                <div className="container mx-auto px-4 py-3 flex justify-between items-center">
                    <div className="flex items-center space-x-2">
                        <Link to="/" className="text-xl font-semibold">
                            <img className="logo" src="https://i.ibb.co/XzQGJ9s/image-only-white.png" alt="Logo"/>
                        </Link>
                    </div>
                    <div className="hidden md:flex space-x-8">
                        {benutzer && <Link to="/ausflug-erstellen" className="hover:underline block px-4 py-2">Ausflug erstellen</Link>}
                        {benutzer ? (
                            <>
                                <Link to="/benutzer-seite" className="hover:underline flex items-center space-x-2 px-4">
                                    <span>Hallo, {benutzer.userName}!</span>
                                    {profilePicture ? (
                                        <img src={profilePicture} alt={`${benutzer.userName}'s profile`}
                                             className="w-8 h-8 rounded-full object-cover"/>
                                    ) : (
                                        <div
                                            className="w-8 h-8 rounded-full bg-gray-300 flex items-center justify-center">
                                            {benutzer.userName.charAt(0).toUpperCase()}
                                        </div>
                                    )}
                                </Link>

                                {benutzer && invitations.length > 0 && (
                                    <div className="relative custom-padding-top10">
                                        <Link to="/benutzer-seite" className="relative">
                                            <FaBell className="bell-trigger text-2xl"/>
                                            <span
                                                className="absolute -top-1 -right-1 bg-red-500 text-white rounded-full px-2 py-1 text-xs">
                {invitations.length}
              </span>
                                        </Link>
                                        <div className="hide-EinladungsMenu absolute z-10">
                                            <EinladungsMenu invitations={invitations}/>
                                        </div>
                                    </div>
                                )}


                                <Link to="/" className="hover:underline block px-4 py-2"
                                      onClick={() => benutzerHandle("")}>Abmelden</Link>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="hover:underline block px-4 py-2">Anmelden</Link>
                                <Link to="/signup" className="hover:underline block px-4 py-2">Registrieren</Link>
                            </>
                        )}

                    </div>
                    <div className="md:hidden">
                        <button onClick={toggleMenu} className="text-2xl focus:outline-none">
                            {isOpen ? <FaTimes/> : <FaBars/>}
                        </button>
                    </div>
                </div>



                {isOpen && (
                    <div className="md:hidden shadow-md text-align-right width100 p-r-10">
                        {benutzer && <Link to="/ausflug-erstellen" className="block px-4 py-2 hover:underline">Ausflug erstellen</Link>}
                        {benutzer ? (
                            <>
                                <Link to="/benutzer-seite"
                                      className="hover:underline block px-4">Hallo, {benutzer.userName}!</Link>
                                {benutzer && invitations.length > 0 && (
                                    <div className="relative margin-left-90">
                                        <Link to="/benutzer-seite" className="relative">
                                            <FaBell className="bell-trigger text-2xl"/>
                                            <span
                                                className="absolute ma-r-1em -top-1 -right-1 bg-red-500 text-white rounded-full px-2 py-1 text-xs">
                {invitations.length}
              </span>
                                        </Link>
                                        <div className="hide-EinladungsMenu absolute z-10">
                                            <EinladungsMenu invitations={invitations}/>
                                        </div>
                                    </div>
                                )}
                                <Link to="/" className="hover:underline block px-4 py-2"
                                      onClick={() => benutzerHandle("")}>Abmelden</Link>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="hover:underline block px-4 py-2">Anmelden</Link>
                                <Link to="/signup" className="hover:underline block px-4 py-2">Registrieren</Link>
                            </>
                        )}
                    </div>
                )}
            </nav>
        </div>
    );
};

export default Navbar;
