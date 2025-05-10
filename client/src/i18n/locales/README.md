# Language Files

This directory contains translation files for the BodegaCat application.

## Currently Supported Languages

- English (`en.json`)
- Spanish (`es.json`)

## Adding More Languages

To add support for additional languages:

1. Create a new JSON file with the appropriate language code (e.g., `fr.json` for French)
2. Copy the structure from an existing language file
3. Translate all the values while keeping the keys unchanged
4. Update the i18n.js file to import and include the new language

## Language Code Reference

- English: `en`
- Spanish: `es`
- French: `fr`
- Italian: `it`
- German: `de`
- Russian: `ru`
- Chinese: `zh`
- Arabic: `ar`
- Japanese: `ja`
- Haitian Creole: `ht`
- and many more...

## Important Notes

- Ensure all JSON files are properly formatted and encoded as UTF-8
- Test your translations thoroughly before deployment
- Consider using professional translation services for production applications