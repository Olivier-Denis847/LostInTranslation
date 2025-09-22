package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        CountryCodeConverter countryConverter = new CountryCodeConverter();
        LanguageCodeConverter languageConverter = new LanguageCodeConverter();

        SwingUtilities.invokeLater(() -> {
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String langaugeCode : translator.getLanguageCodes()) {
                String langugage = languageConverter.fromLanguageCode(langaugeCode);
                languageComboBox.addItem(langugage);
            }
            languagePanel.add(languageComboBox);

            JPanel valuesPanel = new JPanel();

            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new GridLayout(0, 2));
            countryPanel.add(new JLabel("Country:"));

            String[] items = new String[translator.getCountryCodes().size()];
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryConverter.fromCountryCode(countryCode);
            }

            JList<String> countryList = new JList<>(items);
            JScrollPane scrollPane = new JScrollPane(countryList);
            countryPanel.add(scrollPane, 1);

            JLabel resultLabelText = new JLabel("Translation:");
            valuesPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            valuesPanel.add(resultLabel);

            // adding listener for when the user clicks the submit button
            countryList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    makeTranslation(languageComboBox, languageConverter, countryConverter,
                            countryList, translator, resultLabel);
                }
            });
            languageComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    makeTranslation(languageComboBox, languageConverter, countryConverter,
                            countryList, translator, resultLabel);
                }
            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(valuesPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }

    private static void makeTranslation(JComboBox<String> languageComboBox, LanguageCodeConverter languageConverter, CountryCodeConverter countryConverter, JList<String> countryList, Translator translator, JLabel resultLabel) {
        String languageRaw = languageComboBox.getSelectedItem().toString();
        String language = languageConverter.fromLanguage(languageRaw);
        String country = countryConverter.fromCountry(countryList.getSelectedValue());
        country = country.toLowerCase();

        String result = translator.translate(country, language);
        if (result == null) {
            result = "no translation found!";
        }
        resultLabel.setText(result);
    }
}
