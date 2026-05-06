package com.mycompany.aplikasidekstopbengkeldansparepart.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * Utility that turns a regular JComboBox into a searchable auto-complete
 * dropdown. Typing in the editor filters the popup list.
 *
 * Bug-fix note: the previous implementation lost the caret position / text
 * because refreshModel() fired another document-change which reset the
 * editor. The fix uses an {@code adjusting} guard and explicitly restores
 * the editor text + caret after the model is refreshed.
 */
public final class AutoCompleteComboBox {

    private AutoCompleteComboBox() {
    }

    public static Controller attach(JComboBox<String> combo) {
        return new Controller(combo);
    }

    public static final class Controller {

        private final JComboBox<String> combo;
        private final DefaultComboBoxModel<String> model;
        private final JTextField editor;
        private List<String> items = new ArrayList<>();
        private boolean adjusting;

        private Controller(JComboBox<String> combo) {
            this.combo = combo;
            this.combo.setEditable(true);
            this.model = new DefaultComboBoxModel<>();
            this.combo.setModel(this.model);
            this.editor = (JTextField) combo.getEditor().getEditorComponent();
            this.editor.getDocument().addDocumentListener(
                    (SimpleDocumentListener) event -> filter());
        }

        public void setItems(List<String> items) {
            this.items = new ArrayList<>(items);
            SwingUtilities.invokeLater(() -> withAdjusting(() -> {
                String currentText = editor.getText();
                refreshModel(this.items);
                editor.setText(currentText);
            }));
        }

        private void refreshModel(List<String> options) {
            model.removeAllElements();
            for (String option : options) {
                model.addElement(option);
            }
        }

        private void filter() {
            if (adjusting) {
                return;
            }

            SwingUtilities.invokeLater(() -> withAdjusting(() -> {
                String text = editor.getText();
                int caretPos = editor.getCaretPosition();

                String query = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);

                List<String> filtered = new ArrayList<>();
                for (String item : items) {
                    if (query.isBlank()
                            || item.toLowerCase(Locale.ROOT).contains(query)) {
                        filtered.add(item);
                    }
                }

                refreshModel(filtered);

                // Restore the exact text the user typed (refreshModel may
                // have cleared or replaced it).
                editor.setText(text);
                try {
                    editor.setCaretPosition(
                            Math.min(caretPos, editor.getText().length()));
                } catch (IllegalArgumentException ignored) {
                    // safety — caret out of range after a model reset
                }

                if (filtered.isEmpty()) {
                    combo.hidePopup();
                } else {
                    combo.showPopup();
                }
            }));
        }

        public void setSelectedItem(String item) {
            SwingUtilities.invokeLater(() -> withAdjusting(() -> {
                combo.setSelectedItem(item);
                editor.setText(item == null ? "" : item);
            }));
        }

        public String getSelectedText() {
            Object selected = combo.getEditor().getItem();
            return selected == null ? "" : selected.toString().trim();
        }

        private void withAdjusting(Runnable action) {
            boolean previous = adjusting;
            adjusting = true;
            try {
                action.run();
            } finally {
                adjusting = previous;
            }
        }
    }
}
