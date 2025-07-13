import React, { useEffect, useState, useCallback } from 'react';
import { Link, useParams } from 'react-router-dom';
import ApiService from '../../../service/ApiService';
import { FaCalendarAlt, FaMapMarkerAlt } from 'react-icons/fa';
import styles from './EventDetailsPage.module.css';

const EventDetailsPage = () => {
    const { eventId } = useParams();

    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [userId, setUserId] = useState(null);
    const [isBooked, setIsBooked] = useState(false);
    const [bookingId, setBookingId] = useState(null);
    const [confirmationCode, setConfirmationCode] = useState('');
    const [timeLeft, setTimeLeft] = useState('');

    const loadEvent = useCallback(async () => {
        try {
            setLoading(true);
            const res = await ApiService.getEventById(eventId);
            const evt = res.event || res;
            setEvent(evt);
            console.log('Aktuelle Bookings nach loadEvent():', evt.bookings);

            const profile = await ApiService.getUserProfile();
            setUserId(profile.user.id);

            const booking = evt.bookings?.find(b => b.user?.id === profile.user.id);
            if (booking) {
                setBookingId(booking.id);
                setConfirmationCode(booking.bookingConfirmationCode);
                setIsBooked(true);
            } else {
                setBookingId(null);
                setConfirmationCode('');
                setIsBooked(false);
            }
        } catch (e) {
            setError(e.response?.data?.message || e.message);
        } finally {
            setLoading(false);
        }
    }, [eventId]);

    useEffect(() => {
        loadEvent();
    }, [loadEvent]);

    useEffect(() => {
        if (!event) return;
        const target = new Date(event.eventDate).getTime();
        const updateTimer = () => {
            const now = Date.now();
            const diff = target - now;
            if (diff <= 0) {
                setTimeLeft('Veranstaltung ist gestartet!');
                clearInterval(timer);
                return;
            }
            const days = Math.floor(diff / 86400000);
            const hrs = Math.floor((diff % 86400000) / 3600000);
            const mins = Math.floor((diff % 3600000) / 60000);
            setTimeLeft(`Es gibt noch: ${days}t ${hrs}s ${mins}m`);
        };
        const timer = setInterval(updateTimer, 60000);
        updateTimer();
        return () => clearInterval(timer);
    }, [event]);

    const handleBooking = async () => {
        if (isBooked) {
            alert('Sie sind bereits angemeldet.');
            return;
        }
        try {
            await ApiService.bookEvent(eventId, userId, {});
            await loadEvent();
        } catch (e) {
            alert('Buchung fehlgeschlagen: ' + (e.response?.data?.message || e.message));
        }
    };

    const handleCancel = async () => {
        if (!bookingId) return;
        try {
            await ApiService.cancelBooking(bookingId);
            alert('Ihre Buchung wurde storniert.');
            await loadEvent();
        } catch (e) {
            alert('Stornierung fehlgeschlagen: ' + (e.response?.data?.message || e.message));
        }
    };

    if (loading) return <p className={styles.message}>Lädt…</p>;
    if (error) return <p className={styles.error}>{error}</p>;
    if (!event) return <p className={styles.error}>Veranstaltung nicht gefunden.</p>;

    const { eventName, eventDate, eventDescription, eventPhotoUrl, eventLocation = {}, categories = [] } = event;
    const { city, street, houseNumber, zip } = eventLocation;
    const address = street && zip && city ? `${street} ${houseNumber}, ${zip} ${city}` : 'Ort nicht verfügbar';
    const formattedDate = new Date(eventDate).toLocaleDateString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric' });

    return (
        <div className={styles.outerContainer}>
            <div className={styles.page}>
                <nav className={styles.breadcrumb}>
                    <Link to="/">Home</Link> / <Link to="/events">Veranstaltungen</Link> / <span>{eventName}</span>
                </nav>

                <div
                    className={styles.hero}
                    style={{ backgroundImage: `url(${eventPhotoUrl || 'data:image/jpeg;base64,' + event.eventPhoto})` }}
                >
                    <div className={styles.overlay}>
                        <h1 className={styles.heroTitle}>{eventName}</h1>
                        <p className={styles.heroMeta}>
                            <FaCalendarAlt /> {formattedDate} &nbsp;&nbsp; <FaMapMarkerAlt /> {address}
                        </p>
                        <p className={styles.countdown}>{timeLeft}</p>
                        {categories.length > 0 && (
                            <div className={styles.categories}>
                                {categories.map(cat => (
                                    <span key={cat.name} className={styles.categoryTag}>{cat.name}</span>
                                ))}
                            </div>
                        )}
                    </div>
                </div>

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

                {!isBooked ? (
                    <div className={styles.stickyFooter}>
                        <button className={styles.bookBtn} onClick={handleBooking}>Jetzt buchen</button>
                    </div>
                ) : (
                    <div className={styles.confirmation}>
                        <p>Sie sind angemeldet!</p>
                        <p><strong>Buchungs-Code:</strong> {confirmationCode}</p>
                        <button className={styles.cancelBtn} onClick={handleCancel}>Stornieren</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default EventDetailsPage;
