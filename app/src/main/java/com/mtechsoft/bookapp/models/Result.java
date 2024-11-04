package com.mtechsoft.bookapp.models;

public class Result {
    private int question_id;
    private String selected_option;

    public String getCheckedOption() {
        return selected_option;
    }

    public void setCheckedOption(String checkedOption) {
        this.selected_option = checkedOption;
    }

    public int getQuestId() {
        return question_id;
    }

    public void setQuestId(int questId) {
        this.question_id = questId;
    }

}
