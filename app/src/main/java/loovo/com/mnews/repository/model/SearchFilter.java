package loovo.com.mnews.repository.model;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SearchFilter {
    private String keyWord="BitCoin";
    private String language="all";
    private String sortBy="publishedAt";
    public SearchFilter(String keyWord,String lang,String sortBy){
        this.keyWord=keyWord;
        this.language=lang;
        this.sortBy=sortBy;
    }
    @Inject
    public SearchFilter(){
        System.out.println("new search filter created");
    }
    public void setKeyWord(String keyWord)
    {
        this.keyWord=keyWord;
    }
    public void setLang(String lang)
    {
        this.language=lang;
    }
    public void setSortBy(String sortBy)
    {
        this.sortBy=sortBy;
    }
    public String getKeyWord()
    {
        return keyWord;
    }
    public String getLang()
    {
        return language;
    }
    public String getSortBy()
    {
        return sortBy;
    }
}
