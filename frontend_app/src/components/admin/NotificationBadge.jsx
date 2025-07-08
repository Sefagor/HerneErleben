import React, {useEffect, useState} from 'react';
import ApiService from '../../service/ApiService';
import {format} from 'date-fns';
import '../../service/NotificationBadge.css';

function NotificationBadge() {
    const [newCount, setNewCount] = useState(0);
    const [cancelledCount, setCancelledCount] = useState(0);

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = async () => {
        try {
            const response = await ApiService.getAllBookings();
            const bookingList = response.bookingList || response;

            const todayStr = format(new Date(), 'yyyy-MM-dd');

            const newBookings = bookingList.filter(booking =>
                booking.status === 'ACTIVE' &&
                format(new Date(booking.bookingDate), 'yyyy-MM-dd') === todayStr
            );

            const cancelledBookings = bookingList.filter(booking =>
                booking.status === 'CANCELLED' &&
                format(new Date(booking.bookingDate), 'yyyy-MM-dd') === todayStr
            );

            setNewCount(newBookings.length);
            setCancelledCount(cancelledBookings.length);

        } catch (error) {
            console.error('Error fetching bookings:', error.message);
        }
    };

    return (
        <div className="notification-badge-container">
            <div className="badge new-bookings">
                🔔 New: {newCount}
            </div>
            <div className="badge cancelled-bookings">
                ❌ Cancelled: {cancelledCount}
            </div>
        </div>
    );
}

export default NotificationBadge;
