// src/App.js
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './component/common/Navbar';
import FooterComponent from './component/common/Footer/Footer';
import LoginPage from './component/auth/LoginPage';
import RegisterPage from './component/auth/RegisterPage';
import HomePage from './component/home/HomePage';
import FindBookingPage from './component/booking_rooms/MyEventPage/FindBookingPage';
import AdminPage from './component/admin/AdminPage';
import EditEventPage from './component/admin/EditEventPage';
import ManageBookingsPage from './component/admin/ManageBookingsPage';
import EditBookingPage from './component/admin/EditBookingPage';
import ProfilePage from './component/profile/ProfilePage';
import EditProfilePage from './component/profile/EditProfilePage';
import { ProtectedRoute, AdminRoute } from '../../../swt2-kid-app-wandji/frontend_app/src/service/guard';
import '@fontsource/delius';
import AllEventsPage from "./component/booking_rooms/AllEvents/AllEvents";
import EventDetailsPage from "./component/booking_rooms/EventDetails/EventDetailsPage";
import 'leaflet/dist/leaflet.css';
import ManageEventsPage from './component/admin/ManageEventsPage';
import AddEventPage from './component/admin/AddEventPage'
import ManageUsersPage from './component/admin/ManageUsersPage';
import AddUserPage from './component/admin/AddUserPage';
import EditUserPage from './component/admin/EditUserPage';
import BookingCalendarPage from './component/admin/BookingCalendarPage';
import AddBookingPage from './component/admin/AddBookingPage';
import AdminEmailPage from './component/admin/AdminEmailPage';


function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Navbar />
                <div className="content">
                    <Routes>
                        {/* Public Routes */}
                        <Route exact path="/home" element={<HomePage />} />
                        <Route exact path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/events" element={<AllEventsPage />} />
                        <Route path="/find-booking" element={<FindBookingPage />} />
                        <Route path="/admin/booking-calendar" element={<AdminRoute element={<BookingCalendarPage />} />} />
                        <Route path="/admin/add-booking" element={<AdminRoute element={<AddBookingPage />} />} />
                        <Route path="/admin/send-email" element={<AdminRoute element={<AdminEmailPage />} />} />


                        {/* Protected Routes */}
                        <Route path="/events/:eventId"
                               element={<ProtectedRoute element={<EventDetailsPage />} />}
                        />
                        <Route path="/profile"
                               element={<ProtectedRoute element={<ProfilePage />} />}
                        />
                        <Route path="/edit-profile"
                               element={<ProtectedRoute element={<EditProfilePage />} />}
                        />

                        {/* Admin Routes */}
                        <Route path="/admin"
                               element={<AdminRoute element={<AdminPage />} />}
                        />

                        <Route path="/admin/add-event"
                               element={<AdminRoute element={<AddEventPage />} />}
                        />
                        <Route path="/admin/manage-events"
                               element={<AdminRoute element={<ManageEventsPage />} />}
                        />
                        <Route path="/admin/manage-users" element={<AdminRoute element={<ManageUsersPage />} />} />
                        <Route path="/admin/add-user" element={<AdminRoute element={<AddUserPage />} />} />
                        <Route path="/admin/edit-user/:userId" element={<AdminRoute element={<EditUserPage />} />} />


                        <Route path="/admin/edit-event/:eventId"
                               element={<AdminRoute element={<EditEventPage />} />}
                        />
                        <Route path="/admin/manage-bookings"
                               element={<AdminRoute element={<ManageBookingsPage />} />}
                        />
                        <Route path="/admin/edit-booking/:bookingCode"
                               element={<AdminRoute element={<EditBookingPage />} />}
                        />

                        {/* Fallback Route */}
                        <Route path="*" element={<Navigate to="/login" />} />
                    </Routes>
                </div>
                <FooterComponent />
            </div>
        </BrowserRouter>
    );
}

export default App;
