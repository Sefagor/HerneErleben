// src/components/ProfilePage/ProfilePage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../../service/ApiService';
import EventCard from '../../common/EventCard/EventCard';
import styles from './ProfilePage.module.css';

const ProfilePage = () => {
    const [user, setUser]   = useState(null);
    const [error, setError] = useState(null);
    const navigate          = useNavigate();

    useEffect(() => {
        const fetchUserWithBookings = async () => {
            try {
                const profileRes = await ApiService.getUserProfile();
                const profile    = profileRes.user;

                const bookingsRes = await ApiService.getUserBookings(profile.id);
                const rawBookings = bookingsRes.user?.bookings ?? [];

                const bookings = rawBookings.map(b => ({
                    ...b,
                    event: {
                        ...b.event,
                        // categories undefined ise boş dizi ata
                        categories: Array.isArray(b.event.categories)
                            ? b.event.categories
                            : []
                    }
                }));

                setUser({
                    ...profile,
                    bookings
                });
            } catch (err) {
                setError(err.response?.data?.message || err.message);
            }
        };

        fetchUserWithBookings();
    }, []);

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    if (error) return <p className={styles.errorMessage}>{error}</p>;
    if (!user)  return <p>Lädt…</p>;

    const { name, email, phoneNumber, bookings } = user;

    return (
        <div className={styles.profilePage}>
            {/* Header Card */}
            <div className={styles.headerCard}>
                <div className={styles.headerInfo}>
                    <h2 className={styles.welcome}>Hallo, {name}</h2>
                    <p className={styles.subtitle}>Schön, dich wiederzusehen!</p>
                </div>
                <button
                    className={styles.editProfileButton}
                    onClick={handleEditProfile}
                >
                    Profil bearbeiten
                </button>
            </div>

            {/* Profile Details */}
            <div className={styles.profileDetailsCard}>
                <h3 className={styles.cardTitle}>Meine Profildaten</h3>
                <dl className={styles.detailsList}>
                    <dt>Email:</dt>
                    <dd>{email}</dd>
                    <dt>Telefon:</dt>
                    <dd>{phoneNumber}</dd>
                </dl>
            </div>

            {/* Bookings */}
            <div className={styles.bookingsSection}>
                <h3 className={styles.cardTitle}>Meine Buchungen</h3>
                {bookings.length > 0 ? (
                    <div className={styles.bookingsList}>
                        {bookings.map(booking => (
                            <EventCard
                                key={booking.id}
                                event={booking.event}
                            />
                        ))}
                    </div>
                ) : (
                    <p className={styles.emptyMessage}>Du hast aktuell keine Buchungen.</p>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;
