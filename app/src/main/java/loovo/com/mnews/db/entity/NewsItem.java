package loovo.com.mnews.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import loovo.com.mnews.db.DataTypeConverter;
import loovo.com.mnews.db.MNewsTypeConverters;


@Entity
@TypeConverters(MNewsTypeConverters.class)
public class NewsItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName(value = "author")
    public String author;
    @SerializedName("source")
    @Expose
    @TypeConverters(DataTypeConverter.class)
    private Source source = null;
    @SerializedName(value = "title")
    public String title;
    @SerializedName(value = "description")
    public String description;
    @SerializedName(value = "url")
    public String url;
    @SerializedName(value = "urlToImage")
    public String urlToImage;
    @SerializedName(value = "publishedAt")
    public String publishedDate;
    @SerializedName(value = "content")
    public String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Source getSource() {
        return this.source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

}
