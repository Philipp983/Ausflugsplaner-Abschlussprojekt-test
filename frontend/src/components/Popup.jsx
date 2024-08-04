import React from 'react';
import '../styles/popup.css';
import Button from '../utils/Button';

const Popup = ({ title, description, onCancel, onConfirm, cancelText = "Cancel", confirmText = "Delete" }) => {
    return (
        <div className="popup-overlay">
            <div className="popup-content">
                <p>{description}</p>
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
                        color="var(--main-lavender-color)"
                        icon={() => <span className="icon-cancel">✖</span>}
                    />

                </div>
            </div>
        </div>
    );
};

export default Popup;
