package az.pashabank.apl.ms.model.thy;


import java.util.List;
import java.util.StringJoiner;

public class SecurityQuestionsReturn {

    private List<SecurityQuestion> specificComboBoxListArray;

    public SecurityQuestionsReturn() {
    }

    public SecurityQuestionsReturn(List<SecurityQuestion> specificComboBoxListArray) {
        this.specificComboBoxListArray = specificComboBoxListArray;
    }

    public List<SecurityQuestion> getSpecificComboBoxListArray() {
        return specificComboBoxListArray;
    }

    public void setSpecificComboBoxListArray(List<SecurityQuestion> specificComboBoxListArray) {
        this.specificComboBoxListArray = specificComboBoxListArray;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SecurityQuestionsReturn.class.getSimpleName() + "[", "]")
                .add("specificComboBoxListArray=" + specificComboBoxListArray)
                .toString();
    }
}
