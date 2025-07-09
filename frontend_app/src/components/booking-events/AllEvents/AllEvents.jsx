import React, {useEffect, useState} from 'react';
import ApiService from '../../../service/ApiService';
import Pagination from '../../common/Pagination';
import EventCard from '../../common/EventCard/EventCard';
import EventSearch from '../../common/EventSearch/EventSearch';
import styles from './AllEvents.module.css';

const AllEventsPage = () => {
    // Ursprüngliche Liste aller Events
    const [allEvents, setAllEvents] = useState([]);
    // Gefilterte Events
    const [filteredEvents, setFilteredEvents] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const eventsPerPage = 10;

    // Backend beim ersten Laden alle Events abrufen
    useEffect(() => {
        ApiService.getAllEvents()
            .then(data => {
                const list = Array.isArray(data) ? data : (data.eventList || []);
                setAllEvents(list);
                setFilteredEvents(list);
            })
            .catch(console.error);
    }, []);

    // Funktion zur Aktualisierung der Suchergebnisse
    const handleSearchResult = (results) => {
        // Beim neuen Filter immer von der vollständigen Liste starten
        setFilteredEvents(results);
        setCurrentPage(1);
    };

    // Pagination-Berechnungen
    const lastIdx = currentPage * eventsPerPage;
    const firstIdx = lastIdx - eventsPerPage;
    const currentEvents = filteredEvents.slice(firstIdx, lastIdx);

    return (
        <div className={styles.allEventsPage}>
            <div className={styles.titleBlock}>
                <h2 className={styles.title}>Veranstaltungen</h2>
            </div>

            <div className={styles.controls}>
                {/* Für Inline-Suche immer alle Events übergeben */}
                <EventSearch
                    inline
                    events={allEvents}
                    handleSearchResult={handleSearchResult}
                />
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
