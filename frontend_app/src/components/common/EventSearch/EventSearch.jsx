import React, { useState } from 'react';
import ApiService from '../../../service/ApiService';
import styles from './EventSearch.module.css';

const EventSearch = ({ handleSearchResult, inline = false, events = [] }) => {
    const [location, setLocation] = useState('');
    const [category, setCategory] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');

    const onSearch = async (e) => {
        e.preventDefault();

        if (inline) {
            // Frontend-side filtering
            const filtered = events.filter((ev) => {
                const evDate = new Date(ev.eventDate);
                const from = fromDate ? new Date(fromDate) : null;
                const to = toDate ? new Date(toDate) : null;

                const inDateRange =
                    (!from || evDate >= from) &&
                    (!to || evDate <= to);

                const inLocation = location
                    ? ev.eventLocation?.name
                        .toLowerCase()
                        .includes(location.toLowerCase())
                    : true;

                const inCategory = category
                    ? ev.categories?.some((cat) =>
                        cat.name.toLowerCase().includes(category.toLowerCase())
                    )
                    : true;

                return inDateRange && inLocation && inCategory;
            });

            handleSearchResult(filtered);
        } else {
            // Server-side search
            try {
                const params = {};
                if (location) params.location = location;
                if (category) params.category = category;
                if (fromDate) params.from = fromDate;
                if (toDate) params.to = toDate;

                const response = await ApiService.searchEvents(params);
                const list = Array.isArray(response)
                    ? response
                    : response.eventList || [];
                handleSearchResult(list);
            } catch (error) {
                console.error('Search error:', error);
            }
        }
    };

    return (
        <form className={styles.searchForm} onSubmit={onSearch}>
            <input
                type="text"
                placeholder="Location"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
            />
            <input
                type="text"
                placeholder="Category"
                value={category}
                onChange={(e) => setCategory(e.target.value)}
            />
            <input
                type="date"
                placeholder="From"
                value={fromDate}
                onChange={(e) => setFromDate(e.target.value)}
            />
            <input
                type="date"
                placeholder="To"
                value={toDate}
                onChange={(e) => setToDate(e.target.value)}
            />
            <button type="submit">Search</button>
        </form>
    );
};

export default EventSearch;
