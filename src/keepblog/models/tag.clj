(ns keepblog.models.tag
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]]
        [keepblog.models.korma_config])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]))

;; 根据条件获得相应标签信息
(defn get-by [conditions]
  (select tags
          (where conditions)))

(defn get-tag-ids-by-names [names]
  (let [tag-ids '()]
    (for [name names]
      (let [res (conj tag-ids (:id (first (get-by {:tagname name}))))]
        (first res)))))
    
(defn get-tags-by-article-id [article-id]
  (select tags
          (join tag_article (= :tag_article.tag_id :id))
          (where {:tag_article.article_id article-id})))