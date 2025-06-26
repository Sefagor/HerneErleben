import React from 'react';
import styles from './Footer.module.css';

const Footer = () => {
    const mainLinks = [
        {href: '/home', label: 'Startseite'},
        {href: '/events', label: 'Veranstaltungskalender'},
        {href: '/find-booking', label: 'Finde meine Veranstaltung'},
        {href: '/profile', label: 'Profile'}
    ];

    const herneSeite = 'https://www.herne.de/Technische-Seiten';

    const legalLinks = [
        {href: `${herneSeite}/Impressum`, label: 'Impressum'},
        {href: `${herneSeite}/Barrierefreiheit`, label: 'Barrierefreiheitserklärung'},
        {href: `${herneSeite}/Datenschutz`, label: 'Datenschutzerklärung'},
        {href: `${herneSeite}/Kontakt`, label: 'Kontakt'},
        {href: `${herneSeite}/Presse`, label: 'Presse'},
        {href: `${herneSeite}/Stellenangebote`, label: 'Stellenangebote'},
        {href: `${herneSeite}/Newsletter`, label: 'Newsletter'}
    ];

    const socialIcons = [
        {
            href: 'http://www.facebook.com/pages/Stadt-Herne/119593054779168',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/icon_facebook_44x44px_SocialMedia.png',
            alt: 'Facebook'
        },
        {
            href: 'https://www.linkedin.com/company/stadt-herne/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/LinkedIn_icon_44x44px_SocialMedia.png',
            alt: 'LinkedIn'
        },
        {
            href: 'https://www.instagram.com/stadt_herne/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/instagramm_icon_SocialMedia.png',
            alt: 'Instagram'
        },
        {
            href: 'http://www.europa.eu/index_de.htm',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/icon_europaeische_union_44x44px_SocialMedia.png',
            alt: 'Europäische Union'
        },
        {
            href: 'https://www.total-e-quality.de/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/icon_teq_44x44px_SocialMedia.png',
            alt: 'Total E-Quality'
        },
        {
            href: 'http://www.cranger-kirmes.de/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/icon_cranger_kirmes_44x44px_SocialMedia.png',
            alt: 'Cranger Kirmes'
        },
        {
            href: 'http://www.fairtradestadt-herne.de/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/icon_fairtrade_44x44px_SocialMedia.png',
            alt: 'Fairtrade-Stadt Herne'
        },
        {
            href: 'https://www.herne.de/Wirtschaft-und-Infrastruktur/Bauen-und-Wohnen/Bauen/Geoportal/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/globus_ohne_hintergrund_44x44_SocialMedia.png',
            alt: 'Geoportal'
        },
        {
            href: 'http://www.inherne.net/',
            src: 'https://www.herne.de/Bilder/Bilder-Footer-Icons/logo_inherne_44px_neu_SocialMedia.jpg',
            alt: 'InHerne Magazin'
        }
    ];

    return (
        <footer className={styles.footer}>
            <div className={styles.container_schmuckleiste_footer} role="presentation"/>

            <div className={`${styles.container_footer} ${styles.fullwidth}`}>
                <div className={styles.container}>
                    <h6 className={styles.sprunglinks_unsichtbar}>Fußzeile</h6>

                    {/* Kontakt & Logo */}
                    <div className={styles.box_footer}>
                        <div className={styles.box_footer_logo}>
                            <a href="/home">
                                <img
                                    id="stadt_herne_logo_footer"
                                    src="https://www.herne.de/media/template/images/stadt_herne_logo_footer.svg"
                                    title="Logo der Stadt Herne"
                                    alt="Logo der Stadt Herne"
                                />
                            </a>
                        </div>
                        <div className={styles.box_adresse_footer}>
                            Friedrich-Ebert-Platz 2<br/> 44623 Herne
                        </div>
                        <div className={styles.box_telefon_footer}>
                            Telefon: 0 23 23 / 16 - 0<br/> Telefax: 0 23 23 / 16 - 21 00
                        </div>
                        <div className={styles.box_internet_footer}>
                            E-Mail:{' '}
                            <a href="mailto:info@herne.de" title="E-Mail-Adresse der Stadt Herne">
                                info@herne.de
                            </a>
                            <br/> Internet:{' '}
                            <a href="https://www.herne.de" title="Internet-Adresse der Stadt Herne">
                                www.herne.de
                            </a>
                            <br/> App:{' '}
                            <a
                                href="https://www.herne.de/Stadt-und-Leben/Stadtfakten/Stadt-Herne-App/"
                                title="Informationen zur Stadt-Herne-App"
                            >
                                Stadt-Herne-App
                            </a>
                        </div>
                    </div>

                    {/* Haupt-Links */}
                    <div className={styles.box_footer}>
                        <ul className={styles.footer_links}>
                            {mainLinks.map(link => (
                                <li key={link.href}>
                                    <a href={link.href}>{link.label}</a>
                                </li>
                            ))}
                        </ul>
                    </div>

                    {/* Social Icons */}
                    <div className={styles.box_footer}>
                        <div className={styles.container_box_icons_footer}>
                            {socialIcons.map(icon => (
                                <a
                                    key={icon.src}
                                    href={icon.href}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    title={icon.alt}
                                >
                                    <img src={icon.src} alt={icon.alt}/>
                                </a>
                            ))}
                        </div>
                        <div className={styles.clearfix}/>
                    </div>

                    {/* Rechtliche Links */}
                    <div className={styles.box_footer}>
                        <ul className={styles.footer_legal}>
                            {legalLinks.map(link => (
                                <li key={link.href}>
                                    <a
                                        href={link.href}
                                        target={link.href.startsWith('http') ? '_blank' : undefined}
                                        rel={link.href.startsWith('http') ? 'noopener noreferrer' : undefined}
                                    >
                                        {link.label}
                                    </a>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </div>

            {/* Footer Bottom */}
            <div className={`${styles.container_footer_bottom} ${styles.fullwidth}`}>
                <div className={styles.footer_copy}>© Stadt Herne 2025</div>
            </div>
        </footer>
    );
};

export default Footer;
