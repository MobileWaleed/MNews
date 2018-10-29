package loovo.com.mnews.view.callback;

import loovo.com.mnews.db.entity.NewsItem;

public interface NewsCallback {
    void onClick(NewsItem newsItem);
}

