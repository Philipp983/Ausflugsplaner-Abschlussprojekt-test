import React, { useState } from 'react';
import axios from 'axios';

const BildHochladen = ({ ausflugId, maxFileSize, onImageUpload }) => {
  const [isFileUpload, setIsFileUpload] = useState(true);
  const [file, setFile] = useState(null);
  const [imageUrl, setImageUrl] = useState('');  // Initialize with an empty string
  const [errorMessage, setErrorMessage] = useState('');
  const baseUrl = import.meta.env.VITE_BASE_URL;

  const handleToggle = () => {
    setIsFileUpload(!isFileUpload);
    setFile(null);
    setImageUrl('');
    setErrorMessage('');
  };

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile && selectedFile.size > maxFileSize) {
      setErrorMessage(`File size exceeds the limit of ${maxFileSize / 1024 / 1024} MB`);
      setFile(null);
    } else {
      setFile(selectedFile);
      setErrorMessage('');
    }
  };

  const handleUrlChange = (event) => {
    setImageUrl(event.target.value);
    setErrorMessage('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (isFileUpload) {
      if (file) {
        const formData = new FormData();
        formData.append('image', file);

        try {
          const response = await axios.post(`${baseUrl}/api/ausflugbild/ausflug/${ausflugId}/upload`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
          console.log('Image file uploaded:', response.data);
          onImageUpload(URL.createObjectURL(file));
          setFile(null);
        } catch (error) {
          console.error('Error uploading image file:', error);
          setErrorMessage('Error uploading image file');
          setFile(null);
        }
      } else {
        setErrorMessage('Please select a file to upload');
      }
    } else {
      if (imageUrl) {
        try {
          const response = await axios.post(`${baseUrl}/api/ausflugbild/ausflug/${ausflugId}/url`, null, {
            params: { imageUrl }
          });
          // console.log('Image URL uploaded:', response.data);
          onImageUpload(imageUrl);
          setImageUrl('');
        } catch (error) {
          console.error('Fehler beim hochladen der URL:', error);
          setErrorMessage('Fehler beim hochladen der URL');
        }
      } else {
        setErrorMessage('Bitte eine URL angeben');
      }
    }
  };

  return (
      <div className="margin-top">
        <div>
          <button type="button" onClick={handleToggle}>
            {isFileUpload ? 'Zur URL wechseln' : 'Zum Datei-Upload wechseln'}
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          {isFileUpload ? (
              <div>
                <input
                    className="fotos-zum-auswaehlen"
                    type="file"
                    accept="image/*,video/*"
                    onChange={handleFileChange}
                />
              </div>
          ) : (
              <div>
                <input
                    type="text"
                    value={imageUrl}
                    onChange={handleUrlChange}
                    placeholder="Bild-URL eingeben"
                />
              </div>
          )}
          <button className="button bg-btn-green margin-top-20" type="submit">Speichern</button>
        </form>
        {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      </div>
  );
};

export default BildHochladen;
