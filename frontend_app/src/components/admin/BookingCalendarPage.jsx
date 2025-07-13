import React, {useEffect, useState} from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import ApiService from '../../service/ApiService';
import {format} from 'date-fns';
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

            // count bookings per day:
            const counts = {};
            bookingList.forEach(booking => {
                const dateStr = format(new Date(booking.bookingDate), 'yyyy-MM-dd');
                counts[dateStr] = (counts[dateStr] || 0) + 1;
            });
            setBookingsPerDay(counts);
            setBookings(bookingList);
        } catch (error) {
            console.error('Error fetching bookings:', error.message);
        }
    };

    const tileContent = ({date, view}) => {
        if (view === 'month') {
            const dateStr = format(date, 'yyyy-MM-dd');
            const count = bookingsPerDay[dateStr] || 0;
            return count > 0 ? (
                <div style={{marginTop: 5, fontSize: '0.8em', color: 'green'}}>
                    {count} Booking{count > 1 ? 's' : ''}
                </div>
            ) : null;
        }
    };

    const handleDateChange = (date) => {
        setSelectedDate(date);
    };

    const selectedDateStr = format(selectedDate, 'yyyy-MM-dd');

    const bookingsForSelectedDate = bookings.filter(booking =>
        format(new Date(booking.bookingDate), 'yyyy-MM-dd') === selectedDateStr
    );

    return (
        <AdminLayout>
            <div className="booking-calendar-container">
                <h2>Booking Calendar</h2>
                <Calendar
                    onChange={handleDateChange}
                    value={selectedDate}
                    tileContent={tileContent}
                />
                <h3>Bookings for {selectedDateStr}</h3>
                {bookingsForSelectedDate.length === 0 ? (
                    <p>No bookings for this day.</p>
                ) : (
                    <ul>
                        {bookingsForSelectedDate.map(booking => (
                            <li key={booking.id}>
                                {booking.bookingConfirmationCode} - {booking.user?.name} → {booking.event?.eventName}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </AdminLayout>
    );
}

export default BookingCalendarPage;
