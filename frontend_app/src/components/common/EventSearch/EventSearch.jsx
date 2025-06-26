// frontend_app/src/common/EventSearch.jsx
import React, { useEffect, useState, useRef } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import ApiService from '../../../service/ApiService';
import styles from './EventSearch.module.css';
import { FaCalendarAlt, FaMapMarkerAlt, FaTags } from 'react-icons/fa';

const EventSearch = ({ handleSearchResult, inline = false }) => {
    const [location, setLocation] = useState('');
    const [category, setCategory] = useState('');
    const [dateType, setDateType] = useState('today');
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState('');
    const errorTimeoutRef = useRef();

    useEffect(() => {
        ApiService.getEventCategories()
            .then(list => setCategories(list))
            .catch(err => console.error(err));
    }, []);

    useEffect(() => {
        // cleanup on unmount
        return () => clearTimeout(errorTimeoutRef.current);
    }, []);

    const showError = (msg, timeout = 5000) => {
        clearTimeout(errorTimeoutRef.current);
        setError(msg);
        errorTimeoutRef.current = setTimeout(() => setError(''), timeout);
    };

    const onSearch = async () => {
        if (!location || !category) {
            return showError('Bitte wählen Sie Ort und Kategorie.');
        }

        let from, to;
        const today = new Date();
        switch (dateType) {
            case 'today':
                from = to = today.toISOString().slice(0, 10);
                break;
            case 'weekend': {
                // gelecek cumartesi ve pazar
                const sat = new Date(today);
                const day = sat.getDay();
                sat.setDate(sat.getDate() + ((6 - day + 7) % 7));
                const sun = new Date(sat);
                sun.setDate(sat.getDate() + 1);
                from = sat.toISOString().slice(0, 10);
                to = sun.toISOString().slice(0, 10);
                break;
            }
            case 'custom':
                if (!startDate || !endDate) {
                    return showError('Bitte wählen Sie das Zeitraum.');
                }
                from = startDate.toISOString().slice(0, 10);
                to = endDate.toISOString().slice(0, 10);
                break;
            default:
                return;
        }

        try {
            const params = { location, category, from, to };
            const { statusCode, eventList } = await ApiService.getEventsByParams(params);
            if (statusCode !== 200) {
                return showError('Fehler beim Abrufen der Veranstaltungen.');
            }
            if (!eventList.length) {
                return showError('Die Veranstaltung konnte nicht gefunden werden.');
            }
            handleSearchResult(eventList);
        } catch (err) {
            console.error(err);
            showError('Ein unbekannter Fehler ist passiert.');
        }
    };

    return (
        <div className={`${styles.searchRow} ${inline ? styles.inline : styles.absolute}`}>
            {/* Ort */}
            <div className={styles.item}>
                <div className={styles.label}>Ort</div>
                <div className={styles.control}>
                    <FaMapMarkerAlt className={styles.icon} />
                    <input
                        type="text"
                        value={location}
                        onChange={e => setLocation(e.target.value)}
                        placeholder="Stadt"
                        className={styles.input}
                    />
                </div>
            </div>

            {/* Kategorie */}
            <div className={styles.item}>
                <div className={styles.label}>Kategorie</div>
                <div className={styles.control}>
                    <FaTags className={styles.icon} />
                    <select
                        value={category}
                        onChange={e => setCategory(e.target.value)}
                        className={styles.select}
                    >
                        <option value="" disabled>Wählen Sie</option>
                        {categories.map(cat => (
                            // API'niz kullandığına göre cat.id ve cat.name örneğiyle
                            <option key={cat.id} value={cat.name}>
                                {cat.name}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            {/* Zeitraum */}
            <div className={styles.item}>
                <div className={styles.label}>Zeitraum</div>
                <div className={styles.control}>
                    <FaCalendarAlt className={styles.icon} />
                    <select
                        value={dateType}
                        onChange={e => setDateType(e.target.value)}
                        className={styles.select}
                    >
                        <option value="today">Heute</option>
                        <option value="weekend">Dieses Wochenende</option>
                        <option value="custom">Genauer Zeitraum</option>
                    </select>
                </div>
                {dateType === 'custom' && (
                    <div className={styles.inlinePicker}>
                        <DatePicker
                            selectsRange
                            startDate={startDate}
                            endDate={endDate}
                            onChange={([s, e]) => {
                                setStartDate(s);
                                setEndDate(e);
                            }}
                            inline
                        />
                    </div>
                )}
            </div>

            {/* Search Button */}
            <button type="button" className={styles.button} onClick={onSearch}>
                Suchen
            </button>

            {/* Hata mesajı */}
            {error && <div className={styles.error}>{error}</div>}
        </div>
    );
};

export default EventSearch;
