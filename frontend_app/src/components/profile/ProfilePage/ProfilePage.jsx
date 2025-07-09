// ProfilePage.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../../service/ApiService';
import styles from './ProfilePage.module.css';

const ProfilePage = () => {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await ApiService.getUserProfile();
                const userPlusBookings = await ApiService.getUserBookings(response.user.id);
                setUser(userPlusBookings.user);
            } catch (err) {
                setError(err.response?.data?.message || err.message);
            }
        };
        fetchUserProfile();
    }, []);

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    return (
        <div className={styles.profilePage}>
            {user && <h2 className={styles.welcome}>Welcome, {user.name}</h2>}

            <div className={styles.profileActions}>
                <button
                    className={styles.editProfileButton}
                    onClick={handleEditProfile}
                >
                    Edit Profile
                </button>
            </div>

            {error && <p className={styles.errorMessage}>{error}</p>}

            {user && (
                <div className={styles.profileDetails}>
                    <h3>My Profile Details</h3>
                    <p><strong>Email:</strong> {user.email}</p>
                    <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
                </div>
            )}

            <div className={styles.bookingsSection}>
                <h3>My Booking History</h3>
                {/* Booking kartları buraya gelecek */}
            </div>
        </div>
    );
};

export default ProfilePage;
