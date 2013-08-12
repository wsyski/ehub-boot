package com.axiell.ehub.provider.publit.api;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private List<Title> titles;

    @JsonAutoDetect
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Title {
        private Integer id;
        private String title;
        private String subTitle;
        private String publisher;
        private String original_publication_year;
        private Category category;

    }

    @JsonAutoDetect
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Category {
        private Integer proprietary_category_id;
        private String name;
        private String bic;
        private String br_code;

        public Integer getProprietary_category_id() {
            return proprietary_category_id;
        }

        public void setProprietary_category_id(final Integer proprietary_category_id) {
            this.proprietary_category_id = proprietary_category_id;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getBic() {
            return bic;
        }

        public void setBic(final String bic) {
            this.bic = bic;
        }

        public String getBr_code() {
            return br_code;
        }

        public void setBr_code(final String br_code) {
            this.br_code = br_code;
        }
    }

}


