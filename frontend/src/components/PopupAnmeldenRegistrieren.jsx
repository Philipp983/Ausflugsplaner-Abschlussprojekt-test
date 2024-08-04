import React from 'react';
import '../styles/popup.css';
import Button from '../utils/Button';

const Popup = ({description, onCancel, onConfirm, cancelText = "Cancel", confirmText = "Delete" }) => {
    return (
        <div className="popup-overlay">
            <div className="popup-content">
                <p>Bitte melde dich an oder registriere dich, um den Ausflug anzuschauen</p>
                <div className="popup-buttons">
                    <Button
                        text={confirmText}
                        onClick={onConfirm}
                        color="var(--main-green-color)"
                    />
                    <Button
                        className=""
                        text={cancelText}
                        onClick={onCancel}
                        color="var(--main-gold-color)"
                    />

                </div>
            </div>
        </div>
    );
};

export default Popup;
