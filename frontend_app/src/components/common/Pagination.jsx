import React from 'react';

const Pagination = ({
                        eventsPerPage,
                        totalEvents,
                        itemsPerPage,
                        totalItems,
                        currentPage,
                        paginate
                    }) => {
    const perPage = eventsPerPage || itemsPerPage;
    const total = totalEvents || totalItems;
    const pageNumbers = [];

    for (let i = 1; i <= Math.ceil(total / perPage); i++) {
        pageNumbers.push(i);
    }

    return (
        <div className='pagination-nav'>
            <ul className="pagination-ul">
                {pageNumbers.map((number) => (
                    <li key={number} className="pagination-li">
                        <button
                            onClick={() => paginate(number)}
                            className={`pagination-button ${currentPage === number ? 'current-page' : ''}`}
                        >
                            {number}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Pagination;
