import React, { useState, useEffect, useRef } from 'react';
import ApiService from '../../../service/ApiService';
import styles from './EventSearch.module.css';

const EventSearch = ({ handleSearchResult, inline = false, events = [] }) => {
    // Zustände für Filter
    const [location, setLocation] = useState('');
    const [categorySearch, setCategorySearch] = useState('');
    const [category, setCategory] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');

    // Daten für Kategorie-Dropdown
    const [allCategories, setAllCategories] = useState([]);
    const [filteredCategories, setFilteredCategories] = useState([]);
    const [showCat, setShowCat] = useState(false);
    const catRef = useRef(null);

    // Kategorien vom Backend holen und deduplizieren
    useEffect(() => {
        ApiService.getEventCategories()
            .then(cats => {
                const names = cats.map(c => c.name);
                const uniq = Array.from(new Set(names));
                setAllCategories(uniq);
                setFilteredCategories(uniq);
            })
            .catch(console.error);
    }, []);

    // Dropdown schließen, wenn außerhalb geklickt wird
    useEffect(() => {
        const handler = e => {
            if (catRef.current && !catRef.current.contains(e.target)) {
                setShowCat(false);
            }
        };
        document.addEventListener('mousedown', handler);
        return () => document.removeEventListener('mousedown', handler);
    }, []);

    // Kategorieeingabe ändern
    const handleCategoryInput = e => {
        const val = e.target.value;
        setCategorySearch(val);
        setCategory(val);
        setFilteredCategories(
            allCategories.filter(name =>
                name.toLowerCase().includes(val.toLowerCase())
            )
        );
        setShowCat(true);
    };

    const selectCategory = name => {
        setCategory(name);
        setCategorySearch(name);
        setShowCat(false);
    };

    // Es müssen alle Filter ausgefüllt sein
    const isSearchEnabled = location && category && fromDate && toDate;

    const onSearch = async e => {
        e.preventDefault();

        // Neue Debug-Logeinträge
        console.log('Inline flag:', inline);
        console.log('Events list:', events);
        console.log('Filters:', { location, category, fromDate, toDate });

        if (!isSearchEnabled) {
            console.log('Search not enabled - missing filters');
            return;
        }

        if (inline) {
            console.log('--- Debugging Filter Details ---');
            const filtered = events.filter(ev => {
                const evDate = new Date(ev.eventDate);
                const from = new Date(fromDate);
                const to = new Date(toDate);

                const okDate = evDate >= from && evDate <= to;
                const city = ev.eventLocation?.city || '';
                const okLoc = city.toLowerCase().includes(location.toLowerCase());
                const okCat = ev.categories?.some(c => c.name === category);

                if (!okDate) console.log(`Event ${ev.id} failed DATE`, evDate, from, to);
                if (!okLoc) console.log(`Event ${ev.id} failed LOCATION`, city, location);
                if (!okCat) console.log(`Event ${ev.id} failed CATEGORY`, ev.categories.map(c=>c.name), category);

                return okDate && okLoc && okCat;
            });
            console.log('Matched events IDs:', filtered.map(ev => ev.id));
            handleSearchResult(filtered);
        } else {
            try {
                const res = await ApiService.searchEvents({
                    location,
                    category,
                    from: fromDate,
                    to: toDate
                });
                console.log('Server-side search response:', res);
                const list = Array.isArray(res) ? res : res.eventList || [];
                handleSearchResult(list);
            } catch (err) {
                console.error('Search error:', err);
            }
        }
    };

    return (
        <form className={styles.searchForm} onSubmit={onSearch}>
            <input
                type="text"
                placeholder="Location"
                value={location}
                onChange={e => setLocation(e.target.value)}
            />

            {/* Kategorie-Dropdown */}
            <div className={styles.categoryContainer} ref={catRef}>
                <input
                    type="text"
                    placeholder="Category"
                    value={categorySearch}
                    onChange={handleCategoryInput}
                    onFocus={() => setShowCat(true)}
                    autoComplete="off"
                />
                {showCat && (
                    <ul className={styles.dropdownList}>
                        {filteredCategories.map(name => (
                            <li
                                key={name}
                                className={styles.dropdownItem}
                                onClick={() => selectCategory(name)}
                            >
                                {name}
                            </li>
                        ))}
                    </ul>
                )}
            </div>

            <input
                type="date"
                placeholder="From"
                value={fromDate}
                onChange={e => setFromDate(e.target.value)}
            />
            <input
                type="date"
                placeholder="To"
                value={toDate}
                onChange={e => setToDate(e.target.value)}
            />
            <button type="submit" disabled={!isSearchEnabled}>
                Suchen
            </button>
        </form>
    );
};

export default EventSearch;
