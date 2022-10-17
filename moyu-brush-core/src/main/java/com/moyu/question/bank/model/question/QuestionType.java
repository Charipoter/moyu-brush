package com.moyu.question.bank.model.question;

import lombok.*;

import java.util.Objects;

/**
 * 选项类型：单选、填空...
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionType {

    private int id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionType type = (QuestionType) o;
        return Objects.equals(name, type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
