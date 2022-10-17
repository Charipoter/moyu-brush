package com.moyu.question.bank.model.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    private List<AnswerOption> options;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<AnswerOption> options = new ArrayList<>();

        public Builder option(int sort, String description, String value) {
            options.add(AnswerOption.builder().sort(sort).description(description).value(value).build());
            return this;
        }

        public Answer build() {
            return new Answer(options);
        }

    }

}
