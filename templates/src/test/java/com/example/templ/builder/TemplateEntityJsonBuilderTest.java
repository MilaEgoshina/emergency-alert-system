package com.example.templ.builder;

public class TemplateEntityJsonBuilderTest extends TemplateEntityBuilderTest<TemplateEntityJson>{
    private String title;
    private String content;

    public static TemplateEntityJsonBuilderTest builder() {
        return new TemplateEntityJsonBuilderTest();
    }

    public TemplateEntityJsonBuilderTest title(String email) {
        this.title = email;
        return this;
    }

    public TemplateEntityJsonBuilderTest content(String password) {
        this.content = password;
        return this;
    }

    @Override
    public TemplateEntityJson build() {
        return new TemplateEntityJson(title, content);
    }
}
