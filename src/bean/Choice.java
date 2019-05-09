package bean;

import javax.swing.*;

public class Choice {
    private long choiceId;
    private JRadioButton choiceContent;
    private long num;

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

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
        this.choiceContent = new JRadioButton(content + "(" + num + "%)");
    }

    @Override
    public String toString() {
        return choiceContent.getText().substring(0,choiceContent.getText().lastIndexOf("("));
    }
}
