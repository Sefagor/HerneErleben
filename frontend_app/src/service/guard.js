// src/ProtectedRoute.js
import React from 'react';
import {Navigate, useLocation} from 'react-router-dom';
import ApiService from '../../../../swt2-kids-app-2/frontend/src/service/ApiService';


export const ProtectedRoute = ({element: Component}) => {
    const location = useLocation();

    return ApiService.isAuthenticated() ? (
        Component
    ) : (
        <Navigate to="/login" replace state={{from: location}}/>
    );
};


export const AdminRoute = ({element: Component}) => {
    const location = useLocation();

    return ApiService.isAdmin() ? (
        Component
    ) : (
        <Navigate to="/login" replace state={{from: location}}/>
    );
};
