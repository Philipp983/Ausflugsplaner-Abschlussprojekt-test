import React, { useState } from 'react';
import axios from 'axios';
import useProfilePicture from './useProfilePicture';

const ProfilePictureUpload = ({ benutzer, benutzerHandle }) => {
    const [profilePicture, setProfilePicture] = useState(null);
    const [profilePictureURL, setProfilePictureURL] = useState('');
    const [triggerUpdate, setTriggerUpdate] = useState(false);
    const profilePicturePreview = useProfilePicture(benutzer.id, triggerUpdate);
    const baseUrl = import.meta.env.VITE_BASE_URL;

    const handleFileChange = (event) => {
        const selectedFile = event.target.files[0];
        setProfilePicture(selectedFile);
    };

    const handleProfilePictureURLChange = (event) => {
        const url = event.target.value;
        setProfilePictureURL(url);
    };

    const handleProfilePictureSubmit = async (e) => {
        e.preventDefault();
        if (profilePicture) {
            const formData = new FormData();
            formData.append('image', profilePicture);

            try {
                const response = await axios.post(`${baseUrl}/api/profilbild/benutzer/${benutzer.id}/upload`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
                console.log('Profile picture uploaded:', response.data);
                benutzerHandle({ ...benutzer, profilePicture: response.data.url });
                setTriggerUpdate(!triggerUpdate); // Trigger re-fetch
            } catch (error) {
                console.error('Error uploading profile picture:', error);
            }
        } else if (profilePictureURL) {
            try {
                const response = await axios.post(`${baseUrl}/api/profilbild/benutzer/${benutzer.id}/url?imageUrl=${profilePictureURL}`);
                console.log('Profile picture URL uploaded:', response.data);
                benutzerHandle({ ...benutzer, profilePicture: profilePictureURL });
                setTriggerUpdate(!triggerUpdate); // Trigger re-fetch
            } catch (error) {
                console.error('Error uploading profile picture URL:', error);
            }
        } else {
            console.log('Please select a file to upload');
        }
    };

    return (
        <form onSubmit={handleProfilePictureSubmit}>
            <div className="profil-bild" style={{ backgroundImage: `url(${profilePicturePreview || 'https://upload.wikimedia.org/wikipedia/commons/2/2c/Default_pfp.svg'})` }}></div>
            <div className="input-group">
                <label htmlFor="profilePicture">Profilbild</label>
                <input
                    type="file"
                    id="profilePicture"
                    name="profilePicture"
                    accept="image/*"
                    onChange={handleFileChange}
                />
            </div>
            <div className="input-group">
                <label htmlFor="profilePictureURL">Profilbild URL</label>
                <input
                    type="text"
                    id="profilePictureURL"
                    name="profilePictureURL"
                    value={profilePictureURL}
                    onChange={handleProfilePictureURLChange}
                    placeholder="Bild-URL eingeben"
                />
            </div>
            <button type="submit" className="button bg-btn-green">Profilbild hochladen</button>
        </form>
    );
};

export default ProfilePictureUpload;
