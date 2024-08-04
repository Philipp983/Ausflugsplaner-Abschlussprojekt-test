import React from 'react';
import { FaChevronLeft, FaChevronRight } from 'react-icons/fa';
import '../styles/pagination.css';

const Pagination = ({ ausfluegePerPage, totalAusfluege, paginate, currentPage }) => {
    const pageNumbers = [];
    for (let i = 1; i <= Math.ceil(totalAusfluege / ausfluegePerPage); i++) {
        pageNumbers.push(i);
    }

    const totalPages = pageNumbers.length;
    const maxPageNumbers = 5; // Total number of page buttons to display, including first and last

    let pageRange = [];
    if (totalPages <= maxPageNumbers) {
        pageRange = pageNumbers;
    } else {
        const middlePages = maxPageNumbers - 2; // Subtract 2 for first and last page
        let startPage = Math.max(2, currentPage - Math.floor(middlePages / 2));
        let endPage = Math.min(totalPages - 1, startPage + middlePages - 1);

        if (endPage - startPage + 1 < middlePages) {
            startPage = Math.max(2, endPage - middlePages + 1);
        }

        pageRange = [1, ...Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i), totalPages];
    }

    const handlePrevious = () => {
        if (currentPage > 1) paginate(currentPage - 1);
    };

    const handleNext = () => {
        if (currentPage < totalPages) paginate(currentPage + 1);
    };

    return (
        <nav className="pagination">
            <ul>
                <li>
                    <button onClick={handlePrevious} disabled={currentPage === 1} className="pagination-arrow">
                        <FaChevronLeft />
                    </button>
                </li>
                {pageRange.map((number, index) => (
                    <React.Fragment key={number}>
                        {index > 0 && number - pageRange[index - 1] > 1 && <li className="pagination-ellipsis">...</li>}
                        <li
                            className={`pagination-item ${currentPage === number ? 'active' : ''}`}
                            onClick={() => paginate(number)}
                        >
                            {number}
                        </li>
                    </React.Fragment>
                ))}
                <li>
                    <button onClick={handleNext} disabled={currentPage === totalPages} className="pagination-arrow">
                        <FaChevronRight />
                    </button>
                </li>
            </ul>
        </nav>
    );
};

export default Pagination;
