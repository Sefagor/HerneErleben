import React from 'react';
import {NavLink} from 'react-router-dom';
import styles from './EventCard.module.css';
import {FaCalendarAlt, FaMapMarkerAlt, FaRegCalendarPlus} from 'react-icons/fa';

// Statusfarben
const statusColors = {
    ACTIVE: 'green',
    CANCELLED: 'red',
    EXPIRED: 'yellow',
};

// Wandelt ISO-Datum für Google Kalender um (YYYYMMDDTHHMMSSZ)
const formatForGoogle = date =>
    date.toISOString().replace(/[-:]/g, '').split('.')[0] + 'Z';

// Erzeugt Google Kalender-URL
const getGoogleCalendarUrl = ({title, description, location, startDate, endDate}) => {
    const params = new URLSearchParams({
        action: 'TEMPLATE',
        text: title,
        details: description,
        location,
        dates: `${formatForGoogle(new Date(startDate))}/${formatForGoogle(new Date(endDate))}`,
    });
    return `https://calendar.google.com/calendar/render?${params.toString()}`;
};

const EventCard = ({event}) => {
    const {
        id,
        eventName,
        eventDescription,
        eventDate,
        eventLocation,
        eventPhoto,
        status,
        categories,
    } = event;

    console.log(`EventCard ${id} categories:`, categories);


    // Start- und Enddatum (Standard: 2-stündiges Event)
    const startDate = eventDate;
    const endDate = new Date(new Date(eventDate).getTime() + 2 * 60 * 60 * 1000).toISOString();
    const calendarUrl = getGoogleCalendarUrl({
        title: eventName,
        description: eventDescription,
        location: eventLocation?.city || '',
        startDate,
        endDate,
    });

    // Datum formatieren (de-DE)
    const formattedDate = new Date(eventDate).toLocaleDateString('de-DE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });

    return (
        <div className={styles.card}>
            <div className={styles.imageWrapper}>
                <img
                    src={"data:image/jpeg;base64," + eventPhoto}
                    alt={eventName}
                    className={styles.image}
                />
                <div className={styles.overlay}>
                    <p className={styles.description}>{eventDescription}</p>
                    <div className={styles.meta}>
                        <span><FaCalendarAlt/> {formattedDate}</span>
                        {eventLocation?.city && (
                            <span><FaMapMarkerAlt/> {eventLocation.city}</span>
                        )}
                    </div>

                    {/* Kategorien anzeigen */}
                    {categories.length > 0 && (
                        <div className={styles.categories}>
                            {categories.map(cat => (
                                <span key={cat.name} className={styles.categoryTag}>
                  {cat.name}
                </span>
                            ))}
                        </div>
                    )}

                    <NavLink to={`/events/${id}`} className={styles.button}>
                        Mehr erfahren →
                    </NavLink>
                </div>
            </div>

            <div className={styles.info}>
                <div className={styles.upperContainer}>
                    <h3 className={styles.title}>{eventName}</h3>
                    <div className={styles.status}>
            <span
                className={styles.statusDot}
                style={{backgroundColor: statusColors[status] || 'gray'}}
            />
                        <span className={styles.statusText}>{status}</span>
                    </div>
                </div>
                <a
                    href={calendarUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className={styles.addCalendarButton}
                >
                    <FaRegCalendarPlus className={styles.calendarIcon}/>
                    Zum Kalender hinzufügen
                </a>
            </div>
        </div>
    );
};

export default EventCard;
