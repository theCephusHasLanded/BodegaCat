import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

// Import translation files
import translationEN from './locales/en.json';
import translationES from './locales/es.json';
import translationFR from './locales/fr.json';
import translationDE from './locales/de.json';
import translationIT from './locales/it.json';
import translationJA from './locales/ja.json';
import translationRU from './locales/ru.json';

// Resources object with all translations
const resources = {
  en: {
    translation: translationEN
  },
  es: {
    translation: translationES
  },
  fr: {
    translation: translationFR
  },
  de: {
    translation: translationDE
  },
  it: {
    translation: translationIT
  },
  ja: {
    translation: translationJA
  },
  ru: {
    translation: translationRU
  }
};

i18n
  // detect user language
  .use(LanguageDetector)
  // pass the i18n instance to react-i18next
  .use(initReactI18next)
  // init i18next
  .init({
    resources,
    fallbackLng: 'en',
    debug: process.env.NODE_ENV === 'development',
    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
    },
    detection: {
      order: ['localStorage', 'navigator'],
      lookupLocalStorage: 'preferredLanguage',
      caches: ['localStorage'],
    },
    react: {
      useSuspense: true,
    },
  });

// Special handling for RTL languages like Arabic
document.documentElement.dir = i18n.dir();
i18n.on('languageChanged', (lng) => {
  document.documentElement.dir = i18n.dir(lng);
  document.documentElement.lang = lng;
});

export default i18n;