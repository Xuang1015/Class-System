package bean;

import javax.swing.*;

public class Choice {
    private long choiceId;
    private JRadioButton choiceContent;

    public long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(long choiceId) {
        this.choiceId = choiceId;
    }

    public JRadioButton getChoiceContent() {
        return choiceContent;
    }

    public void setChoiceContent(String content) {
        this.choiceContent = new JRadioButton(content);
    }
}
