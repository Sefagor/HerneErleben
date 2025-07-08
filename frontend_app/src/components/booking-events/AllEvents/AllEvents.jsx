// frontend_app/src/pages/AllEventsPage.jsx
import React, {useEffect, useState} from 'react';
import ApiService from '../../../service/ApiService';
import Pagination from '../../common/Pagination';
import EventCard from '../../common/EventCard/EventCard';
import EventSearch from '../../common/EventSearch/EventSearch';
import styles from './AllEvents.module.css';

const AllEventsPage = () => {
    const [events, setEvents] = useState([]);
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const eventsPerPage = 10;

    const handleSearchResult = (results) => {
        setEvents(results);
        setFilteredEvents(results);
        setSelectedCategory('');
        setCurrentPage(1);
    };

    useEffect(() => {
        ApiService.getAllEvents()
            .then(data => {
                const list = Array.isArray(data) ? data : (data.eventList || []);
                setEvents(list);
                setFilteredEvents(list);
            })
            .catch(console.error);
    }, []);
    console.log(events)
    const lastIdx = currentPage * eventsPerPage;
    const firstIdx = lastIdx - eventsPerPage;
    const currentEvents = filteredEvents.slice(firstIdx, lastIdx);

    // debug
    console.log('total events:', filteredEvents.length);

    return (
        <div className={styles.allEventsPage}>
            <div className={styles.titleBlock}>
                <h2 className={styles.title}>Veranstaltungen</h2>
            </div>

            <div className={styles.controls}>
                <EventSearch inline handleSearchResult={handleSearchResult}/>
            </div>

            <div className={styles.eventGrid}>
                {currentEvents.map((ev, idx) => (
                    <div
                        key={ev.id}
                        className={styles.cardWrapper}
                        style={{animationDelay: `${idx * 100}ms`}}
                    >
                        <EventCard event={ev}/>
                    </div>
                ))}
            </div>

            <div className={styles.paginationWrapper}>
                {filteredEvents.length > 0 && (
                    <Pagination
                        itemsPerPage={eventsPerPage}
                        totalItems={filteredEvents.length}
                        currentPage={currentPage}
                        paginate={setCurrentPage}
                    />
                )}
            </div>
        </div>
    );
};

export default AllEventsPage;
