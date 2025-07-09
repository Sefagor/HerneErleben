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

    useEffect(() => {
        async function load() {
            try {
                setLoading(true);
                const res = await ApiService.getEventById(eventId);
                const evt = res.event || res;
                console.log('Loaded event:', evt);
                setEvent(evt);

                const profile = await ApiService.getUserProfile();
                console.log('Current user profile:', profile);
                setUserId(profile.user.id);

                // Rezervasyon kontrolü: kullanıcı daha önce rezervasyon yaptı mı?
                if (evt.bookings && evt.bookings.some(b => b.user?.id === profile.user.id)) {
                    console.log('User has already booked this event');
                    const userBooking = evt.bookings.find(b => b.user?.id === profile.user.id);
                    setConfirmationCode(userBooking?.bookingConfirmationCode || '');
                    setIsBooked(true);
                } else {
                    console.log('User has not booked yet');
                }
            } catch (e) {
                setError(e.response?.data?.message || e.message);
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [eventId]);

    useEffect(() => {
        if (!event) return;
        const target = new Date(event.eventDate).getTime();
        const tick = () => {
            const now = Date.now();
            const diff = target - now;
            if (diff <= 0) {
                setTimeLeft('Veranstaltung ist gestartet!');
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
            console.log('Booking response:', res);
            setConfirmationCode(res.bookingConfirmationCode);
            setIsBooked(true);
        } catch (e) {
            alert('Fehler bei der Buchung: ' + (e.response?.data?.message || e.message));
        }
    };

    if (loading) return <p className={styles.message}>Lädt…</p>;
    if (error) return <p className={styles.error}>{error}</p>;
    if (!event) return <p className={styles.error}>Veranstaltung nicht gefunden.</p>;

    const {
        eventName,
        eventDate,
        eventDescription,
        eventPhotoUrl,
        eventLocation = {},
        categories = []
    } = event;
    const {city, street, houseNumber, zip} = eventLocation;
    const address = street && zip && city
        ? `${street} ${houseNumber}, ${zip} ${city}`
        : 'Ort nicht verfügbar';
    const formattedDate = new Date(eventDate).toLocaleDateString('de-DE', {
        day: '2-digit', month: '2-digit', year: 'numeric'
    });

    console.log('isBooked state:', isBooked, 'confirmationCode:', confirmationCode);

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


                    </div>
                </div>

                {/* content */}
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
                        <button className={styles.bookBtn} onClick={handleBooking}>
                            Jetzt buchen
                        </button>
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
