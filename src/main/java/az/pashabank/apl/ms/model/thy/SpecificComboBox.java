package az.pashabank.apl.ms.model.thy;


import java.util.StringJoiner;

public class SpecificComboBox {

    private String comboBoxName;

    public SpecificComboBox() {
    }

    public SpecificComboBox(String comboBoxName) {
        this.comboBoxName = comboBoxName;
    }

    public String getComboBoxName() {
        return comboBoxName;
    }

    public void setComboBoxName(String comboBoxName) {
        this.comboBoxName = comboBoxName;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SpecificComboBox.class.getSimpleName() + "[", "]")
                .add("comboBoxName='" + comboBoxName + "'")
                .toString();
    }
}
