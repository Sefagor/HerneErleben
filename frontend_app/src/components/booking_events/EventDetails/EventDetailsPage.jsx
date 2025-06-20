import React, {useEffect, useState} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import ApiService from '../../../service/ApiService';
import {FaCalendarAlt, FaMapMarkerAlt} from 'react-icons/fa';
import styles from './EventDetailsPage.module.css';

const EventDetailsPage = () => {
    const {eventId} = useParams();
    const navigate = useNavigate();

    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [userId, setUserId] = useState(null);
    const [isBooked, setIsBooked] = useState(false);
    const [confirmationCode, setConfirmationCode] = useState('');
    const [timeLeft, setTimeLeft] = useState('');

    // Detay + profil yükle
    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                const res = await ApiService.getEventById(eventId);
                const evt = res.event || res;
                setEvent(evt);
                const profile = await ApiService.getUserProfile();
                setUserId(profile.user.id);
            } catch (e) {
                setError(e.response?.data?.message || e.message);
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [eventId]);

    // Geri sayım sayaç
    useEffect(() => {
        if (!event) return;
        const target = new Date(event.eventDate).getTime();
        const tick = () => {
            const now = Date.now();
            const diff = target - now;
            if (diff <= 0) {
                setTimeLeft('Veranstalltung ist gestartet!');
                return clearInterval(timer);
            }
            const days = Math.floor(diff / 86400000);
            const hrs = Math.floor((diff % 86400000) / 3600000);
            const mins = Math.floor((diff % 3600000) / 60000);
            setTimeLeft(`Es gibt noch: ${days}t ${hrs}s ${mins}m`);
        };
        const timer = setInterval(tick, 60000);
        tick();
        return () => clearInterval(timer);
    }, [event]);

    const handleBooking = async () => {
        try {
            const res = await ApiService.bookEvent(eventId, userId, {});
            setConfirmationCode(res.bookingConfirmationCode);
            setIsBooked(true);
        } catch (e) {
            alert('Fehler bei der Buchung: ' + (e.response?.data?.message || e.message));
        }
    };

    if (loading) return <p className={styles.message}>Lädt…</p>;
    if (error) return <p className={styles.error}>{error}</p>;
    if (!event) return <p className={styles.error}>Veranstaltung nicht gefunden.</p>;

    // Alanlar
    const {
        eventName,
        eventDate,
        eventDescription,
        eventPhotoUrl,
        eventLocation = {}
    } = event;
    const {city, street, houseNumber, zip} = eventLocation;
    const address = street && zip && city
        ? `${street} ${houseNumber}, ${zip} ${city}`
        : 'Ort nicht verfügbar';
    const formattedDate = new Date(eventDate).toLocaleDateString('de-DE', {
        day: '2-digit', month: '2-digit', year: 'numeric'
    });

    return (
        <div className={styles.outerContainer}>
            <div className={styles.page}>

                {/* Breadcrumb */}
                <nav className={styles.breadcrumb}>
                    <Link to="/">Home</Link> / <Link to="/events">Veranstaltungen</Link> / <span>{eventName}</span>
                </nav>

                {/* Hero */}
                <div
                    className={styles.hero}
                    style={{backgroundImage: `url(${eventPhotoUrl || 'data:image/jpeg;base64,' + event.eventPhoto})`}}
                >
                    <div className={styles.overlay}>
                        <h1 className={styles.heroTitle}>{eventName}</h1>
                        <p className={styles.heroMeta}>
                            <FaCalendarAlt/> {formattedDate} &nbsp;&nbsp;
                            <FaMapMarkerAlt/> {address}
                        </p>
                        <p className={styles.countdown}>{timeLeft}</p>
                    </div>
                </div>

                {/* İçerik */}
                <div className={styles.contentGrid}>
                    <div className={styles.description}>
                        <h2>Über das Event</h2>
                        <p>{eventDescription || 'Keine Beschreibung verfügbar.'}</p>
                    </div>
                    <div className={styles.map}>
                        <h2>Ort auf der Karte</h2>
                        {address !== 'Ort nicht verfügbar' ? (
                            <iframe
                                title="map"
                                src={`https://www.google.com/maps?q=${encodeURIComponent(address)}&output=embed`}
                                allowFullScreen
                            />
                        ) : (
                            <p>Keine Karte verfügbar.</p>
                        )}
                    </div>
                </div>


                {/* Sticky Book Button */}
                {!isBooked && (
                    <div className={styles.stickyFooter}>
                        {!isBooked && (
                            <button className={styles.bookBtn} onClick={handleBooking}>
                                Jetzt buchen
                            </button>
                        )}
                    </div>
                )}

                {isBooked && (
                    <div className={styles.confirmation}>
                        <p>Sie sind angemeldet!</p>
                        <p><strong>Buchungs-Code:</strong> {confirmationCode}</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default EventDetailsPage;
