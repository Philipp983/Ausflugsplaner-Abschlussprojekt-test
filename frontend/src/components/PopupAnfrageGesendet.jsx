import React from 'react';
import '../styles/popup.css';
import Button from '../utils/Button';

const Popup = ({description, onConfirm}) => {
    return (
        <div className="popup-overlay">
            <div className="popup-content max-height">
                <p>{description}</p>
                <div className="popup-buttons">
                    <Button
                        text="Ok"
                        onClick={onConfirm}
                        color="var(--main-green-color)"
                    />

                </div>
            </div>
        </div>
    );
};

export default Popup;
