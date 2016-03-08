(ns keepblog.models.tag_article
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]]
        [keepblog.models.korma_config])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]))

(defn get-by [conditions]
  (select tag_article
          (where conditions)))

(defn- create [tag-id article-id]
  (try
    (insert tag_article
            (values {:tag_id tag-id,
                     :article_id article-id}))
    (catch Exception e
      (println e)
      nil)))

(defn create! [tag-id article-id]
  (try
    (if (create tag-id article-id)
      (first (get-by {:tag_id tag-id,
                      :article_id article-id}))
      (vali/set-error :create-tag-article-error "创建标签博文关联失败"))
    (catch Exception e
      (println e)
      (vali/set-error :base "数据库插入失败")
      nil)))
