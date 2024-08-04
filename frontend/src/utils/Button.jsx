import React from 'react';
import '../styles/button.css';

const Button = ({ text, icon: Icon, onClick, color }) => {
    return (
        <button
            onClick={onClick}
            style={{ backgroundColor: color }}
            className="custom-button"
        >
            {Icon && <Icon className="icon" />}
            {text}
        </button>
    );
};

export default Button;
