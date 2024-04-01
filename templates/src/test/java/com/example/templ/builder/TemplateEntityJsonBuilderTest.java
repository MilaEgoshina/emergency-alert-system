package com.example.templ.builder;

public class TemplateEntityJsonBuilderTest extends TemplateEntityBuilderTest<TemplateEntityJson>{
    private String templateTitle;
    private String templateContent;

    public static TemplateEntityJsonBuilderTest builder() {
        return new TemplateEntityJsonBuilderTest();
    }

    public TemplateEntityJsonBuilderTest templateTitle(String email) {
        this.templateTitle = email;
        return this;
    }

    public TemplateEntityJsonBuilderTest templateContent(String password) {
        this.templateContent = password;
        return this;
    }

    @Override
    public TemplateEntityJson build() {
        return new TemplateEntityJson(templateTitle, templateContent);
    }
}
