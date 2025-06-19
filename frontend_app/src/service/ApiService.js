import axios from "axios";

export default class ApiService {
    static BASE_URL = "http://localhost:8090";
    static HERNE_URL = "http://localhost:8091";

    static getHeader() {
        const token = localStorage.getItem("token");
        return {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
        };
    }

    /** AUTH */
    static async registerUser(registration) {
        const response = await axios.post(
            `${this.BASE_URL}/auth/register`,
            registration
        );
        return response.data;
    }

    static async loginUser(loginDetails) {
        const response = await axios.post(
            `${this.BASE_URL}/auth/login`,
            loginDetails
        );
        return response.data;
    }

    /** EVENTS */
    /** Get list of event categories */
    static async getEventCategories() {
        const response = await axios.get(
            `${this.BASE_URL}/events/categories`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    /** Get events by location, category and date range */
    static async getEventsByParams({location, category, from, to}) {
        const params = new URLSearchParams({location, category, from, to});
        const response = await axios.get(
            `${this.BASE_URL}/events/search?${params.toString()}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    /** USERS */
    static async getAllUsers() {
        const response = await axios.get(
            `${this.BASE_URL}/users/all`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getUserProfile() {
        const response = await axios.get(
            `${this.BASE_URL}/users/get-logged-in-profile-info`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getUser(userId) {
        const response = await axios.get(
            `${this.BASE_URL}/users/get-by-id/${userId}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getUserBookings(userId) {
        const response = await axios.get(
            `${this.BASE_URL}/users/get-user-bookings/${userId}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async deleteUser(userId) {
        const response = await axios.delete(
            `${this.BASE_URL}/users/delete/${userId}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async updateUserProfile(profileData) {
        const response = await axios.put(
            `${this.BASE_URL}/users/update-profile`,
            profileData,
            { headers: this.getHeader() }
        );
        return response.data;
    }
    static async addEvent(formData) {
        console.log('👉 Sending FormData to /events/add:');
        for (let pair of formData.entries()) {
            console.log(`${pair[0]}: ${pair[1]}`);
        }

        const response = await axios.post(
            `${this.BASE_URL}/events/add`,
            /* `${this.HERNE_URL}/send`,*/
            formData,
            {
                headers: {
                    ...this.getHeader(),
                    "Content-Type": "multipart/form-data",
                },
            }
        );

        console.log('✅ Response:', response.data);
        return response.data;
    }


    static async getRoomTypes() {
        const response = await axios.get(
            `${this.BASE_URL}/rooms/types`
        );
        return response.data;
    }

    static async getAllRooms() {
        const response = await axios.get(
            `${this.BASE_URL}/rooms/all`
        );
        return response.data;
    }

    static async getAllEvents() {
        const response = await axios.get(
            `${this.BASE_URL}/events/all`
        );
        return response.data;
    }

    static async getRoomById(roomId) {
        const response = await axios.get(
            `${this.BASE_URL}/rooms/room-by-id/${roomId}`
        );
        return response.data;
    }

    static async getEventById(eventId) {
        const response = await axios.get(
            `${this.BASE_URL}/events/event-by-id/${eventId}`
        );
        return response.data;
    }

    // GET all locations (you need to implement this on backend!)
    static async getAllLocations() {
        const response = await axios.get(
            `${this.BASE_URL}/locations/all`,
            { headers: this.getHeader() }
        );
        return response.data;
    }

    // UPDATE event
    static async updateEvent(eventId, formData) {
        const response = await axios.put(
            `${this.BASE_URL}/events/update/${eventId}`,
            formData,
            {
                headers: {
                    ...this.getHeader(),
                    "Content-Type": "multipart/form-data",
                },
            }
        );
        return response.data;
    }

    // DELETE event
    static async deleteEvent(eventId) {
        const response = await axios.delete(
            `${this.BASE_URL}/events/delete/${eventId}`,
            { headers: this.getHeader() }
        );
        return response.data;
    }
    static async updateUser(userId, profileData) {
        const response = await axios.put(
            `${this.BASE_URL}/users/update/${userId}`,
            profileData,
            { headers: this.getHeader() }
        );
        return response.data;
    }
    static async sendEmailToAll(emailContent) {
        const response = await axios.post(
            `${this.BASE_URL}/events/notify`,
            emailContent,
            { headers: this.getHeader() }
        );
        return response.data;
    }


    /*
        static async updateRoom(roomId, formData) {
            const response = await axios.put(
                `${this.BASE_URL}/rooms/update/${roomId}`,
                formData,
                {
                    headers: {
                        ...this.getHeader(),
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            return response.data;
        }

        static async deleteRoom(roomId) {
            const response = await axios.delete(
                `${this.BASE_URL}/rooms/delete/${roomId}`,
                {headers: this.getHeader()}
            );
            return response.data;
        }
    */
    /** BOOKINGS */
    static async bookRoom(roomId, userId, booking) {
        const response = await axios.post(
            `${this.BASE_URL}/bookings/book-room/${roomId}/${userId}`,
            booking,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async bookEvent(eventId, userId, booking) {
        const response = await axios.post(
            `${this.BASE_URL}/bookings/book-event/${eventId}/${userId}`,
            booking,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getAllBookings() {
        const response = await axios.get(
            `${this.BASE_URL}/bookings/all`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getBookingByConfirmationCode(bookingCode) {
        const response = await axios.get(
            `${this.BASE_URL}/bookings/get-by-confirmation-code/${bookingCode}`
        );
        return response.data;
    }

    static async cancelBooking(bookingId) {
        const response = await axios.delete(
            `${this.BASE_URL}/bookings/cancel/${bookingId}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    /** AUTHENTICATION CHECKER */
    static logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    static isAuthenticated() {
        return !!localStorage.getItem("token");
    }

    static isAdmin() {
        return localStorage.getItem("role") === "ADMIN";
    }

    static isUser() {
        return localStorage.getItem("role") === "USER";
    }
}
