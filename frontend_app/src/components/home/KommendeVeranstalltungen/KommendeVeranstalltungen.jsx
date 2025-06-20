import React, {useEffect, useState} from 'react';
import ApiService from '../../../service/ApiService';
import styles from './KommendeVeranstalltungen.module.css';
import EventCard from '../../common/EcentCard/EventCard';

const UpcomingEventsSection = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        ApiService.getAllEvents()
            .then(data => {
                const list = Array.isArray(data)
                    ? data
                    : Array.isArray(data.eventList)
                        ? data.eventList
                        : [];

                // Heutiges Datum (YYYY-MM-DD) erhalten
                const today = new Date().toISOString().slice(0, 10);
                const todayDate = new Date(today);

                // Veranstaltungen ab heute filtern, nach Datum sortieren und maximal 10 anzeigen
                const upcoming = list
                    .map(evt => ({
                        ...evt,
                        parsedDate: new Date((evt.eventDate || evt.date || '').slice(0, 10))
                    }))
                    .filter(evt => evt.parsedDate >= todayDate)
                    .sort((a, b) => a.parsedDate - b.parsedDate)
                    .slice(0, 10)
                    .map(({parsedDate, ...rest}) => rest); // parsedDate vor der Ausgabe entfernen

                setEvents(upcoming);
            })
            .catch(err => console.error(err));
    }, []);

    return (
        <section className={styles.container}>
            <div className={styles.titleBlock}>
                <h2 className={styles.title}>Kommende Veranstaltungen</h2>
            </div>
            {events.length === 0 ? (
                <p className={styles.noEvents}>Keine kommenden Veranstaltungen.</p>
            ) : (
                <div className={styles.grid}>
                    {events.map(evt => (
                        <EventCard key={evt.id} event={evt}/>
                    ))}
                </div>
            )}
        </section>
    );
};

export default UpcomingEventsSection;