// src/App.js
import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import Navbar from './components/common/Navbar';
import FooterComponent from './components/common/Footer/Footer';
import LoginPage from './components/auth/LoginPage';
import RegisterPage from './components/auth/RegisterPage';
import HomePage from './components/home/HomePage/HomePage';
import FindBookingPage from './components/booking-events/MyEventPage/FindBookingPage';
import AdminPage from './components/admin/AdminPage';
import ManageBookingsPage from './components/admin/ManageBookingsPage';
import EditBookingPage from './components/admin/EditBookingPage';
import ProfilePage from './components/profile/ProfilePage/ProfilePage';
import EditProfilePage from './components/profile/EditProfilePage';
import {AdminRoute, ProtectedRoute} from './service/guard';
import '@fontsource/delius';
import AllEventsPage from "./components/booking-events/AllEvents/AllEvents";
import EventDetailsPage from "./components/booking-events/EventDetails/EventDetailsPage";
import 'leaflet/dist/leaflet.css';
import ManageUsersPage from './components/admin/ManageUsersPage';
import AddUserPage from './components/admin/AddUserPage';
import EditUserPage from './components/admin/EditUserPage';
import BookingCalendarPage from './components/admin/BookingCalendarPage';
import AdminEmailPage from './components/admin/AdminEmailPage';


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

                        <Route path="/admin/manage-users" element={<AdminRoute element={<ManageUsersPage />} />} />
                        <Route path="/admin/add-user" element={<AdminRoute element={<AddUserPage />} />} />
                        <Route path="/admin/edit-user/:userId" element={<AdminRoute element={<EditUserPage />} />} />

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
