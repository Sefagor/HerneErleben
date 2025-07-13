import React, { useEffect, useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import ApiService from '../../service/ApiService';
import { format, parseISO, isValid } from 'date-fns';
import AdminLayout from '../admin/AdminLayout';

function BookingCalendarPage() {
    const [bookings, setBookings] = useState([]);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [bookingsPerDay, setBookingsPerDay] = useState({});

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = async () => {
        try {
            const response = await ApiService.getAllBookings();
            const bookingList = response.bookingList || response;

            const enrichedBookings = await Promise.all(
                bookingList.map(async (booking) => {
                    try {
                        const detailed = await ApiService.getBookingByConfirmationCode(booking.bookingConfirmationCode);
                        return detailed.booking || booking;
                    } catch (err) {
                        console.warn("Detaildaten konnten nicht geladen werden:", err);
                        return booking;
                    }
                })
            );

            const counts = {};
            enrichedBookings.forEach(booking => {
                const rawDate = booking.event?.eventDate;
                const parsed = typeof rawDate === 'string' ? parseISO(rawDate) : new Date(rawDate);
                if (isValid(parsed)) {
                    const dateStr = format(parsed, 'yyyy-MM-dd');
                    counts[dateStr] = (counts[dateStr] || 0) + 1;
                }
            });

            setBookings(enrichedBookings);
            setBookingsPerDay(counts);
        } catch (error) {
            console.error('Error fetching bookings:', error.message);
        }
    };

    const tileContent = ({ date, view }) => {
        if (view === 'month') {
            const dateStr = format(date, 'yyyy-MM-dd');
            const count = bookingsPerDay[dateStr] || 0;
            return count > 0 ? (
                <div style={{ marginTop: 5, fontSize: '0.75em', backgroundColor: '#d1fae5', padding: '2px 6px', borderRadius: '4px', color: '#065f46' }}>
                    {count} Booking{count > 1 ? 's' : ''}
                </div>
            ) : null;
        }
    };

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    const selectedDateStr = format(selectedDate, 'yyyy-MM-dd');

    const bookingsForSelectedDate = bookings.filter(booking => {
        const rawDate = booking.event?.eventDate;
        const parsed = typeof rawDate === 'string' ? parseISO(rawDate) : new Date(rawDate);
        return isValid(parsed) && format(parsed, 'yyyy-MM-dd') === selectedDateStr;
    });

    return (
        <AdminLayout>
            <div className="booking-calendar-container" style={{ padding: '2rem' }}>
                <h2 style={{ fontSize: '1.75rem', marginBottom: '1rem' }}>📅 Booking Calendar</h2>
                <Calendar
                    onChange={handleDateChange}
                    value={selectedDate}
                    tileContent={tileContent}
                    calendarType="iso8601"
                />
                <div style={{ marginTop: '2rem' }}>
                    <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>📌 Bookings for {selectedDateStr}</h3>
                    {bookingsForSelectedDate.length === 0 ? (
                        <p style={{ color: '#6b7280' }}>No bookings for this day.</p>
                    ) : (
                        <ul style={{ listStyleType: 'none', padding: 0 }}>
                            {bookingsForSelectedDate.map(booking => (
                                <li
                                    key={booking.id}
                                    style={{
                                        padding: '0.75rem 1rem',
                                        marginBottom: '0.5rem',
                                        backgroundColor: '#f9fafb',
                                        borderLeft: '4px solid #10b981',
                                        borderRadius: '0.375rem',
                                        boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)'
                                    }}
                                >
                                    <strong>{booking.bookingConfirmationCode}</strong> — {booking.user?.name || 'No name'}
                                    <br />
                                    <small style={{ color: '#6b7280' }}>{booking.event?.eventLocation?.name || 'Unknown location'}</small>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>
        </AdminLayout>
    );
}

export default BookingCalendarPage;
