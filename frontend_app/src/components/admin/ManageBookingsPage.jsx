import React, { useState, useEffect } from 'react';
import ApiService from '../../service/ApiService';
import { format } from 'date-fns'; // schöneres Date-Format
import AdminLayout from '../admin/AdminLayout';


function ManageBookingsPage() {
    const [bookings, setBookings] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        fetchBookings();
    }, []);

    useEffect(() => {
        console.log("📦 Booking listesi:", bookings);
    }, [bookings]);


    const fetchBookings = async () => {
        try {
            const response = await ApiService.getAllBookings();
            setBookings(response.bookingList || response); // fallback falls API nur Array liefert
        } catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    const handleCancel = async (bookingId) => {
        if (!window.confirm('Do you really want to cancel this booking?')) return;

        try {
            const response = await ApiService.cancelBooking(bookingId);
            if (response.statusCode === 200) {
                setSuccessMessage('Booking cancelled successfully');
                fetchBookings(); // Reload bookings
                setTimeout(() => setSuccessMessage(''), 5000);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    return (
        <AdminLayout>
        <div className="manage-bookings-container">
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            <h2>Manage Bookings</h2>

            <table className="bookings-table">
                <thead>
                    <tr>
                        <th>Confirmation Code</th>
                        <th>Booking Date</th>
                        <th>User Name</th>
                        <th>User Email</th>
                        <th>Event Name</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {bookings.length === 0 ? (
                        <tr>
                            <td colSpan="7">No bookings found.</td>
                        </tr>
                    ) : (
                        bookings.map(booking => (
                            <tr key={booking.id}>
                                <td>{booking.bookingConfirmationCode}</td>
                                <td>{format(new Date(booking.bookingDate), 'yyyy-MM-dd')}</td>
                                <td>{booking.user?.name}</td>
                                <td>{booking.user?.email}</td>
                                <td>{booking.event?.eventName}</td>
                                <td>{booking.status}</td>
                                <td>
                                    {booking.status === 'ACTIVE' && (
                                        <button onClick={() => handleCancel(booking.id)}>Cancel</button>
                                    )}
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
        </AdminLayout>
    );
}

export default ManageBookingsPage;
