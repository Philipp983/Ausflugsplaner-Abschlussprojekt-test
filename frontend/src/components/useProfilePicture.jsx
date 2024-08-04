import { useState, useEffect } from 'react';

const useProfilePicture = (userId, triggerUpdate) => {
    const [profilePicturePreview, setProfilePicturePreview] = useState(null);
    const baseUrl = import.meta.env.VITE_BASE_URL;

    useEffect(() => {

        const fetchProfilePicture = async () => {
            try {
                const response = await fetch(`${baseUrl}/api/profilbild/benutzer/${userId}`);
                if (response.ok) {
                    const contentType = response.headers.get('content-type');
                    if (contentType && contentType.startsWith('image/')) {
                        const blob = await response.blob();
                        const objectURL = URL.createObjectURL(blob);
                        setProfilePicturePreview(objectURL);
                        return () => URL.revokeObjectURL(objectURL);
                    } else {
                        const imageUrl = await response.text();
                        setProfilePicturePreview(imageUrl);
                    }
                } else {
                    console.error('Error loading profile picture');
                }
            } catch (error) {
                console.error('Error fetching profile picture:', error);
            }
        };

        fetchProfilePicture();
    }, [userId, triggerUpdate]);

    return profilePicturePreview;
};

export default useProfilePicture;
