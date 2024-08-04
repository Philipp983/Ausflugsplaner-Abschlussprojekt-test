import React from 'react';
import { Triangle } from 'react-loader-spinner';

const TriangleSpinner = ({ visible }) => {
    return (
        <div className="spinner-container">
        <Triangle
            visible={visible}
            marginLeft="50vw"
            height="80"
            width="80"
            color="#a38dbb"
            ariaLabel="triangle-loading"
            wrapperStyle={{}}
            wrapperClass=""
        />
        </div>
    );
};

export default TriangleSpinner;
