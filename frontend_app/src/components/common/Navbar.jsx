import React from 'react';
import {Link, NavLink, useNavigate} from 'react-router-dom';
import ApiService from '../../service/ApiService';

function Navbar() {
    const isAuthenticated = ApiService.isAuthenticated();
    const isAdmin = ApiService.isAdmin();
    const isUser = ApiService.isUser();
    const navigate = useNavigate();

    const handleLogout = () => {
        const isLogout = window.confirm('Are you sure you want to logout?');
        if (isLogout) {
            ApiService.logout();
            navigate('/home');
        }
    };

    return (
        <nav className="navbar">
            <div className="nav-container">
                <div className="navbar-brand">
                    <Link to="/home">
                        <img src="https://www.herne.de/media/template/images/stadt_herne_logo_header.svg"
                             alt="Herne Logo" className="nav-herne-logo"/>
                    </Link>
                </div>
                <ul className="navbar-ul">
                    <li><NavLink to="/home" activeclassname="active">Home</NavLink></li>
                    <li><NavLink to="/events" activeclassname="active">Events</NavLink></li>
                    <li><NavLink to="/find-booking" activeclassname="active">Find my Booking</NavLink></li>

                    {isUser && <li><NavLink to="/profile" activeclassname="active">Profile</NavLink></li>}
                    {isAdmin && <li><NavLink to="/admin" activeclassname="active">Admin</NavLink></li>}

                    {!isAuthenticated && <li><NavLink to="/login" activeclassname="active">Login</NavLink></li>}
                    {!isAuthenticated && <li><NavLink to="/register" activeclassname="active">Register</NavLink></li>}
                    {isAuthenticated && (
                        <li>
                            <NavLink to="/home" onClick={handleLogout} activeclassname="active">
                                Logout
                            </NavLink>
                        </li>
                    )}

                </ul>
            </div>

        </nav>
    );
}

export default Navbar;
