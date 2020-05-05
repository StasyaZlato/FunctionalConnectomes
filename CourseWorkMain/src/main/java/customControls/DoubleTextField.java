package customControls;


import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class DoubleTextField extends TextField {

    public final Pattern validText = Pattern.compile("(([1-9][0-9]*)|0)?[.,]?[0-9]*");
    private TextFormatter<Double> doubleTextFormatter;

    public DoubleTextField() {
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validText.matcher(text).matches()) {
                return c;
            } else {
                return null;
            }
        };

        doubleTextFormatter = new TextFormatter<>(filter);

        this.setTextFormatter(doubleTextFormatter);
        this.setEditable(true);
    }

    @Override
    public String getText(int start, int end) {
        return super.getText(start, end);
    }
}
